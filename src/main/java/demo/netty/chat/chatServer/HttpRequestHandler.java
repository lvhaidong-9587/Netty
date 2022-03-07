package demo.netty.chat.chatServer;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.handler.stream.ChunkedStream;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/7 0007 10:55
 * Description: Http 处理程序
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String ws;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try{
            String path = location.toURI()+"index.html";
            path = !path.contains("file:")?path:path.substring(5);
            INDEX = new File(path);
        }catch (URISyntaxException e){
            throw new IllegalStateException("无法找到索引 index.html",e);
        }
    }

    public HttpRequestHandler(String ws){
        this.ws = ws;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if(ws.equalsIgnoreCase(fullHttpRequest.uri())){
            //如果请求是一次升级了的 WebSocket 请求，则递增引用计数器（retain）并且将它传递给下一个通道
            //channelRead() 完成后，它会调用 FullHttpRequest 上的 release() 来释放其资源，所以必须使用retain()
            channelHandlerContext.fireChannelRead(fullHttpRequest.retain());
        }else {
            if(HttpUtil.is100ContinueExpected(fullHttpRequest)){
                //处理符合http 1.1的请求
                send100Continue(channelHandlerContext);
            }
            //读取 index.html
            RandomAccessFile file = new RandomAccessFile(INDEX,"r");
            HttpResponse response = new DefaultHttpResponse(
                    fullHttpRequest.protocolVersion(),HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,
                    "text/html;charset=UTF-8");
            boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
            //如果请求了keep-alive，则添加上所需要的HTTP头信息
            if(keepAlive){
                response.headers().set(
                        HttpHeaderNames.CONTENT_LENGTH,file.length());
                response.headers().set(HttpHeaderNames.CONNECTION,
                        HttpHeaderValues.KEEP_ALIVE);
            }
            //将HttpResponse写到客户端，这个只是响应数据的一部分，故不急着刷新到通道
            channelHandlerContext.write(response);

            if(channelHandlerContext.pipeline().get(SslHandler.class) == null){
                //这里使用 DefaultFileRegion，是因为他的效率是最好的，他使用零拷贝在执行传输
                channelHandlerContext.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
            }else {
                channelHandlerContext.write(new ChunkedNioFile(file.getChannel()));
            }
            //写lastHttpContent,属性数据到客户端
            ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            //没有请求keep-alive，则在写操作完成后关闭 Channel
            if(!keepAlive){
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private static void send100Continue(ChannelHandlerContext context){
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.CONTINUE);
        context.writeAndFlush(fullHttpResponse);
    }
}

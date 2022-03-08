package demo.netty.spdy;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 11:25
 * Description: 标准 http 处理通道
 */
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if(HttpUtil.is100ContinueExpected(fullHttpRequest)){
            send100Continue(channelHandlerContext);
        }
        //新建 FullHttpResponse,用于对请求的响应
        FullHttpResponse response = new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(),HttpResponseStatus.OK);
        //生成响应的内容
        response.content().writeBytes(getContent().getBytes(StandardCharsets.UTF_8));
        //生成响应头
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");

        //开始判断 keep-alive
        boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);

        //启用了keep,设置头部符合 HTTP RFC
        if(keepAlive){

            //添加内容长度消息
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
            //添加 keep-alive
            response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        }
        ChannelFuture future = channelHandlerContext.writeAndFlush(response);

        //没有启用keep，写完之后关闭连接
        if(!keepAlive){
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }

    /**
     * 获取http连接负载
     * @return 负载字符串
     */
    protected String getContent(){
        return "这些内容是通过 HTTP 传输的";
    }

    /**
     * 符合 http 1.1的请求处理
     */
    private static void send100Continue(ChannelHandlerContext context){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        //写入HTTP版本和响应状态
        context.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

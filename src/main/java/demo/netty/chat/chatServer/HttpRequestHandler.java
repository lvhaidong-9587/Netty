package demo.netty.chat.chatServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
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
            
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    private static void send100Continue(){

    }
}

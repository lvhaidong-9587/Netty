package demo.netty.spdy;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 14:35
 * Description: 协议选择以及处理请求
 */
public class DefaultSpdyOrHttpChooser extends ApplicationProtocolNegotiationHandler {

    protected DefaultSpdyOrHttpChooser(String fallbackProtocol) {
        super(fallbackProtocol);
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
         
    }
}

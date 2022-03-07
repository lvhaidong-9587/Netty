package demo.netty.chat.chatServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/7 0007 17:00
 * Description: webSocket帧的处理，ping、pong、close归于WebSocketServerProtocolHandler 自动处理
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup channelGroup;

    public TextWebSocketFrameHandler(ChannelGroup channelGroup){
        this.channelGroup = channelGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //添加引用计数，并将它写到 ChannelGroup 中所有已连接的客户端
        channelGroup.writeAndFlush(textWebSocketFrame.retain());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){

        }
        super.userEventTriggered(ctx, evt);
    }
}

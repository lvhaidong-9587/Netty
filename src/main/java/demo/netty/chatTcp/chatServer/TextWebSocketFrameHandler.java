package demo.netty.chatTcp.chatServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
        //如果该事件握手成功，则将 HttpRequest-Handler 从 ChannelPipeline 中移除，因为这个时候已经不会接收 Http 消息了
        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            ctx.pipeline().remove(HttpRequestHandler.class);
            //通知所有已经连接的 WebSocket 客户端，新的客户端已经连接上了
            channelGroup.writeAndFlush(new TextWebSocketFrame(
                    "client "+ctx.channel()+" joined"));
            //将新的 WebSocket Channel 添加到 ChannelGroup 中，以便他可以接收所有的消息
            channelGroup.add(ctx.channel());
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}

package demo.netty.code.DecoderAndEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 14:53
 * Description: 抽象编码器实现  pojo到pojo,牺牲了一部分的可重用性
 */
public class WebSocketConvertHandlerDemo extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandlerDemo.WebSocketFrame> {

    public static final WebSocketConvertHandlerDemo INSTANCE = new WebSocketConvertHandlerDemo();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, WebSocketFrame webSocketFrame,
                          List<Object> list) throws Exception {

        //从缓冲区浅拷贝（数据共享）buf，为计数器+1.计数器归0，该实例会被抛弃
        ByteBuf buf = webSocketFrame.getBuf().duplicate().retain();

        //noinspection AlibabaSwitchStatement
        switch (webSocketFrame.type){
            case BINARY -> list.add(new BinaryWebSocketFrame(buf));
            case TEXT -> list.add(new TextWebSocketFrame(buf));
            case CLOSE -> list.add(new CloseWebSocketFrame(true,0,buf));
            case CONTINUATION -> list.add(new ContinuationWebSocketFrame(buf));
            case PONG -> list.add(new PongWebSocketFrame(buf));
            case PING -> list.add(new PingWebSocketFrame(buf));
            default -> throw new IllegalStateException("不支持客户端发送的消息类型 "+ webSocketFrame);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, io.netty.handler.codec.http.websocketx.WebSocketFrame webSocketFrame,
                          List<Object> list) throws Exception {

        //通过instanceof来检索合适的FrameType
        if(webSocketFrame instanceof BinaryWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.BINARY,webSocketFrame.content().copy()));
        }else if(webSocketFrame instanceof CloseWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.CLOSE,webSocketFrame.content().copy()));
        }else if(webSocketFrame instanceof PingWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.PING,webSocketFrame.content().copy()));
        }else if(webSocketFrame instanceof PongWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.PONG,webSocketFrame.content().copy()));
        }else if(webSocketFrame instanceof TextWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.TEXT,webSocketFrame.content().copy()));
        }else if(webSocketFrame instanceof ContinuationWebSocketFrame){
            list.add(new WebSocketFrame(WebSocketFrame.FrameType.CONTINUATION,webSocketFrame.content().copy()));
        }else {
            throw new IllegalStateException("不支持的 websocket 消息: "+webSocketFrame);
        }
    }


    /**
     * 自定义消息类型
     */
    public static final class WebSocketFrame{
        //枚举确定类型
        public enum FrameType{
            BINARY,
            CLOSE,
            PING,
            PONG,
            TEXT,
            CONTINUATION
        }

        private final FrameType type;
        private final ByteBuf buf;

        public WebSocketFrame(FrameType type,ByteBuf buf){
            this.type = type;
            this.buf = buf;
        }

        public FrameType getType(){
            return type;
        }

        public ByteBuf getBuf(){
            return buf;
        }
    }
}

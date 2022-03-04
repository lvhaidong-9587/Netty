package demo.netty.serialization.ProtoBuf;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 13:17
 * Description: 基于谷歌 protoBuf的序列化，更加适合于跨语言项目
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite messageLite;

    public ProtoBufInitializer(MessageLite messageLite){
        this.messageLite = messageLite;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //在消息的整型长度域中动态分割ByteBuf
        channel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
        channel.pipeline().addLast(new ProtobufEncoder());
        channel.pipeline().addLast(new ProtobufDecoder(messageLite));
        channel.pipeline().addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            //开始表演吧
        }
    }
}

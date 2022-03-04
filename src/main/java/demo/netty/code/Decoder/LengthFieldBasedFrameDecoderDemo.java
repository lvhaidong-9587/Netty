package demo.netty.code.Decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 11:18
 * Description: 基于长度字段的帧解码器，读取头部长度并提取帧的长度，基于数据头部包含帧长度的情况
 */
public class LengthFieldBasedFrameDecoderDemo extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(
                //以8个字节来提取帧
                new LengthFieldBasedFrameDecoder(65*1024,0,8));
        //来处理8个字节帧的苦逼程序
        channel.pipeline().addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
            //来做些什么吧
        }
    }
}

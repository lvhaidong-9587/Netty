package demo.netty.code.Decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 10:19
 * Description: 基于行的解码处理（如 \n,\r\n风格）
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //基于行提取帧，并把数据包传到下一个管道处理程序
        channel.pipeline().addLast(new LineBasedFrameDecoder(65*1024));
        //别看了，我就是那个处理帧的通道处理程序
        channel.pipeline().addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
            //请开始你的表演
        }
    }
}

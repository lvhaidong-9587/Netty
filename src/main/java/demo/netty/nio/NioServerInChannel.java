package demo.netty.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 10:47
 * Description: 通道入站处理
 */
@ChannelHandler.Sharable
public class NioServerInChannel extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(NioServerInChannel.class);

    final ByteBuf buf = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("hello", StandardCharsets.UTF_8));

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       ChannelFuture future = ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
       future.addListener((ChannelFutureListener) channelFuture -> {
           if(channelFuture.isSuccess()){
               log.info("{} 写成功",channelFuture.channel().id());
           }else {
               log.info("{} 写失败",channelFuture.channel().id());
               channelFuture.cause().printStackTrace();
           }
       });
    }
}

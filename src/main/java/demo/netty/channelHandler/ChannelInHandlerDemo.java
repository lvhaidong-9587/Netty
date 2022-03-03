package demo.netty.channelHandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * PROJECT_NAME: Netty
 * @author: lhd
 * 2022/3/3 0003 13:30
 * Description: 手动释放资源
 */
@ChannelHandler.Sharable
public class ChannelInHandlerDemo extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //丢弃收到的消息
        ReferenceCountUtil.release(msg);
    }

}

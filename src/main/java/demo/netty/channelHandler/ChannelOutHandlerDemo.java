package demo.netty.channelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 13:42
 * Description: 具有在请求时延迟操作或者事件的能力
 */
public class ChannelOutHandlerDemo extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //处理资源，进行释放(如果消息是被 消耗/丢弃 并不会被传入下个 ChannelPipeline 的 ChannelOutboundHandler调用)
        ReferenceCountUtil.release(msg);
        //告诉ChannelPromise数据已经被处理
        promise.setSuccess();
    }
}

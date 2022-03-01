package demo.netty.nio;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 10:54
 * Description: 通道初始化
 */
public class NioServerChannelInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(new NioServerInChannel());
    }
}

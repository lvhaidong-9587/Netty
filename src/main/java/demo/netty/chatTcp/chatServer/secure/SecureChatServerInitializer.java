package demo.netty.chatTcp.chatServer.secure;

import demo.netty.chatTcp.chatServer.ChatServerInitializer;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 9:01
 * Description: 支持SSL的聊天通道初始化
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SslContext sslContext;

    public SecureChatServerInitializer(ChannelGroup channelGroup,SslContext sslContext){
        super(channelGroup);
        this.sslContext =sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        super.initChannel(channel);
        SSLEngine engine = sslContext.newEngine(channel.alloc());
        engine.setUseClientMode(false);
        channel.pipeline().addFirst(new SslHandler(engine));
    }
}

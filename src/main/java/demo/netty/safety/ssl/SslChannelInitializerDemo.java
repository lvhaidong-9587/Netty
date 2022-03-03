package demo.netty.safety.ssl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 16:10
 * Description: SSL加密通道初始化
 */
public class SslChannelInitializerDemo extends ChannelInitializer<Channel> {

    private final SslContext sslContext;
    private final boolean startTls;


    public SslChannelInitializerDemo(SslContext sslContext,boolean startTls){
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        SSLEngine engine = sslContext.newEngine(channel.alloc());
        //设置SslEngine是client模式还是server
        engine.setUseClientMode(sslContext.isClient());
        //将SSL添加到第一个位置
        channel.pipeline().addFirst("ssl",new SslHandler(engine,startTls));
    }
}

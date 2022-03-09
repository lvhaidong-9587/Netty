package demo.netty.charUdp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/9 0009 15:12
 * Description: udp 监视器
 */
public class LogEventMonitor {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    public LogEventMonitor(InetSocketAddress address){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new ChannelInitializer<>() {

                    @Override
                    protected void initChannel(Channel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LogEventDecoder());
                        pipeline.addLast(new LogEventHandler());
                    }
                }).remoteAddress(address);
    }

    public Channel bind(){
        return bootstrap.bind(8080).syncUninterruptibly().channel();
    }

    public void stop(){
        eventLoopGroup.shutdownGracefully();
    }

    public static void start() throws InterruptedException {
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(1));
        try{
            Channel channel = monitor.bind();
            System.out.println("监视器开始运行");
            channel.closeFuture().await();
        }finally {
            monitor.stop();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        start();
    }
}

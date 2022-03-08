package demo.netty.charUdp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 17:03
 * Description: logEvent服务引导程序
 */
public class LogEventBroadcaster {
    private final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address,File file){
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //引导该 NioDatagramChannel（无连接的）
                .channel(NioDatagramChannel.class)
                //保活
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run()throws Exception{
        //绑定 channel
        Channel channel = bootstrap.bind(0).sync().channel();
        //用来指文件字节的指针
        long pointer = 0;
        //启动主处理循环
        for(;;){
            long len = file.length();
            if(len < pointer){
                //文件被重置
                //如果有必要，将文件指针设置到该文件的最后一个字节
                pointer = len;
            }else if(len > pointer){
                
            }
        }
    }
}

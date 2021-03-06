package demo.netty.charUdp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 17:03
 * Description: logEvent广播器
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
                .option(ChannelOption.SO_BROADCAST,true)
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
                //添加内容
                RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");
                //设置当前文件的指针，这样不会把旧的发出去
                randomAccessFile.seek(pointer);
                String line;
                while ((line = randomAccessFile.readLine()) != null){
                    //写一个 LogEvent 到管道用于保存文件名和文件实体。(我们期望每个日志实体是一行长度)
                    channel.writeAndFlush(new LogEvent(null,-1, file.getAbsolutePath(), line));
                }
                //存储当前文件的位置，这样，我们可以稍后继续
                pointer = randomAccessFile.getFilePointer();
                randomAccessFile.close();
            }

        }
    }

    public void stop(){
        eventLoopGroup.shutdownGracefully();
    }

    public static void start() throws Exception {
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255",8080),
                new File("index.html"));
        broadcaster.run();
    }

    public static void main(String[] args) throws Exception {
        start();
    }

}

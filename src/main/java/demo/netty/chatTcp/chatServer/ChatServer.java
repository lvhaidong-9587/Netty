package demo.netty.chatTcp.chatServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 8:43
 * Description: 聊天服务器
 */
public class ChatServer {

    /**
     * 创建 DefaultChannelGroup,保存所有已连接的webSocket
     */
    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture start(InetSocketAddress address){
        //引导服务器
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup){
       return new ChatServerInitializer(channelGroup);
    }

    /**
     * 处理服务器关闭，并释放所有资源
     */
    public void destroy(){
        if(channel != null){
            channel.close();
        }
        channelGroup.close();
        eventLoopGroup.shutdownGracefully();
    }

    /**
     * 启动聊天服务器
     * @param port 服务器端口
     */
    public static void chatMain(int port){
        final ChatServer chatServer = new ChatServer();
        ChannelFuture future = chatServer.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }

    public static void main(String[] args) {
        chatMain(8080);
    }
}

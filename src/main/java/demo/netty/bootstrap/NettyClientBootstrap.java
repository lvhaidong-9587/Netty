package demo.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 13:34
 * Description: 客户端引导程序
 */
public class NettyClientBootstrap {

    public void client(){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
                        System.out.println("接收数据");
                        buf.clear();
                    }
                });

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.01",8080));
        future.addListener((ChannelFutureListener) channelFuture -> {
            if(channelFuture.isSuccess()){
                System.out.println("客户端连接成功");
            }else {
                System.out.println("客户端连接失败");
            }
        });
    }
}

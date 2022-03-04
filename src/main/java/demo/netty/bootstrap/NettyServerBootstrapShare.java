package demo.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 14:13
 * Description: 从channel引导客户端，用于代理或者从另一个系统提取数据
 */
public class NettyServerBootstrapShare {

    public void serverShare(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //group(父LoopGroup(),子LoopGroup())
        serverBootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>(){
                    ChannelFuture channelFuture;
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>(){
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
                                        System.out.println("接收数据");
                                    }
                                });
                        bootstrap.group(ctx.channel().eventLoop());
                        //连接到远端
                        channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1",80));
                    }
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
                       //开始你的表演
                    }
                });
        //链接完成处理业务逻辑（比如：代理）
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8080));
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if(channelFuture1.isSuccess()){
                System.out.println("服务器绑定");
            }else {
                System.out.println("服务器绑定失败");
            }
        });
    }
}

package demo.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 15:00
 * Description: netty 通道可选属性添加,这些属性会被自动使用
 */
public class NettyClientOptionBootstrap {

    public void client(){
        //创建一个 AttributeKey 以标识该属性
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        //检索 AttributeKey 的属性及其值
                        Integer idValue = ctx.channel().attr(id).get();
                        // 用 idValue 做点什么
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buf) throws Exception {
                        System.out.println("接收数据");
                    }
                });
        //该设置属性将在连接或绑定的时候生效
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
        //储存该 id 的值
        bootstrap.attr(id,123456);
        //使用配置好的 Bootstrap 实例连接到远程主机
        ChannelFuture future = bootstrap.connect(
                new InetSocketAddress("127.0.0.1", 80));
        future.syncUninterruptibly();
    }
}

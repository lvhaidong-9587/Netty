package demo.netty.oio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 10:03
 * Description: netty的堵塞服务
 */
public class OioServer {

    private void server(int port) throws Exception{
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("hello", Charset.forName("UTF-8"))
        );
        //创建堵塞的事件处理组
        EventLoopGroup group = new OioEventLoopGroup();
        try{
            //创建服务器引导程序
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //添加通道初始化程序
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            //添加入站适配
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    //通道活跃时则写入消息并刷新通道
                                    ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            //进行异步连接
            ChannelFuture future = bootstrap.bind().sync();
            //通道完成后关闭通道
            future.channel().closeFuture().sync();
        }finally {
            //优雅的关闭服务器，并释放资源
            group.shutdownGracefully().sync();
        }
    }
}

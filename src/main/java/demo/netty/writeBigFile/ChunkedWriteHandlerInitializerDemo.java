package demo.netty.writeBigFile;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 11:37
 * Description:将大文件分割成块，一块一块的传输，最后通过SSL加密传播（异步大数据切不引起高内存消耗）
 */
public class ChunkedWriteHandlerInitializerDemo extends ChannelInitializer<Channel> {

    private final File file;
    private final SslContext sslContext;

    public ChunkedWriteHandlerInitializerDemo (File file,SslContext sslContext){
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        //添加ssl
        channel.pipeline().addLast(new SslHandler(sslContext.newEngine(channel.alloc())));
        //处理分块传入的数据
        channel.pipeline().addLast(new ChunkedWriteHandler());
        //写文件
        channel.pipeline().addLast(new WriteStreamHandler());
    }


    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            //通道活跃则触发ChunkedInput来写文件的内容，FileInputStream可以换成任何InputStream
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}

package demo.netty.code.Decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 10:32
 * Description: 基于定界符的解码处理,扩展了LineBasedFrameDecoder
 * 1.由换行（“\n”）分隔;
 * 2.包括一系列项目，每个由单个空格字符分隔;
 * 3.一帧的内容代表一个“命令”：一个名字后跟一些变量参数
 */
public class DelimiterBasedFrameDecoder extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {

    }

    /**
     * POJO
     */
    public static final class User{

        private final ByteBuf name;
        private final ByteBuf args;

        public User(ByteBuf name,ByteBuf args){
            this.name = name;
            this.args = args;
        }

        public ByteBuf name(){
            return name;
        }

        public ByteBuf args(){
            return args;
        }
    }

    /**
     * POJO解码
     */
    public static final class UserDecoder extends LineBasedFrameDecoder{

        public UserDecoder(int maxLength){
            super(maxLength);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            //通过结束符从buffer中提取帧
            ByteBuf byteBuf = (ByteBuf) super.decode(ctx,buffer);
            if(byteBuf == null){
                return null;
            }
            //找到第一个空格字符的索引，前面是命令后便是参数 这里默认分割符是空格符
            int index = byteBuf.indexOf(byteBuf.readerIndex(),byteBuf.writerIndex(),(byte)' ');
            //使用包含命令和参数的切片创建新的User对象
            return new User(byteBuf.slice(byteBuf.readerIndex(),index),byteBuf.slice(index+1, buffer.writerIndex()));
        }
    }

    public static final class UserHandler extends SimpleChannelInboundHandler<User>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, User user) throws Exception {
            //拿到了User，请开始你的表演
        }
    }
}

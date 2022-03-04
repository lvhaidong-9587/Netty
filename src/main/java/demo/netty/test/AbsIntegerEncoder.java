package demo.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 16:24
 * Description: 出站编码
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        //检查是否有足够的字节用来编码
        while(buf.readableBytes()>= 4){
            //从输入的 ByteBuf 中读取下一个整数，并计算绝对值
            int value = Math.abs(buf.readInt());
            //将整数写入到编码消息的 list 中
            list.add(value);
        }
    }
}

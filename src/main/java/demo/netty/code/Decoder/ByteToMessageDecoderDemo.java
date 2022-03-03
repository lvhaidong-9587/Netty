package demo.netty.code.Decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 14:19
 * Description: 解码字节到消息
 */
public class ByteToMessageDecoderDemo extends ByteToMessageDecoder {

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readable = byteBuf.readableBytes();
        //检查是否有可读的字节
        if(readable>=4){
            //读取字节添加到list
            list.add(byteBuf.readInt());
        }
        if(readable>MAX_FRAME_SIZE){
            byteBuf.skipBytes(readable);
            throw new TooLongFrameException("帧太大！");
        }
    }
}

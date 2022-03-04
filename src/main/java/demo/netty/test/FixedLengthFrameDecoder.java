package demo.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 15:34
 * Description: 固定长度读取帧
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength){
        if(frameLength <= 0){
            throw new IllegalArgumentException("你给的数据长度，我很难为你办事啊: "+ frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes()>= frameLength){
            ByteBuf buf = byteBuf.readBytes(frameLength);
            list.add(buf);
        }
    }
}

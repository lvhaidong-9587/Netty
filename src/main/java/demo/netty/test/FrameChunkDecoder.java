package demo.netty.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 16:59
 * Description: 分块解码
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxFrameSize;

    /**
     * 指定将要产生的帧的最大值
     * @param maxFrameSize 最大值
     */
    public FrameChunkDecoder(int maxFrameSize){
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        if(readableBytes > maxFrameSize){
            //如果帧太大，则丢弃它并抛出一个异常
            byteBuf.clear();
            throw new TooLongFrameException();
        }
        //..否则，从ByteBuf中取出一个新的帧
        ByteBuf buf = byteBuf.readBytes(readableBytes);
        //将该帧添加到list后传递到下一个通道处理程序
        list.add(buf);
    }
}

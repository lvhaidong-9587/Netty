package netty.test;

import demo.netty.test.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 16:19
 * Description: 解码入站测试
 */
public class FrameChunkDecoderTest {

    @Test
    public void testFramesDecoded(){
        //创建一个 ByteBuf,并向它写入 9 字节
        ByteBuf buf = Unpooled.buffer();
        for(int i = 0;i < 9;i++){
            buf.writeByte(i);
        }

        ByteBuf input = buf.duplicate();
        //创建一个 EmbeddedChannel , 并向它提供一个字节为 3 的帧
        EmbeddedChannel channel = new EmbeddedChannel(
          new FrameChunkDecoder(3)
        );

        //向它写入 2 个字节，断言 它会产生一个新帧
        assertTrue(channel.writeInbound(input.readBytes(2)));
        try{
            //写入一个 4 字节大小的帧，尝试捕获定义的异常
            channel.writeInbound(input.readBytes(4));
            //上一步没有抛出异常则说明测试失败
            Assert.fail();
        }catch (TooLongFrameException e){
            //
        }
        //写入剩下的 2 个字节，并断言将会产生一个有效帧
        assertTrue(channel.writeInbound(input.readBytes(3)));
        //标记该channel为已完成状态
        assertTrue(channel.finish());

        //读取产生的消息，并验证值
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(2),read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.skipBytes(4).readSlice(3),read);
        read.release();
        buf.release();
    }


}

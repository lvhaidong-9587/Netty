package netty.test;

import demo.netty.test.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 15:42
 * Description: 对FixedLengthFrameDecoder进行测试
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFramesDecoded(){
        //创建ByteBuf，并塞入字节
        ByteBuf buf = Unpooled.buffer();
        for(int i = 0;i<9;i++){
            buf.writeByte(i);
        }
        //对buf进行强复制，数据不共享，原数据读写索引位置不变
        ByteBuf input = buf.duplicate();
        //创建一个嵌入式通道
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3)
        );
        //将数据写入EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));
        //标记Channel状态为已完成
        assertTrue(channel.finish());

        //读取所生成的消息，并且验证是否有 3 帧（切片），其中每帧（切片）都为 3 字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3),read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();

    }

    @Test
    public void testFramesDecoded2(){
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3));
        //返回 false，因为没有一个完整的可供读取的帧
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

}

package demo.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 13:10
 * Description: netty byteBuf读取demo
 */
public class ReadDemo {

    private static final ByteBuf BUF = Unpooled.wrappedBuffer("hello,byteBuf".getBytes(StandardCharsets.UTF_8));


    public static void main(String[] args) {
        ReadDemo readDemo = new ReadDemo();
        ByteBuf byteBuf = readDemo.writeByteBuf(" hello,word ");
//        readDemo.readByteBuf(byteBuf);
//        readDemo.readByteBufString(byteBuf);
        readDemo.readComBuf(byteBuf);
    }

    /**
     * 遍历读取ByteBuf内容
     * @param buf ByteBuf 缓冲器
     */
    public void readByteBuf(ByteBuf buf){
        while (buf.isReadable()){
            System.out.println(buf.readByte());
        }
    }

    /**
     * 直接输出ByteBuf为String
     * @param buf ByteBuf 缓冲器
     */
    public void readByteBufString(ByteBuf buf){
        System.out.println(buf.toString(StandardCharsets.UTF_8));
    }

    /**
     * 复合缓存区的读取
     * @param buf 缓存器
     *            复合缓冲区不允许访问其内部可能存在的支持数组，也不允许直接访问数据
     */
    public void readComBuf(ByteBuf buf){
        //复合缓存区的创建，headerBuf 和 bodyBuf 可以使用 堆缓存区 或者 直接缓存区
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf =  Unpooled.buffer(4,16);
        ByteBuf bodyBuf = Unpooled.directBuffer(4,16);
        compositeByteBuf.addComponents(headerBuf, bodyBuf);

        //读取数据

        for(int i = 0;i<compositeByteBuf.numComponents();i++){
            System.out.println(compositeByteBuf.component(i).toString(StandardCharsets.UTF_8));
        }
    }

    /**
     * 写入内容到ByteBuf缓存器
     * @param i 将要写入的内容，可以使其他基本类型
     *          writableBytes被用于判断是否有足够的写空间
     */
    public ByteBuf writeByteBuf(String i){
        //堆缓存区
        ByteBuf byteBuf = Unpooled.buffer(12,16);
       //直接缓存区
        ByteBuf byteBuf1 = Unpooled.directBuffer(12,16);
        //复合缓存区
        ByteBuf byteBuf2 = Unpooled.compositeBuffer(16);

        while (byteBuf2.writableBytes()>=4){
            byteBuf2.writeBytes(i.getBytes(StandardCharsets.UTF_8));
        }
        return byteBuf2;
    }
}

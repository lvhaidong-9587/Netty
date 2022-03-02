package demo.netty.bytebuf.readOrWrite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 13:10
 * Description: netty byteBuf读取demo
 */
public class ByteBufReadOrWriteDemo {


    /**
     * 遍历读取ByteBuf内容
     *
     * @param buf ByteBuf 缓冲器
     */
    public void readByteBuf(ByteBuf buf) {
        while (buf.isReadable()) {
            System.out.println(buf.readByte());
        }
    }

    /**
     * 直接输出ByteBuf为String
     *
     * @param buf ByteBuf 缓冲器
     */
    public void readByteBufString(ByteBuf buf) {
        System.out.println(buf.toString(StandardCharsets.UTF_8));
    }

    /**
     * 复合缓存区的读取
     *
     * @param buf 缓存器
     *            复合缓冲区不允许访问其内部可能存在的支持数组，也不允许直接访问数据
     *            处理方式类似于直接缓存区
     */
    public void readComBuf(ByteBuf buf) {
        //读取数据
        CompositeByteBuf compBuf = (CompositeByteBuf) buf;
        //删除头部组件
        compBuf.removeComponent(0);
        //遍历获取组件
        for (int i = 0; i < compBuf.numComponents(); i++) {
            System.out.println(compBuf.component(i).toString(StandardCharsets.UTF_8));
        }
    }

    /**
     * 写入内容到ByteBuf缓存器
     *
     * @param i 将要写入的内容，可以使其他基本类型
     *          writableBytes被用于判断是否有足够的写空间
     */
    public ByteBuf writeByteBuf(String i) {

        //复合缓存区的创建，CompositeByteBuf是一个视图，它可能既包含堆缓冲区，也包含直接缓冲区
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        // 假设一条消息由 header 和 body 两部分组成，headerBuf 和 bodyBuf 可以使用 堆缓存区 或者 直接缓存区
        //堆缓存区
        ByteBuf header = Unpooled.buffer(4, 1024);
        //直接缓存区
        ByteBuf body = Unpooled.directBuffer(4, 1024);
        while (body.writableBytes() >= 4) {
            body.writeBytes(i.getBytes(StandardCharsets.UTF_8));
        }
        compositeByteBuf.addComponents(header, body);
        return compositeByteBuf;

    }

    /**
     * 从索引位置开始读取字符
     * @param i 索引位置
     * @param buf 缓存区
     * @return 对应字符
     */
    public ByteBuf readIndex(ByteBuf buf,int i){
        return buf.readerIndex(i);
    }

    /**
     * 从索引位置开始写入字符
     * @param buf 缓存区
     * @param i 索引位置
     * @return 写入后的缓存区
     */
    public ByteBuf writeIndex(ByteBuf buf,int i){
        return buf.writerIndex(i);
    }

}

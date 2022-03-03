package demo.netty.bytebuf.readOrWrite;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

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
        ByteBuf header = Unpooled.buffer( i.length());
        //直接缓存区
        ByteBuf body = Unpooled.directBuffer(i.length());
        while (body.writableBytes() >= i.length()) {
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

    /**
     * 缓冲区复制，缓冲区是一个专门展示ByteBuf内容的视图，无参
     * @param buf 将要被复制的缓存器
     * @return 复制的缓存器
     */
    public ByteBuf copyByteBuf(ByteBuf buf){
        return buf.copy();
    }

    /**
     * 缓冲区复制，缓冲区是一个专门展示ByteBuf内容的视图，带参
     * @param buf 将要被复制的缓存器
     * @param a 开始复制位置，包括
     * @param b 结束复制位置，不包括
     * @return 复制的缓存器
     */
    public ByteBuf copyByteBuf(ByteBuf buf ,int a,int b){
        return buf.copy(a,b);
    }

    /**
     * 缓冲区复制并修改（数据共享，参照上面方法，该方法也可不带参）
     * @param buf 缓冲区
     * @param a 起始点
     * @param b 终点
     */
    public void sliceByteBuf(ByteBuf buf,int a,int b){
        ByteBuf byteBuf = buf.slice(a,b);
        buf.setByte(0,(byte)'O');
        //这里的数据是共享的，所以修改后在其他地方也可见
        if(buf.getByte(0) == byteBuf.getByte(0)){
            System.out.println("buf 和 byteBuf 相同");
        }
    }

    /**
     * 缓冲区复制并修改（不共享数据）
     * @param buf 缓冲区
     * @param a 起始点
     * @param b 终止点
     */
    public void copySetByteBuf(ByteBuf buf,int a,int b){
        ByteBuf byteBuf = buf.copy(a,b);
        byteBuf.setByte(0,(byte)'O');
        //这里的数据是不共享的，所以修改后在其他地方不可见
        if(buf.getByte(0) != byteBuf.getByte(0)){
            System.out.println("buf 和 byteBuf 不相同");
        }
    }



}

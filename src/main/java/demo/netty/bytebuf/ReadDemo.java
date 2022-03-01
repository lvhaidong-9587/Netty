package demo.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 13:10
 * Description: netty byteBuf读取demo
 */
public class ReadDemo {

    public static void main(String[] args) {
        ReadDemo readDemo = new ReadDemo();
        //写内容
        ByteBuf byteBuf = readDemo.writeByteBuf(" hello,word ");
        //读取复合缓冲区
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
     *            处理方式类似于直接缓存区
     */
    public void readComBuf(ByteBuf buf){
        //读取数据
        CompositeByteBuf compBuf = (CompositeByteBuf) buf;
        //删除头部组件
        compBuf.removeComponent(0);
        //遍历获取组件
        for(int i = 0;i< compBuf.numComponents();i++){
            System.out.println(compBuf.component(i).toString(StandardCharsets.UTF_8));
        }
    }

    /**
     * 写入内容到ByteBuf缓存器
     * @param i 将要写入的内容，可以使其他基本类型
     *          writableBytes被用于判断是否有足够的写空间
     */
    public ByteBuf writeByteBuf(String i){

        //复合缓存区的创建，CompositeByteBuf是一个视图，它可能既包含堆缓冲区，也包含直接缓冲区
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        // 假设一条消息由 header 和 body 两部分组成，headerBuf 和 bodyBuf 可以使用 堆缓存区 或者 直接缓存区
        //堆缓存区
        ByteBuf header = Unpooled.buffer(12,16);
        //直接缓存区
        ByteBuf body = Unpooled.directBuffer(12,16);
        while (body.writableBytes() >= 4){
            body.writeBytes(i.getBytes(StandardCharsets.UTF_8));
        }
        compositeByteBuf.addComponents(header, body);
        return compositeByteBuf;
    }

}

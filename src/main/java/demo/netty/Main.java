package demo.netty;

import demo.netty.bytebuf.ByteBufDemo;
import io.netty.buffer.ByteBuf;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 17:13
 * Description: 启动类
 */
public class Main {

    public static void main(String[] args) {
        ByteBufDemo byteBufDemo = new ByteBufDemo();
        //写内容
        ByteBuf byteBuf = byteBufDemo.writeByteBuf(" hello,word ");
        //读取复合缓冲区
        byteBufDemo.readComBuf(byteBuf);
    }
}

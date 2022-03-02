package demo.netty;

import demo.netty.bytebuf.readOrWrite.ByteBufReadOrWriteDemo;
import demo.netty.bytebuf.select.ByteBufSelectDemo;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 17:13
 * Description: 启动类
 */
public class Main {

   public static ByteBufReadOrWriteDemo byteBufReadOrWriteDemo = new ByteBufReadOrWriteDemo();
   public static ByteBufSelectDemo bufSelectDemo = new ByteBufSelectDemo();

    public static void main(String[] args) {
        ByteBuf byteBuf = byteBufReadOrWriteDemo.writeByteBuf("ok \r");
        //读取复合缓冲区
        byteBufReadOrWriteDemo.readComBuf(byteBuf);
        int i = bufSelectDemo.selectCr(byteBuf);
        System.out.println(i);
//        ByteBuf buf = byteBufReadOrWriteDemo.readIndex(byteBuf,i);
//        byteBufReadOrWriteDemo.readByteBufString(buf);
    }

    /**
     * 读写测试
     */
    public static void readOrWrite(){
        //写内容
        ByteBuf byteBuf = byteBufReadOrWriteDemo.writeByteBuf(" hello,word null");
        //读取复合缓冲区
        byteBufReadOrWriteDemo.readComBuf(byteBuf);
    }
}

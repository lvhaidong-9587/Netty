package demo.netty.bytebuf.select;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

/**
 * @author: lhd
 * Date: 2022/3/1 0001 17:27
 * Description: ByteBuf查询 demo
 */
public class ByteBufSelectDemo {


    /**
     * 查找以 NULL 结尾的内容
     * @param buf 输入的内容
     * @return 该字符所在的索引位置
     */
    public int selectNull(ByteBuf buf){
        return buf.forEachByte(ByteProcessor.FIND_NUL);
    }

    /**
     * 查找回车符
     * @param buf 输入的内容
     * @return 该字符所在的索引位置
     */
    public int selectCr(ByteBuf buf){
        return buf.forEachByte(ByteProcessor.FIND_CR);
    }
}

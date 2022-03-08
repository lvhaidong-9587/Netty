package demo.netty.memcached;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 15:50
 * Description: 实现 Memcached 的其余部分协议,必须要将 client.op(op 任何新的操作添加)转换为其中一个方法请求,2
 */
public class Opcode {
    public static final byte GET = 0x00;
    public static final byte SET = 0x01;
    public static final byte DELETE = 0x04;
}

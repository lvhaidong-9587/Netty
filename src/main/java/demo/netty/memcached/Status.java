package demo.netty.memcached;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 15:48
 * Description: 实现 Memcached 的其余部分协议,必须要将 client.op(op 任何新的操作添加)转换为其中一个方法请求,1
 */
public class Status {
    /**
     * 没有错误
     */
    public static final short NO_ERROR = 0x0000;
    /**
     * 未找到密钥
     */
    public static final short KEY_NOT_FOUND = 0x0001;
    /**
     * 秘钥存在
     */
    public static final short KEY_EXISTS = 0x0002;
    /**
     * 值太大
     */
    public static final short VALUE_TOO_LARGE = 0x0003;
    /**
     * 无效参数
     */
    public static final short INVALID_ARGUMENTS = 0x0004;
    /**
     * 未存储的项目
     */
    public static final short ITEM_NOT_STORED = 0x0005;
    /**
     * INC_DEC_NON_NUM_VAL
     */
    public static final short INC_DEC_NON_NUM_VAL = 0x0006;
}


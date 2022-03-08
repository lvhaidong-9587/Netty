package demo.netty.memcached;

import java.util.Random;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 15:31
 * Description: 自定义编码器-缓存请求 这个类会将请求发送到：Memcached server
 */
public class MemcachedRequest {
    //创建一个随机数
    private static final Random rand = new Random();
    //确定硬编码
    private final int magic = 0x80;
    //操作 例如 set、get
    private final byte opCode;
    //key值 delete、get、set
    private final String key;
    //random
    private final int flags = 0xdeadbeef;
    //0 = 项目永不过期
    private final int expires;
    //如果设置了set，则值
    private final String body;
    //Opaque
    private final int id = rand.nextInt();
    //数据版本检查...未使用
    private final long cas = 0;
    //并非所有操作都有附加功能
    private final boolean hasExtras;

    public MemcachedRequest(byte opcode, String key, String value) {
        this.opCode = opcode;
        this.key = key;
        this.body = value == null ? "" : value;
        this.expires = 0;
        //在我们的示例中，只有 set 命令有附加功能
        hasExtras = opcode == Opcode.SET;
    }

    public MemcachedRequest(byte opCode, String key) {
        this(opCode, key, null);
    }

    /**
     * 幻数，它可以用来标记文件或者协议的格式
     * @return
     */
    public int magic() {
        return magic;
    }

    /**
     * opCode,反应了响应的操作已经创建了
     * @return
     */
    public int opCode() {
        return opCode;
    }

    /**
     * 执行操作的 key
     * @return
     */
    public String key() {
        return key;
    }

    /**
     * 使用的额外的 flag
     * @return
     */
    public int flags() {
        return flags;
    }

    /**
     * 表明到期时间
     * @return
     */
    public int expires() {
        return expires;
    }

    /**
     * body
     * @return
     */
    public String body() {
        return body;
    }

    /**
     * 请求的 id。这个id将在响应中回显
     * @return
     */
    public int id() {
        return id;
    }

    /**
     * compare-and-check 的值
     * @return
     */
    public long cas() {
        return cas;
    }

    /**
     * 如果有额外的使用，将返回 true
     * @return
     */
    public boolean hasExtras() {  //10
        return hasExtras;
    }
}

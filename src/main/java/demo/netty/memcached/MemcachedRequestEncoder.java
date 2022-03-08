package demo.netty.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 15:54
 * Description: Memcached 请求编码器
 */
public class MemcachedRequestEncoder extends MessageToByteEncoder<MemcachedRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MemcachedRequest msg, ByteBuf byteBuf) throws Exception {
        //转换的 key 和实际请求的 body 到字节数组
        byte[] key = msg.key().getBytes(CharsetUtil.UTF_8);
        byte[] body = msg.body().getBytes(CharsetUtil.UTF_8);
        //计算body的大小 正文的总大小 = 键大小 + 内容大小 + 附加大小
        int bodySize = key.length + body.length + (msg.hasExtras() ? 8 : 0);
        //写幻数到ByteBuf
        byteBuf.writeByte(msg.magic());
        //写操作码字节
        byteBuf.writeByte(msg.opCode());
        //写入密钥长度（2 字节）,密钥长度最大为 2 个字节，即 Java short
        byteBuf.writeShort(key.length);
        //写附加长度 (1 byte)
        int extraSize =  msg.hasExtras() ? 0x08 : 0x0;
        byteBuf.writeByte(extraSize);
        //byte 是数据类型，目前在 Memcached 中没有实现但需要
        byteBuf.writeByte(0);
        //接下来的两个字节是保留的，当前未实现但需要
        byteBuf.writeShort(0);
        //写入总长度（4 字节 - 32 位整数）
        byteBuf.writeInt(bodySize);
        //write opaque ( 4 bytes) - 在响应中返回的 32 位 int
        byteBuf.writeInt(msg.id());
        //write CAS ( 8 bytes),字节头以 CAS 结束
        byteBuf.writeLong(msg.cas());

        if (msg.hasExtras()) {
            //写入额外内容（标志和到期，每个 4 个字节） - 总共 8 个字节
            byteBuf.writeInt(msg.flags());
            byteBuf.writeInt(msg.expires());
        }
        //write key
        byteBuf.writeBytes(key);
        //write value
        byteBuf.writeBytes(body);
    }
}

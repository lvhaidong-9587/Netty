package demo.netty.code.Decoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/4 0004 10:32
 * Description: 基于定界符的解码处理,扩展了LineBasedFrameDecoder
 * 1.由换行（“\n”）分隔;
 * 2.包括一系列项目，每个由单个空格字符分隔;
 * 3.一帧的内容代表一个“命令”：一个名字后跟一些变量参数
 */
public class DelimiterBasedFrameDecoder extends ChannelInitializer<Channel> {
    
}

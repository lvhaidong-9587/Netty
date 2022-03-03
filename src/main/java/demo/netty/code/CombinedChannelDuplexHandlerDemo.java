package demo.netty.code;

import demo.netty.code.Decoder.ByteToMessageDecoderDemo;
import demo.netty.code.Encoder.MessageToByteEncoderDemo;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 16:01
 * Description: 编解码器 有一定的可重用性
 */
public class CombinedChannelDuplexHandlerDemo extends CombinedChannelDuplexHandler<ByteToMessageDecoderDemo, MessageToByteEncoderDemo> {

    public CombinedChannelDuplexHandlerDemo(){
        super(new ByteToMessageDecoderDemo(),new MessageToByteEncoderDemo());
    }
}

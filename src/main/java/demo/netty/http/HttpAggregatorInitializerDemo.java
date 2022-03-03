package demo.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 16:46
 * Description: http 消息聚合和压缩，http消息有多部分组成，需要将他们聚合在一起
 */
public class HttpAggregatorInitializerDemo extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpAggregatorInitializerDemo(boolean client){
        this.client = client;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if(client){
            pipeline.addLast("codec",new HttpClientCodec());
            //处理来自服务器的压缩内容
            pipeline.addLast("decompressor",new HttpContentDecompressor());
        }else {
            pipeline.addLast("codec",new HttpServerCodec());
            //添加客户端支持的内容压缩器来压缩数据
            pipeline.addLast("compressor",new HttpContentCompressor());
        }
        //设定最大消息值为512kb
        pipeline.addLast("aggegator",new HttpObjectAggregator(512*1024));
    }
}

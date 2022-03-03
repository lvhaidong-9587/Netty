package demo.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 16:36
 * Description: http通道初始化
 */
public class HttpPipelineInitializerDemo extends ChannelInitializer<Channel> {

    private final boolean client;

    public HttpPipelineInitializerDemo(boolean client){
        this.client = client;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if(client){
            pipeline.addLast("decoder",new HttpResponseDecoder());
            pipeline.addLast("encoder",new HttpRequestEncoder());
        }else{
            pipeline.addLast("decoder",new HttpRequestDecoder());
            pipeline.addLast("encoder",new HttpResponseEncoder());
        }
    }
}

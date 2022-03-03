package demo.netty.webSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/3 0003 17:20
 * Description: webSocketServer 通道初始化
 */
public class WebSocketServerInitializerDemo extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(
                new HttpServerCodec(),
                //在握手时聚合HttpRequest
                new HttpObjectAggregator(65535),
                //如果
                new WebSocketServerProtocolHandler("/ws")
        );
    }
}

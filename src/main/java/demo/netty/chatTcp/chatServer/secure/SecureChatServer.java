package demo.netty.chatTcp.chatServer.secure;

import demo.netty.chatTcp.chatServer.ChatServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * PROJECT_NAME: Netty
 *
 * @author: lhd
 * 2022/3/8 0008 9:13
 * Description: 支持SSL的聊天服务端
 */
public class SecureChatServer extends ChatServer {

    private final SslContext sslContext;

    public SecureChatServer(SslContext sslContext){
        this.sslContext = sslContext;
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
        return new SecureChatServerInitializer(channelGroup,sslContext);
    }

    public static void chatStart(int port) throws Exception {
        SelfSignedCertificate signedCertificate = new SelfSignedCertificate();
        SslContext sslContext = SslContext.newServerContext(
                signedCertificate.certificate(),signedCertificate.privateKey()
        );
        final SecureChatServer server = new SecureChatServer(sslContext);
        ChannelFuture future = server.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(server::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }
}

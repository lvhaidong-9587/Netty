package demo.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: lhd
 * Date: 2022/2/26 0026 11:45
 * Description: 异步IO
 */
public class PlainNioServer {

    private final static Logger log = LoggerFactory.getLogger(PlainNioServer.class);

    public void server(int port) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        ServerSocket ss = socketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        //绑定到服务器指定的端口
        ss.bind(address);
        //建立新的链接
        Selector selector = Selector.open();
        //注册socket通道
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("hello".getBytes());
        for(;;){
            try{
                //等待事件传入（堵塞）
                selector.select();
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
            //获取新的SelectionKey实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try{
                    //检查时间是一个新的链接后准备接受
                    if(key.isWritable()){
                        ServerSocketChannel server =
                                (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //注册客户端
                        client.register(selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ,msg.duplicate());
                        log.info("Accepted connection from {}",client);
                    }
                    //检查客户端是否可写
                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()){
                            //为客户端写入数据，直到缓存区为空
                            if(client.write(buffer) == 0){
                                break;
                            }
                        }
                        //关闭连接
                        client.close();
                    }
                }catch (IOException e){
                    key.channel().close();
                }finally {
                    key.channel().close();
                }
            }
        }

    }
}

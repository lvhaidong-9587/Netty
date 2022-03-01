package demo.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author: lhd
 * Date: 2022/2/26 0026 11:01
 * Description: 阻塞IO
 */
public class PlainOioServer {

    private static final Logger log = LoggerFactory.getLogger(PlainOioServer.class);

    public void serve(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);
        try{
            for(;;){
                final Socket cliantSocket = socket.accept();
                log.info("接受连接: {}",cliantSocket);

                new Thread(() -> {
                    OutputStream out;
                    try{
                        out = cliantSocket.getOutputStream();
                        out.write("holle".getBytes(StandardCharsets.UTF_8));
                        out.flush();
                        cliantSocket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                        try{
                            cliantSocket.close();
                        }catch (IOException ex){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

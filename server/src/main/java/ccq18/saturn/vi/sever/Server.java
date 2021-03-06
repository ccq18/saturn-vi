package ccq18.saturn.vi.sever;


import ccq18.saturn.vi.ThreadPool;
import ccq18.saturn.vi.sever.protocol.http.HttpProtocol;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class Server {


    public static boolean run(int port, int nThreads,  RequestHandle requestHandle ) throws Exception {

        // 服务端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(port);
        ExecutorService newThreadPool = ThreadPool.newThreadPool(nThreads);
        log.info("sever port:{}", port);
        Socket client = null;
        boolean f = true;
        while (f) {
            //等待客户端的连接，如果没有获取连接
            client = server.accept();
            log.info("client connect");
            log.info("client:{}", client.getInetAddress().getHostAddress());
            //为每个客户端连接开启一个线程
            newThreadPool.execute(new ServerThread(client, new HttpProtocol(requestHandle)));
        }
        server.close();
        return true;
    }

}
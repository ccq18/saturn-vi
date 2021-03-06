package ccq18.saturn.vi.gateway;

import ccq18.saturn.vi.ThreadPool;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class Gateway {

    public static void main(String[] args) throws Exception{
        //String proxyhost = "service.issue.pw";int proxyport = 80;
        String proxyhost = "127.0.0.1"; int proxyport = 2006;// 代理端口
        int port = 20006;//服务端口
        int nThreads = 20;//线程池
        //服务端在20006端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(port);
        log.info("sever port:{}",port);
        ExecutorService newThreadPool = ThreadPool.newThreadPool(nThreads);
        boolean f = true;
        while(f){
            //等待客户端的连接，如果没有获取连接
            Socket client = server.accept();
            log.info("client connect");
            log.info("client:{}",client.getInetAddress().getHostAddress());
            //为每个客户端连接开启一个线程
            Socket proxyserver = new Socket(proxyhost, proxyport);
            log.info("new  proxyserver");

            newThreadPool.execute(new GatewayThread(proxyhost,proxyserver,client));
        }
        server.close();
    }
}

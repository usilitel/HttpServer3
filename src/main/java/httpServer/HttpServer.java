package httpServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.logging.Logger;


public class HttpServer {
    //private static Logger log = Logger.getLogger(HttpServer.class.getName());

    public static void main (String... args) throws Throwable {

        //System.out.println(String.format("aaaaa", 3, "sssss"));


        try (ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("Server started, please visit: http://localhost:" + args[0]);
            while (!Thread.currentThread().isInterrupted()) {
                Socket s = ss.accept();
                System.out.println("Client accepted");
                new Thread(new HelloWorldServer(s)).start();
            }
        }
    }


    /*
    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080); // создали серверный сокет
        System.out.println("Server started. Waiting for a client...");
        while (true) {
            Socket s = ss.accept(); // создали клиентский сокет
            System.out.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }
    */
}
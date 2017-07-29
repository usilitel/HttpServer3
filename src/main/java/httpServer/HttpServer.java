package httpServer;

import propertiesReader.PropertiesReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.logging.Logger;


public class HttpServer {
    //private static Logger log = Logger.getLogger(HttpServer.class.getName());
    static PropertiesReader MimeTypes = new PropertiesReader("src\\main\\resources\\config\\MimeTypes.properties");
    static PropertiesReader ExtensionPaths = new PropertiesReader("src\\main\\resources\\config\\ExtensionPaths.properties");




    public static void main (String... args) throws Throwable {

        String a = "test2.html/";
        String b = a.substring(a.lastIndexOf(".")+1);
        System.out.println(b);



        //System.out.println("---");
        //MimeTypes.loadFile("src\\main\\resources\\config\\MimeTypes.properties");

        System.out.println("---");
        System.out.println("значение поля jpg:");
        System.out.println(MimeTypes.getValue("jpg"));

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
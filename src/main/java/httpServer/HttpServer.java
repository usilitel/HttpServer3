package httpServer;

import propertiesReader.PropertiesReader;

import java.net.ServerSocket;
import java.net.Socket;


// главный класс, запускающий HTTP сервер
public class HttpServer {

    // сразу загружаем конфигурационные файлы
    static PropertiesReader MimeTypes = new PropertiesReader("src\\main\\resources\\config\\MimeTypes.properties");
    static PropertiesReader ExtensionPaths = new PropertiesReader("src\\main\\resources\\config\\ExtensionPaths.properties");


    public static void main (String... args) throws Throwable {

        try (ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]))) {
            System.out.println("Server started, please visit: http://localhost:" + args[0]);
            while (true) {
                Socket s = ss.accept();
                System.out.println("Client accepted");
                new Thread(new HelloWorldServer(s)).start();
            }
        }
    }

}
package httpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.stream.Collectors;

import static httpServer.SocketProcessor.RESPONSE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpServerTest {

    public static final int PORT = 8080;
    public static final String REQUEST = "GET / HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Pragma: no-cache\r\n" +
            "Cache-Control: no-cache\r\n" +
            "Upgrade-Insecure-Requests: 1\r\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 YaBrowser/17.6.1.745 Yowser/2.5 Safari/537.36\r\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
            "DNT: 1\r\n" +
            "Accept-Encoding: gzip, deflate, sdch, br\r\n" +
            "Accept-Language: ru,en;q=0.8\r\n" +
            "X-Compress: null\r\n\r\n";

    static Thread serverThread;


    class ServerThread extends Thread{
        public void run(){
            try{
                (new HttpServer()).main(String.valueOf(PORT));
            }
            catch (Throwable e){}
        }
    }

    @Before
    public void before() throws Throwable {
        serverThread = new ServerThread(); //Thread(() -> HttpServer.main(String.valueOf(PORT)), "server");
        serverThread.start();
    }
    @After
    public void after() throws Throwable {
        serverThread.interrupt();
    }



    @Test
    public void ping() throws Throwable {

        try (Socket socket = new Socket("localhost", PORT);
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             socket.getInputStream()))) {

            outputStream.write(REQUEST.getBytes());
//            outputStream.close();

            String response = reader.lines()
//                    .filter(s -> s.trim().length() > 0)
                    .collect(Collectors.joining("\r\n"));

            String s = HelloWorldServer.HTML;
            assertThat(response, is(String.format(RESPONSE, s.length(), s)));

//            String line;
//            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
//                log.info(line);
//            }
        }

    }
}
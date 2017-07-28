package httpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public abstract class SocketProcessor implements Runnable {

    public static final String RESPONSE =
            "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    //"Content-Type: image/jpeg\r\n" +
                    "Content-Length: %d\r\n" +
                    "Connection: close\r\n\r\n%s";


    //abstract protected String mapRequest(HttpRequest httpRequest);
    //abstract protected void sendResponse(HttpRequest httpRequest, OutputStream os);
    abstract protected void sendResponse(HttpRequest httpRequest, OutputStream os);
    abstract protected void sendResponse2(HttpRequest httpRequest, OutputStream os);
    abstract protected void sendResponse3(HttpRequest httpRequest, OutputStream os);

    private Socket s;
    private InputStream is;
    private OutputStream os;

    SocketProcessor(Socket s) throws Throwable {
        this.s = s;
        is = s.getInputStream();
        os = s.getOutputStream();
    }

    public void run() {

        try {
            HttpRequest httpRequest = getHttpRequest();
            System.out.println(httpRequest.getPath());

            if(httpRequest.getPath().equals("/test2.html/pic1.jpg"))
                sendResponse2(httpRequest, os);
            if(httpRequest.getPath().equals("/test2.html/"))
                sendResponse3(httpRequest, os);

            //System.out.println(mapRequest(getHttpRequest()));
            //writeResponse(mapRequest(getHttpRequest()));
            //sendResponse(getHttpRequest(), os);
            //sendResponse2(getHttpRequest(), os);
            //sendResponse3(getHttpRequest(), os);
        } catch (Throwable t) {
                /*do nothing*/
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
                    /*do nothing*/
            }
        }
        System.out.println("Client processing finished");
    }

/*
    // посылаем ответ клиенту
    private void writeResponse(String s) throws Throwable {
        os.write(String.format(RESPONSE, s.length(), s).getBytes());
        //os.write(RESPONSE.getBytes());
        os.flush();
    }
*/

    private HttpRequest getHttpRequest() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = br.readLine().trim();

        // определяем метод http-запроса
        String[] contentAndTail = s.split("\\s", 2);
        HttpMethod httpMethod = HttpMethod.valueOf(contentAndTail[0]);
        System.out.println("httpMethod = " + httpMethod);

        // определяем путь http-запроса
        contentAndTail = contentAndTail[1].split("[?\\s]", 2);
        String path = contentAndTail[0];
        System.out.println("path = " + path);

        // определяем параметры http-запроса
        Map<String, String> params = contentAndTail[1].startsWith("HTTP") ?
                Collections.emptyMap() :
                getParams(contentAndTail[1].split("\\s", 2)[0]);
        System.out.println("params = " + params);

        // определяем headers http-запроса
        Map<String, String> headers = new HashMap<>();
        while ((s = br.readLine()) != null && !s.trim().isEmpty()) {
            String[] header = s.split(":\\s", 2);
            headers.put(header[0], header[1]);
        }
        System.out.println("headers = " + headers);

        // определяем body http-запроса
        StringBuilder body = new StringBuilder();
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT)
            while (br.ready() && (s = br.readLine()) != null)
                body.append(s);
        System.out.println("body = " + body);

        return HttpRequest.from(httpMethod, path, params, headers, body.toString());
    }

    // получаем map из строки вида param=value&param=value
    private Map<String, String> getParams(String s) {
        Map<String, String> params = new HashMap<>();
        for (String param : s.split("&")) {
            String[] split = param.split("=", 2);
            params.put(split[0], split[1]);
        }
        return params;
    }





    /*
    private Socket s;
    private InputStream is;
    private OutputStream os;

    public SocketProcessor(Socket s) throws Throwable {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }


    public void run() {
        try {
            this.readInputHeaders();
            this.writeResponse("<html><body><h1>Hello from Habrahabr</h1></body></html>");
        } catch (Throwable t) {
                //do nothing
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
                    //do nothing
            }
        }
        System.out.println("Client processing finished");
    }

    // передаем ответ клиенту
    private void writeResponse(String s) throws Throwable {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + s.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + s;
        os.write(result.getBytes());
        os.flush();
    }

    // читаем текст пришедшего запроса
    private void readInputHeaders() throws Throwable {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while(true) {
            String s = br.readLine();
            System.out.println(s);
            if(s == null || s.trim().length() == 0) {
                break;
            }
        }
    }
    */
}
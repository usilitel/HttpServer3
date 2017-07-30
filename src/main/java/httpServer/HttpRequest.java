package httpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// класс для работы с пришедшим HTTP-запросом
public class HttpRequest {

    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> params;
    private Map<String, String> headers;
    private String body;


    HttpRequest(InputStream is) throws Throwable {
        // определяем метод http-запроса
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = br.readLine().trim();
        String[] contentAndTail = s.split("\\s", 2);
        this.httpMethod = HttpMethod.valueOf(contentAndTail[0]);
        //System.out.println("httpMethod = " + httpMethod);

        // определяем путь http-запроса
        contentAndTail = contentAndTail[1].split("[?\\s]", 2);
        this.path = contentAndTail[0];
        if (path.equals("/"))
            path = "/index.html";
        if (path.endsWith("/"))
            path = path.substring(0,path.length()-1);
        //System.out.println("path = " + path);

        // определяем параметры http-запроса
        this.params = contentAndTail[1].startsWith("HTTP") ?
                Collections.emptyMap() :
                getParams(contentAndTail[1].split("\\s", 2)[0]);
        //System.out.println("params = " + params);

        // определяем headers http-запроса
        this.headers = new HashMap<>();
        while ((s = br.readLine()) != null && !s.trim().isEmpty()) {
            String[] header = s.split(":\\s", 2);
            headers.put(header[0], header[1]);
        }
        //System.out.println("headers = " + headers);

        // определяем body http-запроса
        StringBuilder body = new StringBuilder();
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT)
            while (br.ready() && (s = br.readLine()) != null)
                body.append(s);
        this.body = body.toString();
        //System.out.println("body = " + body);

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


    public HttpMethod getMethod() {
        return httpMethod;
    }


    public String getPath() {
        return path;
    }


    public Map<String, String> getParams() {
        return params;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }


    public String getBody() {
        return body;
    }


    public String getFileExtension(){
        String path = this.getPath();
        return path.substring(path.lastIndexOf(".")+1);
    }

    public String getFileName(){
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        if (fileName.equals("")) fileName="index.html";
        return fileName;
    }

}

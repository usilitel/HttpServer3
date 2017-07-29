package httpServer;

import java.util.Map;

public interface HttpRequest {

    HttpMethod getMethod();

    String getPath();

    Map<String, String> getParams();

    default String getHostAndPort() {
        return getHeaders().get("Host");
    }

    Map<String, String> getHeaders();

    String getBody();

    String getFileExtension();

    String getFileName();

    static HttpRequest from(HttpMethod httpMethod,
                            String path,
                            Map<String, String> params,
                            Map<String, String> headers,
                            String body) {

        return new HttpRequest() {
            @Override
            public HttpMethod getMethod() {
                return httpMethod;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }

            @Override
            public String getBody() {
                return body;
            }

            @Override
            public String getFileExtension(){
                String path = this.getPath(); //"test2.html/";
                String extension = path.substring(path.lastIndexOf(".")+1);
                return extension;
            }

            @Override
            public String getFileName(){
                String path = this.getPath();
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                return fileName;
            }

        };
    }
}

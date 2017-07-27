package httpServer;

import java.io.*;
import java.net.Socket;

public class HelloWorldServer extends SocketProcessor {

    public static final String HTML =
            //"<html><head><meta charset=\"utf-8\"/></head><body><h1>Привет от Habrahabr`а!..</h1></body></html>";
            "<html><head><meta charset=\"utf-8\"/></head><body><h1>Hello from Habrahabr</h1></body></html>";
            //"<html><head><meta charset=\"utf-8\"/></head><body><h1>абв от fds</h1></body></html>";

    HelloWorldServer(Socket s) throws Throwable {
        super(s);
    }


    // посылаем ответ клиенту через OutputStream
    @Override
    protected void sendResponse(HttpRequest httpRequest, OutputStream os) {
        //String inputFileName = "C:\\projects\\HttpServer3\\src\\main\\resources\\static\\test2.html";
        //String inputFileName = "src\\main\\resources\\static\\test2.html";
        String inputFileName = "src/main/resources/static" + httpRequest.getPath();
        //System.out.println(inputFileName + "[inputFileName]");
        //System.out.println(httpRequest.getPath() + "[httpRequest.getPath()]");


        char[] c = new char[10];
        String s;

        StringBuilder response = new StringBuilder();

        try{
            Reader fileReader = new InputStreamReader(new FileInputStream(inputFileName),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((s = bufferedReader.readLine()) != null) {
                response.append(s);
            }

            s=response.toString();
            os.write(String.format(RESPONSE, s.length(), s).getBytes());
            os.flush();

/*
            while (fileReader.read(c)>0){
                response.append(c);
                c = new char[15];
            }

            String s = response.toString();
            os.write(String.format(RESPONSE, s.length(), s).getBytes());
            os.flush();
*/

//            System.out.println(s + "[flush]");
//            fileReader.read(c);
//            String s = String.copyValueOf(c);
//            os.write(String.format(RESPONSE, s.length(), s).getBytes());
//            os.flush();
        }
        catch (IOException e){}

    }


/*
    @Override
    protected String mapRequest(HttpRequest httpRequest) {
        //return HTML;

        String inputFileName = "C:\\projects\\HttpServer3\\src\\main\\resources\\static\\test2.html";
        //Reader inFile = null;
        char[] c = new char[10];

        try{
            Reader fileReader = new InputStreamReader(new FileInputStream(inputFileName),"UTF-8");
            fileReader.read(c);
        }
        catch (IOException e){}

        String s = String.copyValueOf(c);


        //return HTML;
        return s;
    }
    */
}

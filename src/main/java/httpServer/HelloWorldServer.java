package httpServer;

import java.io.*;
import java.net.Socket;

// класс посылает ответы на HTTP-запросы
public class HelloWorldServer implements Runnable {

    private Socket s;
    private InputStream is;
    private OutputStream os;

    // при соединении получаем InputStream и OutputStream
    HelloWorldServer(Socket s) throws Throwable {
        this.s = s;
        is = s.getInputStream();
        os = s.getOutputStream();
    }


    public void run() {
        try {
            // обрабатываем запрос
            HttpRequest httpRequest =  new HttpRequest(is); //
            System.out.println("Request path: " + httpRequest.getPath());
            //System.out.println("Request params: " + httpRequest.getParams());

            // посылаем ответ
            sendResponse(httpRequest, os);

        } catch (Throwable t) {
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
            }
        }
        System.out.println("Client processing finished");
    }


    // посылаем ответ клиенту через OutputStream
    protected void sendResponse(HttpRequest httpRequest, OutputStream os) {

        String fileExtension = httpRequest.getFileExtension(); // получили расширение запрошенного файла
        String inputFileName = HttpServer.ExtensionPaths.getValue(fileExtension) + httpRequest.getFileName(); // получили путь к файлу, который нужно отправить клиенту

        String responseHeader =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + HttpServer.MimeTypes.getValue(fileExtension) + "\r\n" +
                        "Content-Length: %d\r\n" +
                        "Connection: close\r\n\r\n";

        // получаем количество частей, на которые нужно разбить ответ
        int cntThreads = 1;
        try{
            cntThreads = Integer.valueOf(httpRequest.getParams().get("cntThreads"));
        }
        catch (Exception e){}
        System.out.println("cntThreads = " + cntThreads);


        // читаем файл и посылаем его клиенту
        try(FileInputStream fin=new FileInputStream(inputFileName))
        {
            // вычисляем размер буфера
            int bufferSize = fin.available()/cntThreads + ((fin.available()%cntThreads==0) ? 0 : 1); // если ответный файл не делится нацело - добавляем к буферу 1 байт
            byte[] buffer = new byte[bufferSize];
            //System.out.println("bufferSize = " + bufferSize);

            os.write(String.format(responseHeader, fin.available()).getBytes()); // отправили заголовок ответа

            while (fin.read(buffer, 0, bufferSize) > 0){ // отправляем тело ответа (разбиваем его на части)
                os.write(buffer);
                os.flush();
                buffer = new byte[bufferSize]; // очищаем массив
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

}

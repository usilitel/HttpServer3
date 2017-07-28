package httpServer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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


        //char[] c = new char[10];
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





    // посылаем ответ клиенту через OutputStream (посылаем картинку)
    @Override
    protected void sendResponse2(HttpRequest httpRequest, OutputStream os) {

        String inputFileName = "C:\\projects\\HttpServer3\\src\\main\\resources\\static\\pic1.jpg";

        ArrayList<Byte> bytes = new ArrayList<Byte>();

        String s;
        StringBuilder sb = new StringBuilder();

        try(FileInputStream fin=new FileInputStream(inputFileName))
        {
            byte[] buffer = new byte[fin.available()];
            // считаем файл в буфер
            fin.read(buffer, 0, fin.available());
            System.out.println("Размер файла: " + fin.available() + " байт(а)");
            System.out.println("Содержимое файла:");
            for(int i=0; i<buffer.length;i++){
                System.out.print((char)buffer[i]);
            }

            os.write(String.format(RESPONSE, buffer.length, "").getBytes());
            os.write(buffer);
            os.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

/*





            int i=-1;
            while((i=fin.read())!=-1){
                //System.out.print((char)i);
                //System.out.print(i);
                //sb.append((char)i);
                bytes.add();
            }

            s = sb.toString();
            os.write(String.format(RESPONSE, s.length(), s).getBytes());
            os.flush();
            */






        /*
        //char[] c = new char[10];
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

        }
        catch (IOException e){}

*/



/*
        try {
            try {
                FileInputStream fis = new FileInputStream(inputFileName);
                byte[] data = new byte[64 * 1024];
                for (int read; (read = fis.read(data)) > -1; ){
                    os.write(data, 0, read);
                    System.out.println("[read]" + read);
                }

                os.flush();
            } catch (FileNotFoundException e) {
            }
        }
        catch (IOException e){
        }
        */





    // посылаем ответ клиенту через OutputStream (посылаем ответ частями)
    @Override
    protected void sendResponse3(HttpRequest httpRequest, OutputStream os) {
        String inputFileName = "C:\\projects\\HttpServer3\\src\\main\\resources\\static\\test2.html";

        String RESPONSE1 =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        //"Content-Type: image/jpeg\r\n" +
                        "Content-Length: %d\r\n" +
                        "Connection: close\r\n\r\n";

        ArrayList<Byte> bytes = new ArrayList<Byte>();

        String s;
        StringBuilder sb = new StringBuilder();

        try(FileInputStream fin=new FileInputStream(inputFileName))
        {
            byte[] buffer = new byte[fin.available()];
            // считаем файл в буфер

//            System.out.println("Размер файла: " + fin.available() + " байт(а)");
//            System.out.println("Содержимое файла:");
            fin.read(buffer, 0, fin.available());
//            for(int i=0; i<buffer.length;i++){
//                System.out.print((char)buffer[i]);
//            }

            byte[] buffer1 = new byte[100];
            byte[] buffer2 = new byte[81];
            System.arraycopy(buffer, 0, buffer1, 0, 100);
            System.arraycopy(buffer, 100, buffer2, 0, 81);

            os.write(String.format(RESPONSE1, buffer.length).getBytes());
            os.write(buffer1);
            os.flush();

            os.write(buffer2);
            os.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
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

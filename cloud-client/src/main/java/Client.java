import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;

        public Client(String host, int port, String file){
        try{
            socket = new Socket(host, port);
            sendFile(file);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendFile(String file) throws IOException {
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }
        fis.close();
        dos.close();
    }


    public static void main(String[] args) throws IOException {
        Book book = new Book("The Jungle Book", "Joseph Rudyard Kipling", 1893);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("book.ser"));
        oos.writeObject(book);
        oos.close();
        String filePath = "book.ser";
        new Client("localhost", 8888, filePath);
    }


}

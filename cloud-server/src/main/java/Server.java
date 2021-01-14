import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private ServerSocket ss;

    public Server(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                try {
                    saveFile(clientSock);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException, ClassNotFoundException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream("book2.ser");
        byte[] buffer = new byte[1024];
        int read = 0;
        int totalRead = 0;
        while((read = dis.read(buffer)) != -1) {
            totalRead += read;
            fos.write(buffer, 0, read);
        }
        System.out.println("Read " + totalRead + " bytes.");

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book2.ser"));
        Book book2 = (Book) ois.readObject();
        ois.close();
        book2.info();
        fos.close();
        dis.close();
    }

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }
}

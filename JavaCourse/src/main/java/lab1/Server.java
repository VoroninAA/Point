package lab1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server
{
    public static void main(String[] args) throws Exception
{
    Point point = new Point(5.0, 5.0, 5.0);
    ServerSocket ss = new ServerSocket(5056);
    Thread time_thread = new Thread(new Server().new timeGoing(point));
    time_thread.start();
    while (true) {
        Socket s = null;

        try {
            // socket object to receive incoming client requests
            s = ss.accept();

            System.out.println("A new client is connected : " + s);

            // obtaining input and out streams
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Assigning new thread for this client");

            // create a new thread object
            Thread t = new ClientHandler(s, dis, dos, point, time_thread);

            // Invoking the start() method
            t.start();

        } catch (Exception e) {
            assert s != null;
            s.close();
            e.printStackTrace();
        } finally {
        }
    }

}

    class timeGoing implements Runnable {
        Point point;
        public timeGoing(Point point) {
            this.point = point;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


class ClientHandler extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    Point point;
    Thread time_thread;
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Point point, Thread time_thread)
    {   this.point = point;
        this.s = s;
        this.time_thread=time_thread;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            while (true) {
                out.write(  point.x + " " + point.y + " " + point.z  + "\n");
                out.flush();
                System.out.println( point.x + " " + point.y + " " + point.z  + "\n");
                String word = in.readLine();
                System.out.println(word);
                if (word != null && !word.isEmpty()) {

                        try {
                            double x = Double.parseDouble(word.substring(0, word.indexOf(" ")));
                            double y = Double.parseDouble(word.substring(word.indexOf(" "), word.indexOf(" ", (word.indexOf(" ")+ 1))));
                            double z = Double.parseDouble(word.substring(word.lastIndexOf(" ")));
                        point.setX(Double.valueOf(x));
                        point.setY(Double.valueOf(y));
                        point.setZ(Double.valueOf(z));

                        System.out.println(word);
                    } catch (InputMismatchException e) {
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
                assert out != null;
                out.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
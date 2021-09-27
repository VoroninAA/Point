package lab1;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class Client
{
    private static boolean alarm = false;
    private static String time = "10";
    private static String alarmLabelText = "";
    private static Thread t;
    static JFrame jFrame;
    static JLabel jLabel;
    static JPanel jPanel;
    static JTextField jtextField;
    static JLabel jTextFieldLabel;
    static JTextField jtextFieldRemove;
    static JLabel alarmLabel;
    static boolean time_flag = false;
    static BufferedWriter out = null;
    static BufferedReader in = null;
    static Socket s = null;
    public static void main(String[] args) throws IOException {
        try {
            InetAddress ip = InetAddress.getByName("localhost");

            s = new Socket(ip, 5056);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            t = new Thread(Client::init);
            t.start();

        int counter = 0;
            while (true) {
                out.write("");
                out.flush();
                counter++;
                String word = in.readLine();
                System.out.println(word);
                    if (word != null) {
                            double x = Double.parseDouble(word.substring(0, word.indexOf(" ")));
                            double y = Double.parseDouble(word.substring(word.indexOf(" "), word.indexOf(" ", (word.indexOf(" ")+ 1))));
                            double z = Double.parseDouble(word.substring(word.lastIndexOf(" ")));
                            System.out.println(x);
                            System.out.println(y);
                            System.out.println(z);
                            alarmLabelText =  "Current point " +  x +" " + y + " " + z;

                    }


                Thread.sleep(100);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            s.close();
            in.close();
            out.close();
            t.stop();
        }

    }


    static void init(){

        jFrame = new JFrame("Point");
        jPanel = new JPanel();
        jLabel = new JLabel("");
        alarmLabel = new JLabel("Current point 5.0 5.0 5.0");
        jLabel.setBounds(50,100,200,30);
        jPanel.add(jLabel, BorderLayout.NORTH);
        jtextField = new JTextField(20);
        jTextFieldLabel = new JLabel("Set point here");
        jPanel.add(jTextFieldLabel, BorderLayout.WEST);
        jPanel.add(jtextField, BorderLayout.CENTER);
        jPanel.add(alarmLabel);


        JButton button2 = new JButton("Set point");
        button2.setBounds(50,100,200,30);
        button2.addActionListener(e -> {
            try {
                setPoint();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        jPanel.add(button2);

        jFrame.add(jPanel);
        jFrame.setSize(300,300);

        jFrame.setVisible(true);
        start();
    }
    public static void start(){

        t = new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    out.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alarmLabel.setText(alarmLabelText);
            }
        });
        t.start();
    }

    public static void setPoint() throws IOException {
        out.write(jtextField.getText()  +"\n");
        out.flush();
        alarmLabel.setText(alarmLabelText);
    }
}




import processing.core.PApplet;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends PApplet implements Runnable {

    private static ServerSocket ss;

    private static int width;

    private static Object lock;
    private static Map<Integer, Rectangle> clientObj;

    public GameServer() {
        lock = new Object();
        clientObj = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            while(true) {
                Socket s = ss.accept();

                ObjectInputStream input_stream;
                ObjectOutputStream output_stream;
                synchronized(this) {
                    output_stream = new ObjectOutputStream(s.getOutputStream());
                    input_stream = new ObjectInputStream(s.getInputStream());
                }

                WriteToClient writeToClient = new WriteToClient(output_stream, lock);
                (new Thread(writeToClient)).start();
                (new Thread((new ReadFromClient(input_stream)))).start();
            }
        }
        catch(IOException iox) {
            iox.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PApplet.main("GameServer", args);

        try {
            ss = new ServerSocket(5200);

            GameServer gameServer = new GameServer();
            (new Thread(gameServer)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settings() {
        width = 1000;
        int height = 800;
        size(width, height);
    }

    public void draw() {
        background(0);
        text("Server", width-150, 20);
        drawClients();
    }

    private void drawClients() {
        for(Map.Entry<Integer, Rectangle> entry: clientObj.entrySet()) {
            Rectangle r = entry.getValue();
            rect((int) r.getX(), (int) r.getY(),100,100);
        }
    }

    static ArrayList<Rectangle> getAllClientObj() {
        return new ArrayList<>(clientObj.values());
    }

    static void setClientObj(int id, Object object) {
        synchronized (lock) {
            clientObj.put(id, (Rectangle) object);
            lock.notifyAll();
        }
    }
}

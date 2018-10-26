import processing.core.PApplet;

import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends PApplet {
    private static boolean isGameStart;

    private static int id;
    private static int height;
    private static int width;
    private static int xPos;
    private static int yPos;
    private static int speed;

    private static ArrayList<Rectangle> clientObj;

    private static WriteToServer writeToServer;
    private static ReadFromServer readFromServer;

    public static void main(String[] args) {
        id = Integer.parseInt(args[0]);
        PApplet.main("Client", args);

        isGameStart = false;
        clientObj = new ArrayList<>();

        try {
            Socket s = new Socket("127.0.0.1", 5200);
            ObjectOutputStream output_stream = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream input_stream = new ObjectInputStream(s.getInputStream());

            writeToServer = new WriteToServer(id, output_stream);
            readFromServer = new ReadFromServer(input_stream);
            (new Thread(writeToServer)).start();
            (new Thread(readFromServer)).start();
        } catch (Exception e) {}
    }

    public void settings() {
        width = 1000;
        height = 800;
        speed = 10;
        size(width, height);
    }

    public void setup() {
        xPos = width/2 + 50;
        yPos = height/2 + 50;
    }

    public void draw() {
        background(0);

        text("Client-" + id, width-150, 20);
        text("Use arrow keys to move", width - 150, height - 150);
        if(!isGameStart) text("Press any button to start game!", xPos-50, yPos-50);

        drawClients();
    }

    public void drawClients() {
        for(Rectangle r: clientObj) {
            rect((int) r.getX(), (int) r.getY(),100,100);
        }
    }

    public void keyPressed() {
        final int k = keyCode;
        isGameStart = true;

        if((k == LEFT  | k == 'A') && xPos > 0)  xPos -= speed;
        else if((k == RIGHT | k == 'D') && xPos < width - 100) xPos += speed;
        else if(k == UP || k == 'W') yPos -= speed;
        else if(k == DOWN || k == 'S') yPos += speed;

        synchronized (writeToServer) {
            writeToServer.notify();
        }
    }

    static Object getGameObject() {
        return new Rectangle(xPos, yPos, 100, 100);
    }

    static void setGameObjects(Object object) {
        clientObj = (ArrayList<Rectangle>) object;
    }
}

import java.io.ObjectInputStream;

public class ReadFromServer implements Runnable {

    private ObjectInputStream input_stream;

    ReadFromServer(ObjectInputStream input_stream) {
        this.input_stream = input_stream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Client.setGameObjects(input_stream.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

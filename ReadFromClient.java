import java.io.ObjectInputStream;

public class ReadFromClient implements Runnable {

    private ObjectInputStream input_stream;

    ReadFromClient(ObjectInputStream input_stream) {
        this.input_stream = input_stream;
    }

    @Override
    public void run() {
        try {
            while (true)
                GameServer.setClientObj(input_stream.readInt(), input_stream.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

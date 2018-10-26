import java.io.ObjectOutputStream;

public class WriteToServer implements Runnable {

    private int id;
    private ObjectOutputStream output_stream;

    WriteToServer(int id, ObjectOutputStream output_stream) {
        this.id = id;
        this.output_stream = output_stream;
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    wait();
                    output_stream.writeInt(id);
                    output_stream.writeObject(Client.getGameObject());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.io.ObjectOutputStream;

public class WriteToClient implements Runnable {

    private Object lock;
    private ObjectOutputStream output_stream;

    WriteToClient(ObjectOutputStream output_stream, Object lock) {
        this.output_stream = output_stream;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (lock) {
                    lock.wait();
                    output_stream.writeObject(GameServer.getAllClientObj());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class UnOnlineFileThread implements Runnable{
    private String getterId;
    private Vector messageVector2 ;

    public UnOnlineFileThread(String getterId, Vector messageVector2) {
        this.getterId = getterId;
        this.messageVector2 = messageVector2;
    }

    @Override
    public void run() {
        while (true){
        if (ServerThreads.get(getterId) != null){
            for (int i = 0; i < messageVector2.size(); i++) {
                try {
                    ObjectOutputStream objectOutputStream =
                            new ObjectOutputStream(ServerThreads.get(getterId).getSocket().getOutputStream());
                    objectOutputStream.writeObject(messageVector2.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }break;
        }

    }
    }
}

package Server;


import common.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

public class UnOnlineMessageThread implements Runnable{
    private String getterId ;
    private Vector<Message> messageVector ;


    public UnOnlineMessageThread(String getterId, Vector<Message> messageVector) {
        this.getterId = getterId;
        this.messageVector = messageVector;
    }

    @Override
    public void run() {
        System.out.println("等待用户上线");
        while(true){
            if (ServerThreads.get(getterId) != null){
                try {

                    for (int i = 0; i < messageVector.size(); i++) {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerThreads.get(getterId).getSocket().getOutputStream());
                        objectOutputStream.writeObject(messageVector.get(i));
                        System.out.println("离线消息发送" + i);

                    }

                    messageVector.clear();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}


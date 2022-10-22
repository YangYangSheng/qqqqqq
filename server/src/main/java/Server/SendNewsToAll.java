package Server;

import common.Message;
import common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class SendNewsToAll implements Runnable{
    private Scanner myScanner = new Scanner(System.in);
    @Override
    public void run() {
        while (true){
            System.out.println("输入你要推送的新闻");
            String next = myScanner.next();
            Message message = new Message();
            message.setMessType(MessageType.MESSAGE_CON_TO_ALL);
            message.setSender("服务器");
            message.setContent(next);
            HashMap hashMap = ServerThreads.getHashMap();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String string = iterator.next().toString();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerThreads.get(string).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }
    }
}

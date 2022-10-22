package Server;

import common.Message;
import common.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class ServerThread extends Thread{
    private Socket socket;
    private String userId;
    private Vector<Message> messageVector = new Vector();
    private Vector<Message> messageVector2 = new Vector();

    public Vector<Message> getMessageVector() {
        return messageVector;
    }
    public ServerThread(){}

    public ServerThread(String userId , Socket socket){
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                System.out.println("用户" + userId+"线程启动");
                if(message.getMessType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.println( "用户" + userId + "请求在线用户列表");
                    Message message1 = new Message();
                    message1.setContent(ServerThreads.getOnlineList());
                    message1.setGetter(userId);
                    message1.setMessType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ServerThreads.get(userId).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message1);
                }else if (message.getMessType().equals(MessageType.MESSAGE_CON)){
                    if (ServerThreads.get(message.getGetter()) == null){
                        messageVector.add(message);
                        if (messageVector.size() == 1 ){
                            new Thread(new UnOnlineMessageThread(message.getGetter(),messageVector)).start();
                        }
                    }else {
                    ServerThread serverThread = ServerThreads.get(message.getGetter());
                    OutputStream outputStream = serverThread.getSocket().getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                        objectOutputStream.writeObject(message);
                    }
                }
                else if(message.getMessType().equals(MessageType.MESSAGE_EXIT_LOGN)){
                    ServerThreads.removeThread(message.getSender());
                    System.out.println("线程"+ message.getSender()+"移除");
                    socket.close();
                    break;
                }
                else if (message.getMessType().equals(MessageType.MESSAGE_FILE_TO_ONE)){
                    if (ServerThreads.get(message.getGetter()) == null){
                        messageVector2.add(message);
                        if (messageVector2.size() == 1){
                            new Thread (new UnOnlineFileThread(message.getGetter(),messageVector2)).start();
                        }
                    }else {
                    ServerThread serverThread = ServerThreads.get(message.getGetter());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverThread.getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    }
                }
                else if (message.getMessType().equals(MessageType.MESSAGE_CON_TO_ALL)){
                    HashMap hashMap = ServerThreads.getHashMap();
                    Iterator iterator = hashMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        String  next =  (String) iterator.next();
                        if (next != message.getSender()){
                            ServerThread serverThread = ServerThreads.get(next);
                            Socket socket = serverThread.getSocket();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(message);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

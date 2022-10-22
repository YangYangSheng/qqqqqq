package UserClientService;

import common.Message;
import common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientServerThread extends Thread{
    private Socket socket ;
    public ClientServerThread(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while(true){
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) objectInputStream.readObject();
            if(message.getMessType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                String content = message.getContent();
                String[] s = content.split(" ");
                System.out.println("=======在线用户如下=======");
                for (int i = 0; i < s.length; i++) {
                    System.out.println("用户： " + s[i]);
                }
            }else if(message.getMessType().equals(MessageType.MESSAGE_CON)){
                System.out.println(message.getSender() + "对" + message.getGetter() + "说" + message.getContent());
            }else if (message.getMessType().equals(MessageType.MESSAGE_CON_TO_ALL)){
                System.out.println(message.getSender() + "对大家说" + message.getContent());
            }else if (message.getMessType().equals(MessageType.MESSAGE_FILE_TO_ONE)){
                FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                System.out.println("文件接受成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
}

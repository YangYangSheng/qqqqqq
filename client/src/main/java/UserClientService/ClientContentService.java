package UserClientService;

import common.Message;
import common.MessageType;
import common.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class ClientContentService {
    boolean b = false;
    private User u = new User();

    //发送注册的User给客户端
    public void addUser(User user) {
        Socket socket = null;
        Message message = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(user);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            message = (Message) objectInputStream.readObject();
            System.out.println(message.getMessType());
            if (message.getMessType().equals( MessageType.USER_IS_EXIST)) {
                System.out.println("用户名已存在，请重新注册");

            } else if (message.getMessType().equals(MessageType.ADD_USER_SUCCEED) ) {
                System.out.println("注册成功，3s后为您自动登录");
                Thread.sleep(3000);
                user.setStatus(0);
                check(user.getUserId(), user.getPasswd());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean check(String userId, String passwd) {
        u.setUserId(userId);
        u.setPasswd(passwd);
        Socket socket = null;
        Message message = null;

        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(u);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            message = (Message) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (message.getMessType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
            ClientServerThread clientServerThread = new ClientServerThread(socket);
            clientServerThread.start();
            MageClientServerThread.add(userId, clientServerThread);
            b = true;
        } else {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public void getOnlineFriendList() {
        Message message = new Message();
        message.setMessType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        ClientServerThread clientServerThread = MageClientServerThread.get(u.getUserId());
        Socket socket = clientServerThread.getSocket();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToAll(String senderId, String connect) {
        Message message = new Message();
        message.setContent(connect);
        message.setSender(senderId);
        message.setMessType(MessageType.MESSAGE_CON_TO_ALL);
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(MageClientServerThread.get(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFileToOne(String sendId, String getter, String src, String dest) {
        Message message = new Message();
        message.setMessType(MessageType.MESSAGE_FILE_TO_ONE);
        message.setSender(sendId);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDest(dest);
        byte[] bytes = new byte[(int) new File(src).length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);
            fileInputStream.close();
            message.setBytes(bytes);
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(MageClientServerThread.get(sendId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void sendMessageToOne(String senderId, String getterId, String connect) {
        Message message = new Message();
        message.setMessType(MessageType.MESSAGE_CON);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(connect);
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(MageClientServerThread.get(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void exitLogn() {
        Message message = new Message();
        message.setSender(u.getUserId());
        message.setMessType(MessageType.MESSAGE_EXIT_LOGN);
        ClientServerThread clientServerThread = MageClientServerThread.get(u.getUserId());
        Socket socket = clientServerThread.getSocket();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

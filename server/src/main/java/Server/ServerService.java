package Server;



import common.Message;
import common.MessageType;
import common.User;
import mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerService {
    private ServerSocket serverSocket ;
    private static UserMapper mapper;
    private static SqlSession sqlSession;
    {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
         sqlSession = sqlSessionFactory.openSession();
        mapper = sqlSession.getMapper(UserMapper.class);

}

    private static boolean checkUser(User user) throws IOException {
        User user1 = mapper.selectByNamePwd(user);
        System.out.println(user1);
        if (user1 != null){
            return true;
        }else
            {return false;}

    }

    public ServerService() throws Exception{
        try {
            serverSocket =new ServerSocket(9999);
            System.out.println("服务器9999端口正在监听");
            new Thread(new SendNewsToAll()).start();
            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                User user = (User) objectInputStream.readObject();
               if(user.getStatus() == 0){
                   if(checkUser(user)){
                       System.out.println("用户" + user.getUserId() + "连接服务器成功");
                       Message message = new Message();
                       message.setMessType(MessageType.MESSAGE_LOGIN_SUCCEED);
                       ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                       objectOutputStream.writeObject(message);
                       ServerThread serverThread = new ServerThread(user.getUserId(), socket);
                       serverThread.start();
                       System.out.println(user.getUserId() + "线程启动");
                       ServerThreads.add(user.getUserId(), serverThread);


                   }else {
                       ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                       Message message = new Message();
                       message.setMessType(MessageType.MESSAGE_LOGIN_FAIL);
                       objectOutputStream.writeObject(message);
                       socket.close();
                       System.out.println("账号密码输入有误");
                   }
               }else if(user.getStatus() == 1){
                   User user1 = mapper.selectByuseName(user);
                   if(user1 != null){
                       ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                       Message message = new Message();
                       message.setMessType(MessageType.USER_IS_EXIST);
                       objectOutputStream.writeObject(message);
                       socket.close();
                       System.out.println("用户已存在");
                   }else {
                       ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                       Message message = new Message();
                       message.setMessType(MessageType.ADD_USER_SUCCEED);
                       objectOutputStream.writeObject(message);
                       socket.close();
                       mapper.insertUser(user);
                       sqlSession.commit();
                       User user2 = mapper.selectByuseName(user);
                       if(user2 != null){
                           System.out.println("用户注册成功");
                       }else {System.out.println("注册失败");}

                   }
               }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }
}

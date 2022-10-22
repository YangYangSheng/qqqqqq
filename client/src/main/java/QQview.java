import Untils.Until;
import UserClientService.ClientContentService;
import common.User;

public class QQview {
    private boolean loop = true ;
    private ClientContentService clientContentService = new ClientContentService();
    public static void main(String[] args) {
        new QQview().mainMun();

    }
    public void mainMun(){
        while (loop){
        System.out.println("===========欢迎使用qq=========");
            System.out.println("1.登录账号 ");
            System.out.println("9.退出程序 ");
            System.out.println("0.注册用户");
        System.out.println("请选择要进入的程序");
        char string1 = Until.scaString(1).charAt(0);

        switch (string1) {
            case '0':
                System.out.println("请输入你的账号(最多10个字符)");
                String useId = Until.scaString(10);
                System.out.println("请输入你的密码(最多10个字符)");
                String pwd = Until.scaString(10);
                User user = new User();
                user.setUserId(useId);
                user.setPasswd(pwd);
                user.setStatus(1);
                clientContentService.addUser(user);
                break;
            case '1':
                System.out.println("=======进入二级菜单=======");
                System.out.println("请输入的你账号： ");
                String string = Until.scaString(10);
                System.out.println("请输入你的密码： ");
                String string2 = Until.scaString(8);

                    if (clientContentService.check(string, string2)) {
                        System.out.println("登录成功");
                        while (loop) {
                        System.out.println("欢迎用户" + string + "使用qq");
                        System.out.println("选择你要进入的功能");
                        System.out.println("1. 显示在线用户列表");
                        System.out.println("2. 群发消息");
                        System.out.println("3. 私聊信息");
                        System.out.println("4. 发送文件");
                        System.out.println("5. 修改密码");
                        System.out.println("9. 退出程序");
                            char string3 = Until.scaString(1).charAt(0);
                            switch (string3) {
                            case '1':
                               clientContentService.getOnlineFriendList();
                                break;
                            case '2':

                                System.out.println("请输入要对大家说的话");
                                String string4 = Until.scaString(50);
                                clientContentService.sendMessageToAll(string,string4);
                                break;
                            case '3':
                                System.out.println("请输入你要聊天的用户");
                                String getterId = Until.scaString(5);
                                System.out.println("请输入聊天内容");
                                String connect = Until.scaString(100);
                                System.out.println(string + "对" + getterId + "说" + connect);
                                clientContentService.sendMessageToOne(string,getterId,connect);
                                break;
                            case '4':
                                System.out.println("请输入你要发送文件的用户");
                                String string5 = Until.scaString(10);
                                System.out.println("输入要发送的文件");
                                String string6 = Until.scaString(100);
                                System.out.println("输入对方保存文件的位置");
                                String string7 = Until.scaString(100);
                                clientContentService.sendFileToOne(string,string5,string6,string7);
                                break;
                            case'5':

                            case '9':
                                clientContentService.exitLogn();
                                System.exit(0);
                        }}
                    } else {
                        System.out.println("登录失败，请检查账号密码是否错误");
                    }
                    break;


            case '9':

                loop = false;
    }
    }
    }
}


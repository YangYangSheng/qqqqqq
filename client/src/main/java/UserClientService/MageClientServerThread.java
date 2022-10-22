package UserClientService;

import java.util.HashMap;

public class MageClientServerThread {
    public static HashMap hm = new HashMap<String ,ClientServerThread>();



    public static void add(String userId ,ClientServerThread clientServerThread){
        hm.put(userId,clientServerThread);

    }
    public static ClientServerThread get(String userId){
        return (ClientServerThread)hm.get(userId);

    }
}

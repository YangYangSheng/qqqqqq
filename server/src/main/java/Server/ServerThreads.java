package Server;

import java.util.HashMap;
import java.util.Iterator;

public class ServerThreads {
    private static HashMap hashMap = new HashMap<String ,ServerThread>();
    public  static void add(String userId , ServerThread serverThread){
        hashMap.put(userId,serverThread);
    }
    public  static ServerThread get(String userId){
        return (ServerThread) hashMap.get(userId);
    }
    public static void removeThread(String userId){
        hashMap.remove(userId);
    }
    public static String getOnlineList(){
        Iterator iterator = hashMap.keySet().iterator();
        String onlineList = "";
        while (iterator.hasNext()) {

            onlineList +=  iterator.next() +" ";

        }
        return onlineList;

    }

    public static HashMap getHashMap() {
        return hashMap;
    }
}

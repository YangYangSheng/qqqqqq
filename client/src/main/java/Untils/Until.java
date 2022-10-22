package Untils;

import java.util.Scanner;

public class Until {
    public static String scaString(int limt){
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        if(next.length() <= limt && next.length() >0){
            return next;
        }else {
            System.out.println("字符限制在" + limt + "之内");
            return " ";
        }

    }



}

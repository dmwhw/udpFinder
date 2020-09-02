package com.haowen.simple;

public class Main {
    public static void main(String[] args) {
        if (args.length>0){
            if ("server".equals( args[0])){
                new Server().init();
                return ;
            }else if  ("client".equals( args[0])){
                new Client().init();
                return ;
            }
        }
         System.out.println("server or client args.");
    }
}
package com.haowen.simple;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Optional;

public class Server {

    private MulticastSocket sock;
    private InetAddress multicastInet;//多播组地址
    public static Integer serverPort = 6689;
    public static  Integer mUserDataMaxLen=64;
    private String MULTICAST_IP="239.255.255.2";//多播地址的范围 从 224.0.0.0到239.255.255.255

    public void init() {
        try {
            sock = new MulticastSocket(serverPort);
            //加入多播组
            multicastInet = InetAddress.getByName(MULTICAST_IP);
            sock.joinGroup(multicastInet);
            sock.setLoopbackMode(false);// 必须是false才能开启广播功能
            System.out.println("server started...");
            while (true){
                byte[] buf = new byte[mUserDataMaxLen];
                byte[] sendData = new byte[mUserDataMaxLen];
                DatagramPacket recePack = new DatagramPacket(buf, buf.length);
                sock.receive(recePack);//接收包。
                System.out.println("received...");
                byte[] data=recePack.getData();
                System.out.println( new String(Optional.ofNullable(data).orElse("null received from client".getBytes())));
               //从包中获取对方的ip端口。
                String ip = recePack.getAddress().getHostAddress();
                int port = recePack.getPort();
                //发送回应
                byte [] buff= ("from server:client?"+ip+port+"---"+System.currentTimeMillis()).getBytes();
                System.arraycopy(buff, 0 , sendData, 0, buff.length);
                DatagramPacket sendPack = new DatagramPacket(sendData, sendData.length,
                recePack.getAddress(), recePack.getPort());
                System.out.println("ip is "+ip+" port is "+port);
                sock.send(sendPack);
                System.out.println("send...");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().init();
    }
}
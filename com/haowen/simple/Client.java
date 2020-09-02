package com.haowen.simple;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Optional;
 
public class Client {
    private MulticastSocket sock;
    private InetAddress multicastInet;//多播组地址
    private Integer clientPort = 6690;
     private String MULTICAST_IP="239.255.255.2";//多播地址的范围 从 224.0.0.0到239.255.255.255
    private DatagramPacket mSendPack;
    private Thread sendThread ;
    private Thread receiveThread ;
    public void init() {
        try {
            sock = new MulticastSocket(clientPort);
            multicastInet = InetAddress.getByName(MULTICAST_IP);
            sock.joinGroup(multicastInet);
            sock.setLoopbackMode(false);// 必须是false才能开启广播功能
            sock.setTimeToLive(255);
            byte[] sendData = new byte[Server.mUserDataMaxLen];
            mSendPack = new DatagramPacket(sendData, sendData.length, multicastInet, Server.serverPort);
        } catch (Exception e) {
             e.printStackTrace();
        }
        sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                 send(sock);
            }
        });
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                 receive(sock);
            }
        });

        sendThread.start();
        receiveThread.start();
    }
    private void send(MulticastSocket sock) {
        if (sock == null || sock.isClosed()) {
            return;
        }

        while (true) {
            byte[] data = new byte[Server.mUserDataMaxLen];
            byte [] buff= "from client:[client dataPack]".getBytes();
            System.arraycopy(buff, 0 , data, 0, buff.length);
            mSendPack.setData(data);
            try {
                sock.send(mSendPack);
                
            } catch (IOException e) {
                 e.printStackTrace();
                break;
            }
            System.out.println("send......");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                 e.printStackTrace();
            }
         }
    }

    /**
     * 实现收到server返回设备信息，并解析数据
     *
     * @param sock
     */
    private void receive(MulticastSocket sock) {
        if (sock == null || sock.isClosed()) {
            return;
        }

        byte[] receData = new byte[Server.mUserDataMaxLen];
        DatagramPacket recePack = new DatagramPacket(receData, receData.length);

        while (true) {
            recePack.setData(receData);
            try {
                System.out.println(" client receivd......");

                sock.receive(recePack);
                if (recePack.getLength() > 0) {
                    String aa=new String( Optional.ofNullable(recePack.getData()).orElse("null server data".getBytes()));
                    System.out.println(" client receivd......"+aa);
                    String ip = recePack.getAddress().getHostAddress();
                    System.out.println("server ip is "+ip);
                }
                
            } catch (IOException e) {
                e.printStackTrace();
                 break;
            }
        }
    }


    public static void main(String[] args) {
        new Client().init();
    }

}
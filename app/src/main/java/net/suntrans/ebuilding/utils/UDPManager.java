package net.suntrans.ebuilding.utils;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * Created by Administrator on 2017/4/11.
 */

public class UDPManager {
    private final String TAG = "UDPManager";
    private Handler handler;
    int iTimeOut = 1000;
    byte[] buffer = new byte[1024];
    public DatagramSocket wifiSocket = null;
    private boolean isStop = false;
    private static final String ewm = "EWM";
    private static final String hx = "HX";

    public UDPManager(Handler handler) {
        this.handler = handler;

        try {
            wifiSocket = new DatagramSocket();
//            wifiSocket.setSoTimeout(iTimeOut);

        } catch (SocketException e) {
            e.printStackTrace();
        }
        new ReceiveThread().start();
    }

    public void send(String host, byte[] order, String type) {
        DatagramPacket dataPacket = null;
        try {

            dataPacket = new DatagramPacket(buffer, 0x400);
            dataPacket.setData(order);
            dataPacket.setLength(order.length);
            if (type .equals(ewm) )
                dataPacket.setPort(8089);
            else
                dataPacket.setPort(988);

            InetAddress broadcastAddr = InetAddress.getByName(host);
            dataPacket.setAddress(broadcastAddr);
            wifiSocket.send(dataPacket);
            LogUtil.i(TAG, "SEND:" + Converts.Bytes2HexString(order));
            return;
        } catch (Exception e) {
            if (wifiSocket != null) {
                wifiSocket.close();
                wifiSocket = null;
            }
            dataPacket = null;
            e.printStackTrace();
        }
    }

    class ReceiveThread extends Thread {
        @Override
        public void run() {
            LogUtil.i(TAG, "START RECEIVE");
            while (!isStop && wifiSocket != null) {

                byte[] buff = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                try {

                    wifiSocket.receive(packet);
                    LogUtil.i(TAG, "接收数据中...");
                    String ip = packet.getAddress().toString();
                    int port = packet.getPort();
                    int isotherStr = ip.indexOf("/");
                    if (isotherStr > -1) {
                        ip = ip.substring(isotherStr + 1);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("ip", ip);
                    jsonObject.put("port", port);
                    jsonObject.put("content", Converts.Bytes2HexString(packet.getData()));
                    Message message = new Message();
                    message.obj = jsonObject;
                    message.what = 2;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            }
        }
    }


    public void close() {
        isStop = true;
        if (wifiSocket != null)
            wifiSocket.close();
    }
}

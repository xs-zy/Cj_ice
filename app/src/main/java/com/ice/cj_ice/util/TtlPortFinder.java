package com.ice.cj_ice.util;

import android.util.Log;


import com.ice.cj_ice.protocol.ParamsSettingUtil;

import android_serialport_api.SerialPortFinder;
import android_serialport_api.SerialUtil;

/**
 * Created by Administrator on 2019/1/18.
 */

public class TtlPortFinder {
    private static SerialUtil serialUtil;
    private static TtlPortFinder ttlPortFinder = null;
    private static byte[] sendOrderUtil;
    private static String PORT = "/dev/ttyS4";
    private TtlPortFinder(){}
    public static TtlPortFinder getTtlPortFinder(){
                if(ttlPortFinder == null){
                    ttlPortFinder = new TtlPortFinder();
        }
        return ttlPortFinder;
    }

    //初始化串口
    public static SerialUtil initSerial(){
        boolean haveSerial = isHaveSerial();
        if(haveSerial == true){
            serialUtil = SerialUtil.INIT;
            serialUtil.init(PORT, 115200, 0);
            return serialUtil;
        }else {
            return null;
        }
    }


    public static boolean isHaveSerial(){
        boolean TAG = false;
        SerialPortFinder sf = new SerialPortFinder();
        String[] allDevicesPath = sf.getAllDevicesPath();
        for (String port : allDevicesPath) {
            if (port.indexOf("/dev/tty") != -1) {
                TAG = true;
                return TAG;
            } else {
                TAG = false;
                return TAG;
            }
        }
        return TAG;
    }


    //检测连接下位机情况
    public static String getConnectionState() {
        String result = "default";
        SerialUtil serialUtils = initSerial();
        if(serialUtils == null){
            return result;
        }else {
            try {
                byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.INIT_ICE);
                while (true){
                    sendOrderUtil = SendOrderUtil.getSendOrderUtil((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC1, (byte) 0x00, crcByteValue[0], crcByteValue[1], (byte) 0xEF);
                    String hex = serialUtils.bytesToHexString(sendOrderUtil, sendOrderUtil.length);
                    serialUtils.setData(sendOrderUtil);
                    Log.d("xuezhiyuan","发送指令转String:"+hex);
                    Thread.sleep(1000);
                    byte[] buffers = serialUtils.getDataByte();
                    String hexString = serialUtils.bytesToHexString(buffers, buffers.length);
                    if("3f".equals(hexString)){
                        result = "default";
                    }else {
                        Log.d("xuezhiyuan","接收的数据"+hexString);
                        String begin = hexString.substring(10, 12);
                        String data = returnResult(begin);
                        return data;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public static String returnResult(String info){
        String result = "def";
        if ("00".equals(info)) {
            //成功
            result = info;
            return result;
        }else if("a1".equals(info)){
            //fail
            result = info;
            return result;
        }else if("a2".equals(info)){
            //fail
            result = info;
            return result;
        }else if("a3".equals(info)){
            //fail
            result = info;
            return result;
        }else {
            //无数据返回
            return result;
        }
    }
}

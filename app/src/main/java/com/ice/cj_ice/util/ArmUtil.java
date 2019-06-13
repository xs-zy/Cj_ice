package com.ice.cj_ice.util;

import android.util.Log;

import com.ice.cj_ice.leyaoyao.SimpleMsgHandler;
import com.ice.cj_ice.protocol.ParamsSettingUtil;

import NDKLoader.HitbotNDKLoader;
import android_serialport_api.SerialUtil;
import cn.lyy.netty.client.NettyClient;

/**
 * Created by Administrator on 2019/5/28.
 */

public class ArmUtil {

    private static HitbotNDKLoader robot = null;
    private static SerialUtil serialUtil;
    public static synchronized HitbotNDKLoader getArmUtil(){
        if(robot == null)
            synchronized (ArmUtil.class){
                if(robot == null){
                    TtlPortFinder ttlPortFinder = TtlPortFinder.getTtlPortFinder();
                    serialUtil = ttlPortFinder.initSerial();
                    robot = new HitbotNDKLoader(25);
                    robot.net_port_initial();
                    robot.initial(5,160);
                    robot.unlock_position();
                }
            }
        return robot;
    }

    /**
     * 初始化网络
     * @return
     */
    public static int init_net(){
        int portInitial = robot.net_port_initial();
        return portInitial;
    }


    /**
     * 初始化机械臂
     * @return
     */
    public static int init_arm(){
        int initial = robot.initial(5, 160);
        return initial;
    }

    /**
     * 解锁机械臂
     * @return
     */
    public static int unlock_arm(){
        int i = robot.unlock_position();
        return i;
    }

    /**
     * 查看机械臂是否连接
     */
    public static int arm_isconnect(){
        int connect = robot.is_connect();
        return connect;
    }

    /**
     * 机械臂运动到初始化位置
     * @param drop_cup  落杯器高度
     * @return
     */
    public static boolean init_location(String drop_cup,String init_location,int delive){
        float[] drop_cup_height = stringToFloat(drop_cup);
        float[] initLocation = stringToFloat(init_location);
        robot.movej_angle(initLocation[3], initLocation[4], drop_cup_height[2], initLocation[5], delive, 0);
        boolean waitStop = robot.wait_stop();
        return waitStop;
    }


    /**
     * 运动到落杯器位置
     * @param drop_cup
     * @param delive
     * @return
     */
    public static boolean drop_location(String drop_cup,int delive){
        float[] drop_cup_loaction = stringToFloat(drop_cup);
        robot.movej_angle(drop_cup_loaction[3], drop_cup_loaction[4], drop_cup_loaction[2], drop_cup_loaction[5], delive, 0);
        boolean waitStop = robot.wait_stop();
        return waitStop;
    }


    /**
     * 运动到冰淇淋料口位置
     * @param ice
     * @param delive
     * @return
     */
    public static boolean ice_location(String ice,int delive){
        float[] ice_loaction = stringToFloat(ice);
        robot.movej_angle(ice_loaction[3], ice_loaction[4], ice_loaction[2], ice_loaction[5], delive, 0);
        boolean waitStop = robot.wait_stop();
        return waitStop;
    }


    /**
     * 运动到配料桶位置
     * @param blunk
     * @param delive
     * @return
     */
    public static boolean blunk_location(String blunk,int delive){
        float[] blunk_loaction = stringToFloat(blunk);
        robot.movej_angle(blunk_loaction[3], blunk_loaction[4], blunk_loaction[2], blunk_loaction[5], delive, 0);
        boolean waitStop = robot.wait_stop();
        return waitStop;
    }


    /**
     * 机械臂运动到旋转台放杯动作
     * @param rotate_platform   获取movej_angle坐标
     */
    public static void platform_location(String rotate_platform){
        float[] rotate_platform_loaction = stringToFloat(rotate_platform);
        robot.movej_angle(rotate_platform_loaction[3], rotate_platform_loaction[4], rotate_platform_loaction[2], rotate_platform_loaction[5], 100, 0);
        robot.wait_stop();

        robot.movej_angle(rotate_platform_loaction[3], rotate_platform_loaction[4], rotate_platform_loaction[2], rotate_platform_loaction[5]-20, 100, 0);
        robot.wait_stop();

        robot.movej_angle(rotate_platform_loaction[3], rotate_platform_loaction[4], rotate_platform_loaction[2], rotate_platform_loaction[5]+20, 100, 0);
        robot.wait_stop();

        robot.movej_angle(rotate_platform_loaction[3], rotate_platform_loaction[4], rotate_platform_loaction[2], rotate_platform_loaction[5], 100, 0);
        robot.wait_stop();
        //下降80
        robot.movej_angle(rotate_platform_loaction[3], rotate_platform_loaction[4], rotate_platform_loaction[2] - 80, rotate_platform_loaction[5], 100, 0);
        robot.wait_stop();
        //后退80
        float base_x = rotate_platform_loaction[0];
        float base_y = rotate_platform_loaction[1];
        float target_setp_x = (float) (80 * Math.cos(rotate_platform_loaction[5] / 180 * Math.PI));
        float target_setp_y = (float) (80 * Math.sin(rotate_platform_loaction[5] / 180 * Math.PI));
        float target_x = base_x - target_setp_x;
        float target_y = base_y - target_setp_y;
        robot.movel_xyz(target_x, target_y, rotate_platform_loaction[2] - 80, rotate_platform_loaction[5], 100);
        robot.wait_stop();
    }

    /**
     * String 转  FLOAT
     * @param data
     * @return
     */
    private static float[] stringToFloat(String data){
        String[] strings = data.split(" ");
        float[] fs = new float[strings.length];
        for (int i = 0; i < strings.length; i++) {
            fs[i] = Float.parseFloat(strings[i]);
        }
        return fs;
    }



    /**
     * 除了冰淇淋机外的外设
     */
    public static int shipment(int TAG,int certain){
        int value = 0;
        switch (TAG){
            case 5:
                //发送接收落杯指令
                int dropCup = send_drop_cup();
                value = dropCup;
                break;
            case 6:
                //发送冰淇淋机口出料
                int send_ice = send_ice(certain);
                value = send_ice;
                break;
            case 7:
                //发送配料桶转动
                int bluck_ice = send_bluck(certain);
                value = bluck_ice;
                break;
            case 8:
                //缺料检测
                int missing_detection = send_Material_missing_detection();
                value = missing_detection;
                break;
            case 9:
                //清洗冰淇淋机

                break;
            case 10:
                //冰淇淋机自动模式
                int open_auto_model = send_open_auto_model();
                value = open_auto_model;
                break;
            case 11:
                //冰淇淋机出料口全部打开全部关闭

                break;
            case 12:
                //冰淇淋机出料口全部关闭

                break;
            case 13:
                //冰淇淋机停止当前动作
                int close_auto_model = send_close_auto_model();
                value = close_auto_model;
                break;
        }
        return value;
    }

    /**
     * 发送开启自动模式
     * @return
     */
    public static int send_open_auto_model(){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.AUTO_MODEL);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC6,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        String receive = receive(12, 14, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }


    /**
     * 发送关闭自动模式
     * @return
     */
    public static int send_close_auto_model(){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.CLOSE_AUTO);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC8,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        String receive = receive(12, 14, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }



    /**
     * 查询冰淇淋机是否缺料
     * @return
     */
    public static  int send_Material_missing_detection(){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_BOX_STATE);
        send((byte) 0x4D, (byte) 0x09, (byte) 0x09, (byte) 0x00, (byte) 0xC1, (byte) 0x00, crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        String receive = receive(10, 12, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }



    /**
     * 发送落杯
     * @return
     */
    public static int send_drop_cup(){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_CUP_ROTE);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC4, (byte) 0x00, crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        String receive = receive(12, 14, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }


    /**
     * 发送落料
     * @return
     */
    public static int send_ice(int TAG){
        switch (TAG){
            case 0:
                //发送指令   落冰淇淋左口
                byte[] crcByteValue1 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_LEFT);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0B, (byte) 0x00, (byte) 0xC2, (byte) 0x01, (byte) 0x00, (byte) 0x02, crcByteValue1[0], crcByteValue1[1], (byte) 0xEF);
                break;
            case 2:
                //发送指令   落冰淇淋中间口
                byte[] crcByteValue2 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_MIDDEL);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0B, (byte) 0x00, (byte) 0xC2, (byte) 0x02, (byte) 0x00, (byte) 0x03, crcByteValue2[0], crcByteValue2[1], (byte) 0xEF);
                break;
            case 1:
                //发送指令   落冰淇淋右口
                byte[] crcByteValue3 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_RIGHT);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0B, (byte) 0x00, (byte) 0xC2, (byte) 0x03, (byte) 0x00, (byte) 0x02, crcByteValue3[0], crcByteValue3[1], (byte) 0xEF);
                break;
        }
        String receive = receive(10, 12, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }




    /**
     * 发送配料桶转动
     * @return
     */
    public static int send_bluck(int TAG){
        switch (TAG){
            case 3:
                //发送指令   1
                byte[] crcByteValue1 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE1);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x01, (byte) 0x03, crcByteValue1[0], crcByteValue1[1], (byte) 0xEF);
                break;
            case 4:
                //发送指令   2
                byte[] crcByteValue2 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE2);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x02, (byte) 0x03, crcByteValue2[0], crcByteValue2[1], (byte) 0xEF);
                break;
            case 5:
                //发送指令   3
                byte[] crcByteValue3 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE3);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x03, (byte) 0x03, crcByteValue3[0], crcByteValue3[1], (byte) 0xEF);
                break;
            case 6:
                //发送指令   4
                byte[] crcByteValue4 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE4);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x04, (byte) 0x03, crcByteValue4[0], crcByteValue4[1], (byte) 0xEF);
                break;
            case 7:
                //发送指令   5
                byte[] crcByteValue5 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE5);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x05, (byte) 0x03, crcByteValue5[0], crcByteValue5[1], (byte) 0xEF);
                break;
            case 8:
                //发送指令   6
                byte[] crcByteValue6 = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_FOOD_ROTE6);
                send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC3, (byte) 0x05, (byte) 0x03, crcByteValue6[0], crcByteValue6[1], (byte) 0xEF);
                break;
        }
        String receive = receive(12, 14, 20);
        if("00".equals(receive)){
            return 1;
        }else {
            return 0;
        }
    }

    /**
     * 发送指令
     * @param args
     */
    public static void send(final byte... args){
        byte[] sendOrderUtil = SendOrderUtil.getSendOrderUtil(args);
        serialUtil.setData(sendOrderUtil);
    }

    /**
     * 接收指令
     */
    public static String receive(int start,int end,int length) {
        String data = "3f";
        try {
            for (int i = 0; i < 10; i++) {
                byte[] dataByte = serialUtil.getDataByte();
                String hexString = serialUtil.bytesToHexString(dataByte, dataByte.length);
                Log.d("xuezhiyuan===",hexString);
                if ("3f".equals(hexString)) {
                    Thread.sleep(800);
                } else {
                    //已收到结果
                    int size = hexString.length();
                    if(size == length){
                        data = hexString.substring(start, end);
                        return data;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 旋转出料
     * @param location  转动位置
     * @param delive   转动速度
     * @param p   转动圈数
     */
    public static void rotate_ice(String location,int delive,int p){
        float[] toFloat = stringToFloat(location);
        robot.wait_stop();
        robot.pause_move();
        int n=0;
        int angle = 0;
        while (true) {
            int x = LengthUtil.getX(10, angle);
            int y = LengthUtil.getY(10, angle);
            // x y z r d 0
            robot.movel_xyz(toFloat[0] + x, toFloat[1] + y, toFloat[2], toFloat[5], delive);
            n++;
            if (n == 13) {
                robot.resume_move();
            }
            angle += 5;
            if (angle == 360 * p) {
                robot.movel_xyz(toFloat[0], toFloat[1], toFloat[2]-40, toFloat[5], 100);
                break;
            }
           /* if(start == false){
                robot.movel_xyz(toFloat[0], toFloat[1], toFloat[2]-40, toFloat[5], 100);
                break;
            }*/
        }
    }



}

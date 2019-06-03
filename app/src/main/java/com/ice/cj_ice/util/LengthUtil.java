package com.ice.cj_ice.util;

/**
 * Created by Administrator on 2019/5/15.
 */

public class LengthUtil {

    public static int getX(int r,int angle){
        //r  半径
        int X = (int) (r*(sinX(angle)+cosX(angle)));
        return X;
    }

    public static int getY(int r,int angle){
        //r  半径
        int Y = (int) (r*(sinX(angle)-cosX(angle)));
        return Y;
    }

    //弧长=角度/360*2π
    public static double sinX (int x){
        return Math.sin(Math.PI * x / 180);
    }

    public static double cosX (int x){
        return Math.cos(Math.PI * x / 180);
    }

}

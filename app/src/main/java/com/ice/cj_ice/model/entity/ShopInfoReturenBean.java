package com.ice.cj_ice.model.entity;

/**
 * Created by Administrator on 2019/5/10.
 */
//上传推理数据返回
public class ShopInfoReturenBean {
    //{"i":5.0,"n":"核桃仁","pi":"","g":1.0,"p":1.0,"co":1.0,"c":1.0,"cu":20.0}
    private int i;
    private String n;
    private String pi;
    private int g;
    private int p;
    private int co;
    private int c;
    private int cu;

    public ShopInfoReturenBean(){}

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getCu() {
        return cu;
    }

    public void setCu(int cu) {
        this.cu = cu;
    }

    @Override
    public String toString() {
        return "ShopInfoReturenBean{" +
                "i=" + i +
                ", n='" + n + '\'' +
                ", pi='" + pi + '\'' +
                ", g=" + g +
                ", p=" + p +
                ", co=" + co +
                ", c=" + c +
                ", cu=" + cu +
                '}';
    }
}

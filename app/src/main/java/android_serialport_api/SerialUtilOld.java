package android_serialport_api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator
 */
public class SerialUtilOld {

    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private  volatile int size=-1;
    private static final int MAX =1024;
    private static final int MIN =512;
    private byte[] buffer;

    public SerialUtilOld(String path, int baudrate, int flags ) throws NullPointerException {
        try {
            serialPort=new SerialPort(new File(path),baudrate,flags);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (SecurityException e) {
            e.printStackTrace();
        }
        if(serialPort!=null){
            //设置读、写
            inputStream=serialPort.getInputStream();
            outputStream=serialPort.getOutputStream();
        }else {
            throw new NullPointerException("串口设置有误");
        }
    }

    /**
     * 取得byte的长度
     * @return
     */
    public int getSize(){
        return size;
    }

    /**
     * 串口读数据
     * @return
     */
    public synchronized byte[] getData() throws NullPointerException {
        //上锁，每次只能一个线程在取得数据
        try {
            buffer = new byte[1024];
            if (inputStream==null) throw new NullPointerException("inputStream is null");
            //一次最多可读Max的长度
            size=inputStream.read(buffer);
            if (size>0) return buffer;
            else return new byte[]{63};
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{58};
        }
    }

    public void clearBuffer(){
        buffer = null;
    }
    //串口得到数据
    public synchronized byte[] getDataByte()throws NullPointerException {
        try {
            byte [] buffer=new byte[inputStream.available()];
            if (inputStream==null) throw new NullPointerException("is null");
            if (inputStream.available()>0){
                inputStream.read(buffer);
                return buffer;
            }else return new byte[]{63};
        } catch (IOException e) {
            e.printStackTrace();
            //63 3f         3a  58
            return new byte[]{58};
        }
    }

    /**
     * 患上写数据
     * @param data 显示的16进制的字符串
     */
    public synchronized void setData(byte[] data) throws NullPointerException {
        if (outputStream==null) throw new NullPointerException("outputStream为空");
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * byte转hexString
     * @param buffer 数据
     * @param size  字符数
     * @return
     */
    public String bytesToHexString(final byte[] buffer, final int size){
        StringBuilder stringBuilder=new StringBuilder("");
        if (buffer==null||size<=0) return null;
        for (int i = 0; i <size ; i++) {
            String hex= Integer.toHexString(buffer[i]&0xff);
            if(hex.length()<2) stringBuilder.append(0);
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    /**
     * hexString转byte
     * @param hexString
     * @return
     */
    public byte[] hexStringToBytes(String hexString){
        if (hexString==null||hexString.equals("")) return null;
        hexString=hexString.toUpperCase();
        int length=hexString.length()/2;
        char[] hexChars=hexString.toCharArray();
        byte[] d=new byte[length];
        for (int i = 0; i <length ; i++) {
            int pos=i*2;
            d[i]=(byte)(charToByte(hexChars[pos])<<4|charToByte(hexChars[pos+1]));
    }
        return d;
    }
    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
    //<--------------------------------------------------------------------->


    /**
     * 字符串转换成十六进制字符串
     * @param 。String str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     * @param  、String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */

    public String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * bytes转换成十六进制字符串
     * @param 。byte[] b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public String byte2HexStr(byte[] b) {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * bytes字符串转换为Byte值
     * @param 。String src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public  byte[] hexStr2Bytes(String src)
    {
        int m=0,n=0;
        int l=src.length()/2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m=i*2+1;
            n=m+1;
            ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
        }
        return ret;
    }

    /**
     * * String的字符串转换成unicode的String
     * * @param String strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public String strToUnicode(String strText)
            throws Exception
    {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++)
        {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else // 低位在前面补00
                str.append("\\u00" + strHex);
        }
        return str.toString();
    }

    /**
      * unicode的String转换成String的字符串
      * @paramString hex 16进制值字符串 （一个unicode为2byte）
      * @return String 全角字符串
      */
    public String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++)
        {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // 高位需要补上00再转
            String s1 = s.substring(2, 4) + "00";
            // 低位直接转
            String s2 = s.substring(4);
            // 将16进制的string转为int
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
            // 将int转换为字符
            char[] chars = Character.toChars(n);
            str.append(new String(chars));
        }
        return str.toString();
    }

    public byte[] intToBytes(int n){
        byte[] b = new byte[2];
        b[0] = (byte)(n >> 8);
        b[1] = (byte)(n&0xFF);
       /*  for(int i=0;i<2;i++){
            b[i] = (byte)(b[i] >> (24-i*8));
        }*/
        return b;
    }
    /**
     * BYTE 转 bit
     */
    public static String byte2Bit(byte b){
        return ""+(byte)((b>>7) & 0x1) +
                (byte)((b>>6) & 0x1)+
                (byte)((b>>5) & 0x1)+
                (byte)((b>>4) & 0x1)+
                (byte)((b>>3) & 0x1)+
                (byte)((b>>2) & 0x1)+
                (byte)((b>>1) & 0x1)+
                (byte)((b>>0) & 0x1);
    }

}

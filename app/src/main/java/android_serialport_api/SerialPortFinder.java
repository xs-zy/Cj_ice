package android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

/**
   串口通讯的 寻检器
   1.获取/dev下的所有文件，得到一个文件数组files。
   2.循环files，等到files[i]。
   3.获取files[i]的绝度路径，判断这个绝对路径是不是以3.2.1中传递过来的基础地址filename开头。（例如：我们传递过来的是基础地址是：/dev/ttyS，那么我们这里满足条件的就是：/dev/ttyS0，/dev/ttyS1，/dev/ttyS2，/dev/ttyS3，这四个设备就是串口设备）
   4.满足3说明是串口设备，保存到集合中，不满足执行5。
   5.判读是否已经循环结束。结束返回集合，没有结束执行2。
   Created by Administrator
 */
public class SerialPortFinder {

    /**
     * 设备类
     * @author Administrator
     */
    public class Driver {
        public Driver(String name, String root) {
            mDriverName = name;
            mDeviceRoot = root;
        }
        //设备名称
        private String mDriverName;
        //设备根节点
        private String mDeviceRoot;
        //设备集合
        Vector<File> mDevices = null;

        /**
         * 获取设备集合 这个是特定类型的设备 比如USB等
         * @return
         */
        public Vector<File> getDevices() {
            //如果设备已经添加过，就不添加了，否则就必须添加
            if (mDevices == null) {//如果
                mDevices = new Vector<File>();
                File dev = new File("/dev");
                //获取 /dev 下的设备
                File[] files = dev.listFiles();
                int i;
                for (i = 0; i < files.length; i++) {
                    //这里是拿文件的路径和我们 传递进来的路径进行对比，看看开头是不是我们传递进来的  ，传递进来的函数是 外部类 getDrivers()
                    //比如 我们传递进来的文件路径是：  /dev/ttyUSB 那么我们获取道的文件的绝对路径 如：/dev/ttyUSB1 /dev/ttyUSB2
                    //就满足下面这个条件 ，就会加到集合中去
                    if (files[i].getAbsolutePath().startsWith(mDeviceRoot)) {
                        Log.d(TAG, "Found new device: " + files[i]);
                        mDevices.add(files[i]);
                    }
                }
            }
            return mDevices;
        }

        public String getName() {
            return mDriverName;
        }
    }

    private static final String TAG = "SerialPort";

    private Vector<Driver> mDrivers = null;

    /**
     * 获取设备
     * @return
     * @throws IOException
     * 其实就是读取 /proc/tty/drivers 这个文件
     * drivers中有设备的地址的总地址添加到一个集合中
     */
    Vector<Driver> getDrivers() throws IOException {
        if (mDrivers == null) {
            mDrivers = new Vector<Driver>();
            //读取行的流对象
            LineNumberReader r = new LineNumberReader(new FileReader(
                    "/proc/tty/drivers"));
            String l;
            while ((l = r.readLine()) != null) {
                // Issue 3:
                // Since driver name may contain spaces, we do not extract
                // driver name with split()
                //看下面这个会发现 原来我们的 这个第一字符到第二个字符之间是21个，这也就是为什么这里面要用的 0x15 这个原因了
                //dev/tty             /dev/tty        5       0 system:/dev/tty
                String drivername = l.substring(0, 0x15).trim();
                //其实就是获取第一个非空格字符串
                String[] w = l.split(" +");//这里是正则表达式：" +" 表示有一个或者多个空格
                if ((w.length >= 5) && (w[w.length - 1].equals("serial"))) {
                    Log.d(TAG, "Found new driver " + drivername + " on "
                            + w[w.length - 4]);
                    mDrivers.add(new Driver(drivername, w[w.length - 4]));
                }
            }
            r.close();//关闭流
        }
        return mDrivers;
    }

    /**
     * 得到所有的设备的名称
     * @return
     */
    public String[] getAllDevices() {
        Vector<String> devices = new Vector<String>();
        // Parse each driver
        Iterator<Driver> itdriv;
        try {
            itdriv = getDrivers().iterator();//获取道设备的根地址
            while (itdriv.hasNext()) {//迭代获取具体的根路径
                Driver driver = itdriv.next();
                Iterator<File> itdev = driver.getDevices().iterator();//获取道包含根地址的设备对象的集合
                while (itdev.hasNext()) {//迭代获得具体的设备
                    String device = itdev.next().getName();
                    String value = String.format("%s (%s)", device,
                            driver.getName());
                    devices.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[devices.size()]);
    }

    /**
     * 获取道设备的路径
     * @return
     */
    public String[] getAllDevicesPath() {
        Vector<String> devices = new Vector<String>();
        // Parse each driver
        Iterator<Driver> itdriv;
        try {
            itdriv = getDrivers().iterator();
            while (itdriv.hasNext()) {
                Driver driver = itdriv.next();
                Iterator<File> itdev = driver.getDevices().iterator();
                while (itdev.hasNext()) {
                    //获取
                    String device = itdev.next().getAbsolutePath();
                    devices.add(device);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[devices.size()]);
    }

}

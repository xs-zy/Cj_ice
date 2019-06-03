package NDKLoader;


/**
 * Created by Administrator on 2019/3/12.
 */

public class IceInterface {

    //private String port;
    private int robot_id;
    private HitbotNDKLoader robot;
    //private SerialPortUtil serialPortUtil;

    public IceInterface(int robot_id){
        //this.port = port;
        this.robot_id = robot_id;
    }

    /**
     * 初始化机械臂
     * @return 0初始化失败 （机械臂不在线）  1初始化成功
     */
    public int initArm() {
        //加载库文件
        robot.net_port_initial();                    //初始化网络
        robot = new HitbotNDKLoader(robot_id);
        if (robot.is_connect() != 1) {                   //判断机器人是否在线
            System.out.println("机械臂不在线");
            return 0;
        }else {
            robot.initial(5,160);                           //机械臂初始化
            robot.unlock_position();                        //运动前，需解锁机械臂
            //初始化完成
            return 1;
        }
    }

    /**
     * 初始化下位机   打开串口
     */
    public void initStm32(){
      //  serialPortUtil = new SerialPortUtil(port);
       // serialPortUtil.openSerialPort();
    }

    /**
     * 关闭下位机   关闭串口
     */
    public void closeStm32(){
       // serialPortUtil = new SerialPortUtil(port);
       // serialPortUtil.openSerialPort();
    }

    /**
     * 发送机械臂运动到目标点
     * @param ret_location  目标点坐标数组
     * @return 本次状态
     */
    public int motion_arm(float[] ret_location){
        int state = robot.movej_angle(ret_location[0],
                ret_location[1],
                ret_location[2],
                ret_location[3],
                ret_location[4],
                ret_location[5]);
        return state;
    }

    /**
     * 判断机械臂运动是否结束
     * @return  true结束
     */
    public boolean is_arm_stop(){
        boolean wait_stop = robot.wait_stop();
        return wait_stop;
    }

    /**
     * 机械臂开启示教
     */
    public void open_arm_study(){
        robot.set_drag_teach_state(true);
    }

    /**
     * 机械臂关闭示教
     */
    public void close_arm_study(){
        robot.set_drag_teach_state(false);
    }


    /**
     * 获取当前机械臂示教状态
     * @return  false  开启   true  关闭
     */
    public boolean get_arm_study(){
        boolean drag_teach_state = robot.get_drag_teach_state();
        return drag_teach_state;
    }

    /**
     * 获取机械臂当前位置
     * @return
     */
    public float[] get_arm_location(){
        float[] location = robot.get_scara_location();
        return location;
    }

    /**
     * 获取机械臂当前状态
     * @return
     */
    public boolean[] get_arm_state(){
        boolean[] scara_state = robot.get_scara_state();
        return scara_state;
    }




}

package NDKLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 慧灵科技 on 2019-04-18.
 */

public class HitbotNDKLoader {

    private int robot_id;
    public float x = 0, y = 0, z = 0, angle1 = 0, angle2 = 0, r = 0;
    public boolean communicate_success = false, move_flag = false, initial_finish = false;
    public int efg_type = 0;
    public float efg_dis = 0;


    //加载库文件
    static {
        System.loadLibrary("native-lib");
    }

    //声明成员变量
    public HitbotNDKLoader(int robot_id) {
        this.robot_id = robot_id;
    }


    /**
     * 初始化网络
     * @return  0成功    1失败
     */
    public native static int net_port_initial();


    /**
     * 初始化机械臂的各项参数 机械臂进入工作状态
     * @param robot_id   机械臂ID
     * @param generation  臂展系列  400mm为1   320mm为5
     * @param z_trail   上下关节有效行程
     * @return  0  机械臂不在线   1  初始化成功
     */
    private native int robot_initial(int robot_id, int generation, int z_trail);
    public int initial(int generation, int z_trail) {
        return robot_initial(robot_id, generation, z_trail);
    }

    /**
     * 获取机械臂位置坐标
     * @param robot_id  机械臂ID
     * @return float类型机械臂位置坐标
     */
    private native float[] robot_get_param_coord(int robot_id);
    public float[] get_scara_location(){
        float[] ret_coord = robot_get_param_coord(robot_id);
        x = ret_coord[0];
        y = ret_coord[1];
        z = ret_coord[2];
        angle1 = ret_coord[3];
        angle2 = ret_coord[4];
        r = ret_coord[5];
        return ret_coord;
    }

    /**
     * 获取机械臂状态
     * @param robot_id  机械臂ID
     * @return boolean 是否在线  是否初始化  是否运动
     */
    private native boolean[] robot_get_param_state(int robot_id);
    public boolean[] get_scara_state(){
        boolean[] ret_state = robot_get_param_state(robot_id);
        communicate_success = ret_state[0];
        initial_finish = ret_state[1];
        move_flag = ret_state[2];
        return ret_state;
    }

    //更新xyz变量                   无返回
    public float[] get_scara_param() {

        float[] ret_coord = robot_get_param_coord(robot_id);
        x = ret_coord[0];
        y = ret_coord[1];
        z = ret_coord[2];
        angle1 = ret_coord[3];
        angle2 = ret_coord[4];
        r = ret_coord[5];
        EventBus.getDefault().post(ret_coord);
        boolean[] ret_state = robot_get_param_state(robot_id);
        communicate_success = ret_state[0];
        initial_finish = ret_state[1];
        move_flag = ret_state[2];
        return ret_coord;
    }

    /**
     *  设置机械臂轴一和轴二长度
     * @param robot_id 机械臂ID
     * @param l1 轴一长度
     * @param l2 轴二长度
     */
    private native void robot_set_arm_length(int robot_id, float l1, float l2);
    public void set_arm_length(float l1, float l2) {
        robot_set_arm_length(robot_id, l1, l2);
    }

    /**
     *  解锁机械臂   使机械臂可以接受运动指令
     * @param robot_id  机械臂ID
     * @return 0  机械臂不在线   1  解锁成功
     */
    private native int robot_unlock_position(int robot_id);
    public int unlock_position() {
        return robot_unlock_position(robot_id);
    }


    /**
     * 查询机械臂是否在线
     * @param robot_id 机械臂ID
     * @return 0机械臂不在线   1在线
     */
    private native int robot_is_connect(int robot_id);
    public int is_connect() {
        return robot_is_connect(robot_id);
    }


    /**
     * 获取机械臂关节状态
     * @param robot_id  机械臂ID
     * @param joint_number  轴号
     * @return 轴状况
     * 0：轴发生复位，需要重新初始化
    • 1：关节正常 关节正常 关节正常
    • 2：传入参数错误 传入参数错误 传入参数错误 传入参数错误
    • 3：未初始化 未初始化 未初始化
    • 4：轴状态获取失败 轴状态获取失败 轴状态获取失败 轴状态获取失败
    • 5：发生碰撞 发生碰撞 发生碰撞 ;
    • 6：处于拖动模式处于拖动模式处于拖动模式处
     */
    private native int robot_get_joint_state(int robot_id, int joint_number);
    public int get_joint_state(int joint_number) {
        return robot_get_joint_state(robot_id, joint_number);
    }

    /**
     *  设定拖动示教功能是否开启
     * @param robot_id 机械臂ID
     * @param enable true 开启  false关闭
     * @return false 开启失败  true开启成功
     */
    private native boolean robot_set_drag_teach_state(int robot_id, boolean enable);
    public boolean set_drag_teach_state(boolean enable) {
        return robot_set_drag_teach_state(robot_id, enable);
    }

    /**
     *  查询是否处于拖动示教状态
     * @param robot_id 机械臂ID
     * @return false开启   true关闭
     */
    private native boolean robot_get_drag_teach_state(int robot_id);
    public boolean get_drag_teach_state() {
        return robot_get_drag_teach_state(robot_id);
    }

    /**
     * 设定机械臂协作功能是否开启  开启后机械臂遇到障碍物会停止运动并上报此状态
     * @param robot_id 机械臂ID
     * @param enable true  开启    false  关闭
     * @return  false  开启成功    true  开启失败
     */
    private native boolean robot_set_cooperation_fun_state(int robot_id, boolean enable);
    public boolean set_cooperation_fun_state(boolean enable) {
        return robot_set_cooperation_fun_state(robot_id, enable);
    }

    /**
     * 查询机械臂是否开启协作功能
     * @param robot_id 机械臂ID
     * @return false  开启     true  关闭
     */
    private native boolean robot_get_cooperation_fun_state(int robot_id);
    public boolean get_cooperation_fun_state() {
        return robot_get_cooperation_fun_state(robot_id);
    }


    /**
     *  查询机械臂是否发生碰撞
     * @param robot_id 机械臂ID
     * @return false 未发生碰撞  true 发生碰撞
     */
    private native boolean robot_is_collision(int robot_id);
    public boolean is_collision() {
        return robot_get_cooperation_fun_state(robot_id);
    }

    /**
     * 立即结束当前正在执行的指令
     * @param robot_id 机械臂ID
     */
    private native void robot_stop_move(int robot_id);
    public void stop_move() {
        robot_stop_move(robot_id);
    }

    /**
     * 使轴回到零位
     * @param robot_id 机械臂ID
     * @param joint_num 轴号
     * @return 0 机械臂未连接   1调用成功   2传入参数错误   3机械臂正在初始化
     */
    private native int robot_joint_home(int robot_id, int joint_num);
    public int joint_home(int joint_num) {
        return robot_joint_home(robot_id, joint_num);
    }

    /**
     * 以movel运动方式运动到目标点
     * @param robot_id  机械臂ID
     * @param goal_x 目标点 X坐标 （mm）
     * @param goal_y 目标点 Y坐标 （mm）
     * @param goal_z 目标点 Z坐标 （mm）
     * @param goal_r 目标点 R坐标 （mm）
     * @param speed  xyz点的线速度  （mm/s）  轴4旋转角速度
     * @return 状态
    • 0：正在执行其他指令，本次指令无效
    • 1：本次指令生效，机械臂开始运动
    • 2：设置速度小于等零
    • 3：未初始化
    • 4：过程点无法到达
    • 6：伺服未开启
    • 7：存在中间过程点无法以机械臂当前姿态（手系）达到
    • 11 ：手机端在控制
    • 101 ：传入参数 NOT A NUMBERNOT
    • 102 ：发生碰撞，本次指令无效
    • 103 ：轴发生复位， 需要重新初始化本次指令无效
     */
    private native int robot_movel_xyz(int robot_id, float goal_x, float goal_y, float goal_z, float goal_r, float speed);
    public int movel_xyz(float goal_x, float goal_y, float goal_z, float goal_r, float speed) {
        return robot_movel_xyz(robot_id, goal_x, goal_y, goal_z, goal_r, speed);
    }

    /**
     * 以movej的运动方式到达目标点
     * @param robot_id 机械臂ID
     * @param goal_x 目标点X坐标
     * @param goal_y 目标点Y坐标
     * @param goal_z 目标点Z坐标
     * @param goal_r 目标点R坐标
     * @param speed  xyz点的线速度  。。。
     * @param rough  暂未理解
     * @return  ...
     */
    private native int robot_movej_xyz(int robot_id, float goal_x, float goal_y, float goal_z, float goal_r, float speed, float rough);
    public int movej_xyz(float goal_x, float goal_y, float goal_z, float goal_r, float speed, float rough) {
        return robot_movej_xyz(robot_id, goal_x, goal_y, goal_z, goal_r, speed, rough);
    }

    /**
     * 以movej的运动方式到达目标点
     * @param robot_id
     * @param goal_angle1
     * @param goal_angle2
     * @param goal_z
     * @param goal_r
     * @param speed
     * @param rough
     * @return
     */
    private native int robot_movej_angle(int robot_id, float goal_angle1, float goal_angle2, float goal_z, float goal_r, float speed, float rough);
    public int movej_angle(float goal_angle1, float goal_angle2, float goal_z, float goal_r, float speed, float rough) {
        return robot_movej_angle(robot_id, goal_angle1, goal_angle2, goal_z, goal_r, speed, rough);
    }

    /**
     * 切换手系
     * @param robot_id 机械臂ID
     * @param speed 运动速度
     * @return 切换成功   切换失败
     */
    private native int robot_change_attitude(int robot_id, float speed);
    public int change_attitude(float speed) {
        return robot_change_attitude(robot_id, speed);
    }


    /**
     * 以movel的运动方式在指定方向上移动指定距离
     * @param robot_id
     * @param offset_x
     * @param offset_y
     * @param offset_z
     * @param offset_r
     * @param speed
     * @return
     */
    private native int robot_movel_xyz_by_offset(int robot_id, float offset_x, float offset_y, float offset_z, float offset_r, float speed);
    public int xyz_by_offset(float offset_x, float offset_y, float offset_z, float offset_r, float speed) {
        return robot_movel_xyz_by_offset(robot_id, offset_x, offset_y, offset_z, offset_r, speed);
    }


    /**
     * 用于判断运动是否结束   直到运动结束才会返回
     * @param robot_id 机械臂ID
     * @return 默认返回true
     */
    private native boolean robot_wait_stop(int robot_id);
    public boolean wait_stop() {
        return robot_wait_stop(robot_id);
    }

    /**
     * 暂停机械臂的运动
     * @param robot_id 机械臂ID  函数返回后  经过一定延时，机械臂停止运动
     * @return  默认返回1
     */
    private native int robot_pause_move(int robot_id);
    public int pause_move() {
        return robot_pause_move(robot_id);
    }

    /**
     *  恢复机械臂运动
     * @param robot_id 机械臂ID
     * @return 默认返回1
     */
    private native int robot_resume_move(int robot_id);
    public int resume_move() {
        return robot_resume_move(robot_id);
    }


    /**
     *  立即停止机械臂运动  然后释放除上下以外关节的转矩
     * @param robot_id 机械臂ID
     */
    private native void robot_emergency_stop(int robot_id);
    public void emergency_stop() {
        robot_emergency_stop(robot_id);
    }


    /**
     *  立即停止机械臂运动  然后释放除上下以外关节的转矩
     * @param robot_id 机械臂ID
     * @return 0停止   1运动
     */
    private native int robot_is_stop(int robot_id);
    public int is_stop() {
        return robot_is_stop(robot_id);
    }

    /**
     *  设定io输出点状态  out0对应接口文档out1
     * @param robot_id 机械臂ID
     * @param io_number io输出点编号
     * @param io_value 设定值
     * @return  0  参数错误  1  设置成功   3  未初始化
     */
    private native int robot_set_digital_io_out(int robot_id, int io_number, boolean io_value);
    public int set_digital_io_out(int io_number, boolean io_value) {
        return robot_set_digital_io_out(robot_id, io_number, io_value);
    }

    /**
     *  获取io输出点状态
     * @param robot_id 机械臂ID
     * @param io_number io输出点编号
     * @return -1 参数错误  0 io口输出状态为断开   1 io口输出状态为导通  3未初始化
     */
    private native int robot_get_digital_io_out(int robot_id, int io_number);
    public int get_digital_io_out(int io_number) {
        return robot_get_digital_io_out(robot_id, io_number);
    }

    /**
     *  获取io输入点状态
     * @param robot_id 机械臂ID
     * @param io_number io输入点编号
     * @return -1 参数错误  0 io口输入点没有被触发   1 io口输入点被触发  3未初始化
     */
    private native int robot_get_digital_io_in(int robot_id, int io_number);
    public int get_digital_io_in(int io_number) {
        return robot_get_digital_io_in(robot_id, io_number);
    }


    private native int robot_set_efg_fun_state(int robot_id, int type, float distance);
    public int set_efg_fun_state(int type, float distance) {
        return robot_set_efg_fun_state(robot_id, type, distance);
    }


    private native int robot_get_efg_type(int robot_id);
    private native float robot_get_efg_dis(int robot_id);
    public int get_efg_fun_state(int type) {

        int scan_type = robot_get_efg_type(robot_id);
        float scan_dis = robot_get_efg_dis(robot_id);
        if (scan_type == 3) {
            return 3;
        } else {
            efg_type = scan_type;
            efg_dis = scan_dis;
            return 1;
        }

    }

    public native static int closeServer();



}

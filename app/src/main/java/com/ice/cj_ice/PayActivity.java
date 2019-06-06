package com.ice.cj_ice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.leyaoyao.OpenNettyDemo;
import com.ice.cj_ice.model.db.DBManager;
import com.ice.cj_ice.protocol.ParamsSettingUtil;
import com.ice.cj_ice.util.ArmUtil;
import com.ice.cj_ice.util.CRC16;
import com.ice.cj_ice.util.LengthUtil;
import com.ice.cj_ice.util.SendOrderUtil;
import com.ice.cj_ice.util.TtlPortFinder;
import NDKLoader.HitbotNDKLoader;
import android_serialport_api.SerialUtil;


public class PayActivity extends BaseActivity implements View.OnClickListener {

    //private ProgressBar progesss;
    private TextView progesssValue, shipment_state;
    private LinearLayout lin;
    private ImageView imageView_success;
    private Button come_on_btn, back_main_page;
    private String info,key;
    private HitbotNDKLoader robot;
    private SharedPreferences sharedPreferences;
    int angle = 0;
    private DBManager dbManager;
    private String rotate_platform,dropCup,ice_left,ice_middle,ice_right,blank_one,blank_two,blank_three,blank_four,blank_five;
    private int location_z;
    private int location_p;


    @Override
    protected void layoutId() {
        setContentView(R.layout.activity_finash_pay_page);
    }

    @Override
    protected void initView() {
        dbManager = new DBManager(this);
        shipment_state = (TextView) findViewById(R.id.shipment_state);
        imageView_success = (ImageView) findViewById(R.id.shipment_icon);
        //progesss = (ProgressBar) findViewById(R.id.progesss1);
        progesssValue = (TextView) findViewById(R.id.progesss_value1);
        lin = (LinearLayout) findViewById(R.id.lin_Btn);
        come_on_btn = (Button) findViewById(R.id.come_on_make);
        back_main_page = (Button) findViewById(R.id.back_main_page);
        imageView_success.setImageResource(R.drawable.ice_gif);
        Glide.with(this).load(R.drawable.ice_gif).asGif().into(imageView_success);
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundleExtra =
                intent.getBundleExtra("message");
        info = bundleExtra.getString("info", null);
        key = bundleExtra.getString("key", null);
        //货道
        location_z = bundleExtra.getInt("location_z", 400);
        location_p = bundleExtra.getInt("location_p", 400);

        Log.d("xuezhiyuan",location_z+"主料");
        Log.d("xuezhiyuan",location_p+"配料");

        //加载静态库
        robot = ArmUtil.getArmUtil();
        sharedPreferences = App.activity.getSharedPreferences("location", MODE_PRIVATE);
        //获取坐标值
        rotate_platform = sharedPreferences.getString("rotate_platform", null);
        dropCup = sharedPreferences.getString("drop_cup", null);
        ice_left = sharedPreferences.getString("ice_left", null);
        ice_middle = sharedPreferences.getString("ice_middle", null);
        ice_right = sharedPreferences.getString("ice_right", null);
        blank_one = sharedPreferences.getString("blank_one", null);
        blank_two = sharedPreferences.getString("blank_two", null);
        blank_three = sharedPreferences.getString("blank_three", null);
        blank_four = sharedPreferences.getString("blank_four", null);
        blank_five = sharedPreferences.getString("blank_five", null);
    }

    @Override
    protected void initListener() {
        come_on_btn.setOnClickListener(this);
        back_main_page.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        shipment(location_z,location_p);
    }


    @Override
    protected void in(Class tClass) {
        Intent intent = new Intent(this, tClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.come_on_make:
                in(BannerActivity.class);
                break;
            case R.id.back_main_page:
                in(BannerActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * 出货
     * @param z  主料货道
     * @param p  配料货道
     */
    public void shipment(final int z,final int p) {
        angle = 0;
        if (robot != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        ArmUtil.init_arm();
                        ArmUtil.unlock_arm();
                         //机械臂运动到初始化
                        ArmUtil.init_location(dropCup,100);
                        //运动到落杯器
                        ArmUtil.drop_location(dropCup,100);
                        //发送落杯
                        int drop_result = ArmUtil.shipment(5, 0);
                        if(drop_result == 1){
                            changeText("取杯成功");
                            //机械臂运动到初始化
                            ArmUtil.init_location(dropCup,100);
                            //运动到出料口
                            String location = getInfoLocation(z);
                            ArmUtil.ice_location(location,100);
                            changeText("准备接冰淇淋");
                            //发送出料
                            int ice_discharge = ArmUtil.shipment(6, z);
                            if(ice_discharge == 1){
                                //旋转
                                ArmUtil.rotate_ice(location,100,2);
                                changeText("正在接冰淇淋");
                                sleep(8000);
                                //是否运动到配料桶
                                if(p != 400){
                                    String p_location = getInfoLocation(p);
                                    //运动到配料桶
                                    changeText("正在赶往配料桶");
                                    ArmUtil.blunk_location(p_location,30);
                                    changeText("已到达配料桶");
                                    sleep(2000);
                                    int p_discharge = ArmUtil.shipment(7, p);
                                    if(p_discharge == 1){
                                        changeText("接配料成功");
                                    }else {
                                        sleep(3000);
                                        //changeText("接配料失败...出货失败...");
                                        choose_state(3);
                                        ArmUtil.init_location(dropCup,100);
                                    }
                                }
                                //放杯子动作
                                ArmUtil.platform_location(rotate_platform);
                                ArmUtil.init_location(dropCup,100);
                                choose_state(5);
                            }else {
                                sleep(3000);
                                //changeText("接冰淇淋失败...出货失败...");
                                choose_state(2);
                                ArmUtil.init_location(dropCup,100);
                            }
                        }else {
                            sleep(3000);
                            //changeText("取杯失败...出货失败...");
                            choose_state(1);
                            //机械臂运动到初始化
                            ArmUtil.init_location(dropCup,100);
                        }
                    }
                }
            }).start();
        }
    }


    /**
     * 等待时长
     * @param time
     */
    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新文本内容
     * @param text
     */
    private void changeText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shipment_state.setText(text);
            }
        });
    }

    /**
     * 出货成功
     */
    private void successResult(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shipment_state.setText(text);
                imageView_success.setImageResource(R.drawable.finash);
                lin.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * 出货失败
     */
    private void failResult(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shipment_state.setText(text);
                imageView_success.setImageResource(R.drawable.shipment_error);
                lin.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 上传设备启动信息
     */
    private void choose_state(int TAG){
        switch (TAG){
            case 1:
                //取杯子
                failResult("出货失败，您将在一分钟内收到退款\n" +
                        "感谢您的购买，期待后续服务");
                OpenNettyDemo.eqStartResult(key,false,info);
                break;
            case 2:
                //接冰淇淋
                failResult("出货失败，您将在一分钟内收到退款\n" +
                        "感谢您的购买，期待后续服务");
                OpenNettyDemo.eqStartResult(key,false,info);
                break;
            case 3:
                //接配料
                failResult("出货失败，您将在一分钟内收到退款\n" +
                        "感谢您的购买，期待后续服务");
                OpenNettyDemo.eqStartResult(key,false,info);
                break;
            case 5:
                successResult("冰淇淋已经摆放到位，请取走您的冰淇淋\n" +
                        "别忘了获取勺子以享用美味\n");
                //设备启动成功
                OpenNettyDemo.eqStartResult(key,true,info);
                if(location_p != 400){
                    //只有主料
                    //查询货道剩余量
                    String dataCs_p = dbManager.queryDataCs(String.valueOf(location_p));
                    OpenNettyDemo.uploadGift(dataCs_p,"1",String.valueOf(location_p));
                }
                String dataCs = dbManager.queryDataCs(String.valueOf(location_z));
                OpenNettyDemo.uploadGift(dataCs,"1",String.valueOf(location_z));
                break;
        }
    }


    /**
     * 通过货道得到坐标
     * @param TAG
     * @return
     */
    private String getInfoLocation(int TAG){
        String info = null;
        switch (TAG){
            case 0:
                info = ice_left;
                break;
            case 1:
                info = ice_right;
                break;
            case 2:
                info = ice_middle;
                break;
            case 3:
                info = blank_one;
                break;
            case 4:
                info = blank_two;
                break;
            case 5:
                info = blank_three;
                break;
            case 6:
                info = blank_four;
                break;
            case 7:
                info = blank_five;
                break;
        }
        return info;
    }
}

package com.ice.cj_ice;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dou361.dialogui.DialogUIUtils;
import com.hb.dialog.dialog.LoadingDialog;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.protocol.ParamsSettingUtil;
import com.ice.cj_ice.util.ArmUtil;
import com.ice.cj_ice.util.CRC16;
import com.ice.cj_ice.util.CommomDialog;
import com.ice.cj_ice.util.LengthUtil;
import com.ice.cj_ice.util.SegmentView;
import com.ice.cj_ice.util.SendOrderUtil;
import com.ice.cj_ice.util.TtlPortFinder;

import NDKLoader.HitbotNDKLoader;
import android_serialport_api.SerialUtil;


public class TestActivity extends BaseActivity implements View.OnClickListener {

    private SegmentView mSegmentView,segmentViewShop;
    private RelativeLayout right, left;
    int MODEL = 0;
    HitbotNDKLoader robot;
    private CommomDialog commomDialog;
    private volatile  boolean TAG = false;
    private Button studyBtn,up,down,shortAgeBtn;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences,sp;
    private LoadingDialog loadingDialog;
    private int NUM = 5;
    private String rotate_platform;
    private TextView state_test;
    private String connectionState;
    private SerialUtil serialUtil;
    private int IS_INIT = 0;

    @Override
    protected void layoutId() {
        setContentView(R.layout.devices_test);
    }

    @Override
    protected void initView() {
        sp = App.activity.getSharedPreferences("shop_state", MODE_PRIVATE);
        shortAgeBtn = (Button) findViewById(R.id.shortage);
        state_test = (TextView) findViewById(R.id.server_State);
        up = (Button) findViewById(R.id.up_arm);
        down = (Button) findViewById(R.id.down_arm);
        studyBtn = (Button) findViewById(R.id.studyBtn);
        right = (RelativeLayout) findViewById(R.id.r_right);
        left = (RelativeLayout) findViewById(R.id.r_left);
        mSegmentView = (SegmentView) findViewById(R.id.segmentview);
        segmentViewShop = (SegmentView) findViewById(R.id.segmentview_shop);
        int state = sp.getInt("shop_state", 1);
        int shopState = getShopState(state);
        if(shopState == 0){
            segmentViewShop.setSegmentText("正常",0);
            segmentViewShop.setSegmentText("暂停服务",1);
        }else {
            segmentViewShop.setSegmentText("正常",1);
            segmentViewShop.setSegmentText("暂停服务",0);
        }
        mSegmentView.setSegmentText("部件测试", 0);
        mSegmentView.setSegmentText("机械臂测试", 1);

    }


    private int getShopState(int STATE){
        int df = 1;
        switch (STATE){
            case 0:
                df = 0;
                state_test.setText("正在售卖");
                break;
            case 1:
                df = 1;
                state_test.setText("暂停服务");
                break;
            case 2:
                df = 1;
                state_test.setText("网络无连接");
                break;
            case 3:
                df = 1;
                state_test.setText("机械臂未连接");
                break;
            case 4:
                df = 1;
                state_test.setText("无坐标");
                break;
            case 5:
                df = 1;
                state_test.setText("缺料");
                break;
            case 400:
                df = 1;
                state_test.setText("暂无结果");
                break;
        }
        return df;
    }

    @Override
    protected void initData() {
        //初始化机械臂
        robot = ArmUtil.getArmUtil();
        sharedPreferences = App.activity.getSharedPreferences("location", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadingDialog = new LoadingDialog(this);
        TtlPortFinder ttlPortFinder = TtlPortFinder.getTtlPortFinder();
        serialUtil = ttlPortFinder.initSerial();
    }



    @Override
    protected void initListener() {
        /*segmentViewShop.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View view, int postion) {
                switch (postion) {
                    case 0:
                        STATE = 0;
                        break;
                    case 1:
                        STATE = 1;
                        break;
                    default:
                        break;
                }
            }
        });*/

        mSegmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {

            @Override
            public void onSegmentViewClick(View view, int postion) {
                switch (postion) {
                    case 0:
                        loadBujianTest();
                        break;
                    case 1:
                        arm_test();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void loadData() {
        loadBujianTest();
    }


    @Override
    protected void in(Class tClass) {

    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 部件测试
     */
    public void loadBujianTest(){
        final Dialog show1 = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "连接中...", false, false, true).show();
        MODEL = 0;
        right.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectionState = TtlPortFinder.getConnectionState();
                DialogUIUtils.dismiss(show1);
            }
        }).start();
        if ("00".equals(connectionState)) {
            right.setVisibility(View.GONE);
            left.setVisibility(View.VISIBLE);
            Toast.makeText(TestActivity.this, "连接下位机成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TestActivity.this, "连接下位机失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 机械臂测试
     */
    public void arm_test(){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "连接中...", false, false, true).show();
        left.setVisibility(View.GONE);
        right.setVisibility(View.GONE);
        //查看机械臂是否连接
        int armIsconnect = ArmUtil.arm_isconnect();
        if(armIsconnect == 1){
            MODEL = 1;
            DialogUIUtils.dismiss(show);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    right.setVisibility(View.VISIBLE);
                    Toast.makeText(TestActivity.this, "机械臂连接成功",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            DialogUIUtils.dismiss(show);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TestActivity.this, "机械臂连接失败",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    /**
     * 落杯器
     * @param view
     */
    public void drop_cup (View view)  {
        Log.d("xuezhiyuan",TAG+"===================");
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.shipment(5, 0);
                    if(result == 1){
                       DialogUIUtils.dismiss(show);
                       changeText("成功");
                    }else {
                       DialogUIUtils.dismiss(show);
                       changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String dropCup = sharedPreferences.getString("drop_cup", null);
            //  angle1  angle2  z  r  speed  rough
            //  x   y   z   angle1  angle2   r
            if(dropCup == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.drop_location(dropCup,100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","drop_cup");
        }else {
            //请切换模式
            DialogUIUtils.dismiss(show);
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 冰淇淋机左料筒
     * @param view
     */
    public void ice_left (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        int result = ArmUtil.send_ice(0);
                        if (result == 1) {
                            DialogUIUtils.dismiss(show);
                            changeText("成功");
                        } else {
                            DialogUIUtils.dismiss(show);
                            changeText("失败");
                        }
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到冰淇淋机左料筒
            final String ice_left = sharedPreferences.getString("ice_left", null);
            if(ice_left == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(ice_left,100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","ice_left");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 冰淇淋机中间料筒
     * @param view
     */
    public void ice_middle (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_ice(2);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String ice_middle = sharedPreferences.getString("ice_middle", null);
            if(ice_middle == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(ice_middle,100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
                }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","ice_middle");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 冰淇淋机右料筒
     * @param view
     */
    public void ice_right (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_ice(1);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String ice_right = sharedPreferences.getString("ice_right", null);
            if(ice_right == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(ice_right, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","ice_right");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配料桶1
     * @param view
     */
    public void blank_one (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(3);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到配料桶1
            final String blank_one = sharedPreferences.getString("blank_one", null);
            if(blank_one == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_one, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_one");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 配料桶2
     * @param view
     */
    public void blank_two (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(4);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到配料桶2
            final String blank_two = sharedPreferences.getString("blank_two", null);
            if(blank_two == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_two, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_two");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配料桶3
     * @param view
     */
    public void blank_three (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            //部件测试
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(5);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String blank_three = sharedPreferences.getString("blank_three", null);
            if(blank_three == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_three, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_three");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配料桶4
     * @param view
     */
    public void blank_four (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(6);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String blank_four = sharedPreferences.getString("blank_four", null);
            if(blank_four == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_four, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_four");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配料桶5
     * @param view
     */
    public void blank_five (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(7);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String blank_five = sharedPreferences.getString("blank_five", null);
            if(blank_five == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_five, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_five");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 配料桶6
     * @param view
     */
    public void blank_six (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int result = ArmUtil.send_bluck(8);
                    if(result == 1){
                        DialogUIUtils.dismiss(show);
                        changeText("成功");
                    }else {
                        DialogUIUtils.dismiss(show);
                        changeText("失败");
                    }
                }
            }).start();
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到落杯器
            final String blank_six = sharedPreferences.getString("blank_six", null);
            if(blank_six == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            synchronized (this) {
                                if (IS_INIT == 1) {
                                    getinitLocation();
                                    IS_INIT = 0;
                                }
                                ArmUtil.ice_location(blank_six, 100);
                                DialogUIUtils.dismiss(show);
                                dialogText("已完成", "回到初始化位置");
                            }
                        }
                    }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","blank_six");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 旋转台
     * @param view
     */
    public void rotate_platform (View view){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        if(MODEL == 0){
            DialogUIUtils.dismiss(show);
            changeText("无此功能");
        }else if(MODEL == 1 && TAG == false){
            //机械臂运动到旋转台
            final String rotate_platform = sharedPreferences.getString("rotate_platform", null);
            if(rotate_platform == null){
                DialogUIUtils.dismiss(show);
                Toast.makeText(TestActivity.this,"请示教此位置",Toast.LENGTH_SHORT).show();
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            if (IS_INIT == 1) {
                                getinitLocation();
                                IS_INIT = 0;
                            }
                            ArmUtil.platform_location(rotate_platform);
                            DialogUIUtils.dismiss(show);
                            dialogText("已完成", "回到初始化位置");
                        }
                    }
                }).start();
            }
        }else if(MODEL == 2 || TAG == true){
            DialogUIUtils.dismiss(show);
            dialogLocation("提示","是否设置此位置","rotate_platform");
        }else {
            DialogUIUtils.dismiss(show);
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 清洗冰淇淋机
     * @param view
     */
    public void ice_clear(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_CLEAR);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC5, (byte) 0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
    }

    /**
     * 打开所有冰淇淋机料口
     */
    public void all_open(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_OPEN);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC7, (byte) 0x00,(byte)0x01,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
    }

    /**
     * 关闭所有冰淇淋机料口
     */
    public void all_close(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_CLOSE);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xC7, (byte) 0x00,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
    }

    /**
     * 自动模式
     */
    public void auto_model(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.AUTO_MODEL);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC6,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
    }

    /**
     * 关闭自动
     * @param view
     */
    public void stop_auto(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.CLOSE_AUTO);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC8,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
    }


    /**
     * 复位机械臂
     * @param view
     */
    public void stop_arm (View view){
        if(MODEL == 0){
            //部件测试

        }else if(MODEL == 1){
            //复位机械臂
            final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "复位中...", false, false, true).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    robot.initial(5,160);
                    robot.unlock_position();
                    getinitLocation();
                    DialogUIUtils.dismiss(show);
                }
            }).start();
        }else {
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 手动示教
     * @param view
     */
    public void study (View view){
        //Toast.makeText(TestActivity.this,"落杯器转动",Toast.LENGTH_SHORT).show();
        if(MODEL == 0){
            //部件测试

        }else if(MODEL == 1 ||  MODEL == 2){
            //手动示教
            if(TAG == true){
                //关闭示教
                final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "示教关闭中", false, false, true).show();
                up.setVisibility(View.GONE);
                down.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        robot.set_drag_teach_state(false);
                        getinitLocation();
                        DialogUIUtils.dismiss(show);
                    }
                }).start();
                studyBtn.setText("开启\n示教");
                TAG = false;
                MODEL = 1;
                getinitLocation();
            }else {
                //开启示教
                //dialogLoading("是否开启示教","请将机械臂移动到指定位置,点击对挺位置图标");
                up.setVisibility(View.VISIBLE);
                down.setVisibility(View.VISIBLE);
                robot.set_drag_teach_state(true);
                studyBtn.setText("关闭\n示教");
                //回到初始化
                //getinitLocation();
                TAG = true;
                MODEL = 2;
            }
        }else {
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *   机械臂Z轴向上
     * @param view
     */
    public void arm_up(View view){
        if(MODEL == 0){
            //部件测试

        }else if(MODEL == 1 ||  MODEL == 2){
            float[] scara_param = robot.get_scara_param();
            if(scara_param[2] < -20){
                robot.movej_angle(scara_param[3], scara_param[4], scara_param[2]+NUM, scara_param[5], 100, 0);
            }
        }else {
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     *   机械臂Z轴向下
     * @param view
     */
    public void arm_down(View view){
        if(MODEL == 0){
            //部件测试

        }else if(MODEL == 1 ||  MODEL == 2){
            float[] scara_param = robot.get_scara_param();
            if(scara_param[2] > -150){
                robot.movej_angle(scara_param[3], scara_param[4], scara_param[2]-NUM, scara_param[5], 100, 0);
            }
        }else {
            //请切换模式
            Toast.makeText(TestActivity.this,"请切换模式",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *  一键测试
     * @param view
     */
    public void test_arm(View view) throws InterruptedException {
        //获取落杯器位置
        final String dropCup =  sharedPreferences.getString("drop_cup", null);
        final String ice_right = sharedPreferences.getString("ice_right", null);
        final String blank_one = sharedPreferences.getString("blank_one", null);
        rotate_platform = sharedPreferences.getString("rotate_platform", null);
        final String blank_six = sharedPreferences.getString("blank_six", null);
        if(dropCup == null && ice_right == null && blank_one == null && rotate_platform == null && blank_six == null){
            Toast.makeText(TestActivity.this,"请检测是否示教完成",Toast.LENGTH_SHORT).show();
        }else {
            synchronized (this) {
                final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "测试中", false, false, true).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //运动到初始化位置
                        getinitLocation();
                        //运动到落杯器位置
                        ArmUtil.drop_location(dropCup,100);
                        //运动到初始化位置
                        getinitLocation();
                        //运动到接冰淇淋位置
                        ArmUtil.ice_location(ice_right,100);
                        //运动到接配料位置
                        ArmUtil.blunk_location(blank_one,30);
                        //运动到旋转台位置
                        ArmUtil.platform_location(rotate_platform);
                        //运动到初始化位置
                        getinitLocation();
                        DialogUIUtils.dismiss(show);
                    }
                }).start();
            }
        }
    }


    /**
     * 弹出框
     * @param title    标题
     * @param content  加载中
     */
    private void dialogLoading(final String title, String content){
        commomDialog = new CommomDialog(this, R.style.dialog, content, new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                 if(confirm == true){
                     getinitLocation();
                     dialog.dismiss();
                 }else {
                     IS_INIT = 1;
                     dialog.dismiss();
                 }
            }
        });
        commomDialog.setTitle(title).show();
    }



    /**
     *
     * @param title    标题
     * @param content  加载中
     */
    private void dialogLocation(final String title, String content, final String location){
        commomDialog = new CommomDialog(this, R.style.dialog, content, new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if(confirm == true){
                    float[] scara_param = robot.get_scara_param();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < scara_param.length; i++) {
                        stringBuilder.append(scara_param[i] + " ");
                    }
                    String data = stringBuilder.toString();
                    editor.putString(location,data);
                    editor.commit();
                    dialog.dismiss();
                }else {
                    dialog.dismiss();
                }
            }
        });
        commomDialog.setTitle(title).show();
    }



    /**
     * String 转  FLOAT
     * @param data
     * @return
     */
    private float[] stringToFloat(String data){
        String[] strings = data.split(" ");
        float[] fs = new float[strings.length];
        for (int i = 0; i < strings.length; i++) {
            fs[i] = Float.parseFloat(strings[i]);
        }
        return fs;
    }




    /**
     * 进入售卖界面
     * @param view
     */
    public void go_shopping(View view){
        Intent intent = new Intent(this, BannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * 获取初始化位置
     */
    public void getinitLocation(){
        String dropCup = sharedPreferences.getString("drop_cup", null);
        if(dropCup == null){
            robot.movej_angle(46, -106, -83, -93, 100, 0);
            robot.wait_stop();
        }else {
            final float[] toFloat = stringToFloat(dropCup);
            robot.movej_angle(46, -106, toFloat[2], -93, 100, 0);
            robot.wait_stop();
        }


    }

    /**
     * 发送指令
     */
    public void send(final byte... args){
        final Dialog show = DialogUIUtils.showMdLoadingHorizontal(TestActivity.this, "运行中...", false, false, true).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] sendOrderUtil = SendOrderUtil.getSendOrderUtil(args);
                serialUtil.setData(sendOrderUtil);
                try {
                    String stm32 = readStm32();
                    Log.d("xuezhiyuan",stm32);
                    if (stm32.length() == 20) {
                        String begin = stm32.substring(12, 14);
                        final String result = TtlPortFinder.returnResult(begin);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("00".equals(result)) {
                                    Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                } else if ("a1".equals(result)) {
                                    Toast.makeText(TestActivity.this, "包头包尾错误", Toast.LENGTH_SHORT).show();
                                } else if ("a2".equals(result)) {
                                    Toast.makeText(TestActivity.this, "缓存中数据长度大于数据协议长度位定义", Toast.LENGTH_SHORT).show();
                                } else if ("a3".equals(result)) {
                                    Toast.makeText(TestActivity.this, "CRC校验错误", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TestActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    DialogUIUtils.dismiss(show);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 复位落杯器
     * @param view
     */
    public void ice_isShortage(View view){
        byte[] crcByteValue = CRC16.getCRCByteValue(ParamsSettingUtil.ICE_CUP_RESET);
        //send((byte) 0x4D, (byte) 0x4A, (byte) 0x09, (byte) 0x00, (byte) 0xC1,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        send((byte) 0x4D, (byte) 0x4A, (byte) 0x0A, (byte) 0x00, (byte) 0xE1,(byte)0x00,(byte)0x00,crcByteValue[0], crcByteValue[1], (byte) 0xEF);
        String readStm32 = readStm32();
        if(readStm32.length() == 20){
            String result = readStm32.substring(12, 14);
            if("00".equals(result)){
                Toast.makeText(TestActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(TestActivity.this, "失败", Toast.LENGTH_SHORT).show();
        }
    }

    //重复读取下位机返回指令
    public String readStm32(){
        try {
            for(int i = 0;i<10 ;i++){
                byte[] dataByte = serialUtil.getDataByte();
                String hexString = serialUtil.bytesToHexString(dataByte, dataByte.length);
                if("3f".equals(hexString)){
                    Thread.sleep(800);
                }else {
                    //已收到结果
                    return hexString;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "3f";
    }


    /**
     * 更新文本内容
     * @param text
     */
    private void changeText(final String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TestActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 更新
     * @param
     */
    private void dialogText(final String title,final  String content){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogLoading(title,content);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robot.closeServer();
        System.exit(1);
    }
}

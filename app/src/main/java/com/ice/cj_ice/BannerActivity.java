package com.ice.cj_ice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.leyaoyao.SimpleMsgHandler;
import com.ice.cj_ice.leyaoyao.eventbus.PayResultEvent;
import com.ice.cj_ice.leyaoyao.eventbus.WifiStateEvent;
import com.ice.cj_ice.util.ArmUtil;
import com.ice.cj_ice.util.LeyaoyaoUtil;
import com.ice.cj_ice.util.Params;
import com.ice.cj_ice.util.ShipMentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.lyy.netty.client.NettyClient;

/**
 * Created by Administrator on 2019/1/20.
 */

public class BannerActivity extends BaseActivity {

    private SharedPreferences sharedPreferences,sp_shop_state;
    private SharedPreferences.Editor editor;
    private VideoView video;
    MediaController mMediaController;
    private Button clickBtn;
    final long[] mHints =  new long[4];
    private int state = 1;
    private ShipMentUtil shipMentUtil;


    @Override
    protected void layoutId() {
        setContentView(R.layout.banner_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void initView() {
        sp_shop_state = getSharedPreferences("shop_state", MODE_PRIVATE);
        editor = sp_shop_state.edit();
        clickBtn = (Button) findViewById(R.id.clickBtn);
        video = (VideoView) findViewById(R.id.video);
    }

    @Override
    protected void initData() {
        sharedPreferences = getSharedPreferences("loginState", MODE_PRIVATE);
        shipMentUtil = new ShipMentUtil();

        if (!TextUtils.isEmpty("/storage/emulated/0/Movies/"+"iop.mp4")) {
            this.video.setVideoPath("/storage/emulated/0/Movies/"+"iop.mp4");
            this.video.setMediaController(mMediaController);
            this.video.seekTo(0);
            this.video.requestFocus();
            this.video.start();
        }
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    /***
     * 判断网络状态
     * @param event
     */
    @Subscribe
    public void onEvent(WifiStateEvent event) {
        int net_state = event.getState();
        if(net_state == 1){
            //连接乐摇摇
            LeyaoyaoUtil.getLeyaoyaoUtil();
        }else {
            clickBtn.setVisibility(View.GONE);
            //返回数据到测试界面
            state = 2;
        }
    }

    //点击购买按钮
    public void fab(View view) {
        String result = sharedPreferences.getString("D", null);
        if("1".equals(result)){
            Intent intent = new Intent(this, BindActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if("0".equals(result)) {
            Intent intent = new Intent(this, ShopActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            clickBtn.setVisibility(View.GONE);
            state = 400;
            Log.d("xuezhiyuan","无数据");
        }
    }

    @Override
    protected void loadData() {
        //机械臂是否连接
        int armIsconnect = ArmUtil.arm_isconnect();
        Log.d("xuezhiyuan","机械臂状态"+armIsconnect);
        if(armIsconnect == 1) {
            int all_location = shipMentUtil.get_all_location();
            if (all_location == 200) {
                //查询是否缺料
                int missingDetection = ArmUtil.shipment(8,0);
                if(missingDetection == 1){
                    state = 0;
                    clickBtn.setText("点 击"+"\n"+"购 买");
                }else {
                    state = 5;
                    clickBtn.setVisibility(View.GONE);
                    ArmUtil.shipment(13, 0);
                }
            }else {
                state = 4;
                //无坐标
                clickBtn.setVisibility(View.GONE);
            }
        }else {
            state = 3;
            //未连接机械臂
            clickBtn.setVisibility(View.GONE);
        }
    }



    public void top_btn(View view){
        //              将mHints数组内的所有元素左移一个位置
        System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
        //获得当前系统已经启动的时间
        mHints[mHints.length - 1] = SystemClock.uptimeMillis();
        if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("xuezhiyuan","传递状态值"+state);
            //intent.putExtra("state",state);
            editor.putInt("shop_state",state);
            editor.commit();
            startActivity(intent);
        }
    }


    @Override
    protected void in(Class tClass) {

    }
}

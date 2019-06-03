package com.ice.cj_ice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
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
import com.ice.cj_ice.util.ArmUtil;
import com.ice.cj_ice.util.Params;
import com.ice.cj_ice.util.ShipMentUtil;
import cn.lyy.netty.client.NettyClient;

/**
 * Created by Administrator on 2019/1/20.
 */

public class BannerActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private VideoView video;
    MediaController mMediaController;
    private Button clickBtn;
    final long[] mHints =  new long[4];
    private int state;
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
        clickBtn = (Button) findViewById(R.id.clickBtn);
        video = (VideoView) findViewById(R.id.video);
    }

    @Override
    protected void initData() {
        state = getIntent().getIntExtra("state", 2);
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

    //点击购买按钮
    public void fab(View view) {
        int arm_isconnect = ArmUtil.arm_isconnect();
        if(arm_isconnect == 1){
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
                Log.d("xuezhiyuan","无数据");
                NettyClient nettyClient = new NettyClient(Params.appid,Params.appSecret,Params.host,Params.port,Params.uuid,"",new SimpleMsgHandler());
                nettyClient.connect();
            }
        }else {
            clickBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        int armIsconnect = ArmUtil.arm_isconnect();
        if(armIsconnect == 1){
            int all_location = shipMentUtil.get_all_location();
            if(all_location == 200){
                if(state == 1){
                    clickBtn.setVisibility(View.GONE);
                }else if(state == 0){
                    clickBtn.setVisibility(View.VISIBLE);
                    clickBtn.setText("点 击"+"\n"+"购 买");
                }else {
                    clickBtn.setVisibility(View.GONE);
                    NettyClient nettyClient = new NettyClient(Params.appid,Params.appSecret,Params.host,Params.port,Params.uuid,"",new SimpleMsgHandler());
                    nettyClient.connect();
                }
            }
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
            startActivity(intent);
        }
    }


    @Override
    protected void in(Class tClass) {

    }
}

package com.ice.cj_ice;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.util.MyTimeTask;
import com.ice.cj_ice.util.UpIceInfoUtil;
import com.ice.cj_ice.util.ZxingUtils;

import java.util.TimerTask;


public class BindActivity extends BaseActivity {

    private static final int TIMER = 999;
    private MyTimeTask task;
    private ImageView imageView;
    private TextView textView;
    private SharedPreferences sharedPreferences;


    @Override
    protected void layoutId() {
        setContentView(R.layout.activity_bind_device);
    }

    @Override
    protected void initView() {
        sharedPreferences = getSharedPreferences("loginState", MODE_PRIVATE);
        imageView = (ImageView) findViewById(R.id.imageEr);
        textView = (TextView) findViewById(R.id.deviceId);
    }

    @Override
    protected void initData() {
        //上传冰淇淋机货道信息   1-9   5配  5主   1杯
        UpIceInfoUtil.getInstance().upCargoInfo();
        UpIceInfoUtil.getInstance().upShopInfo();
    }


    @Override
    protected void initListener() {

    }


    private void setTimer(){
        Log.d("xuezhiyuan","task");
        task =new MyTimeTask(1000, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TIMER);
                //或者发广播，启动服务都是可以的
            }
        });
        task.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIMER:
                    //在此执行定时操作   获取服务器返回的绑定数据
                    String a = sharedPreferences.getString("A", null);
                    if("b".equals(a)){
                        in(ShopActivity.class);
                        UpIceInfoUtil.getInstance().upCargoSet();
                        stopTimer();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void loadData() {
        String v = sharedPreferences.getString("V", null);
        String q = sharedPreferences.getString("Q", null);
        textView.setText("设备ID："+v);
        Bitmap bitmap = ZxingUtils.createBitmap(q);
        imageView.setImageBitmap(bitmap);
        setTimer();
    }

    private void stopTimer(){
        task.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    @Override
    protected void in(Class tClass) {
        Intent intent = new Intent(this, tClass);
        intent.putExtra("state",1);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

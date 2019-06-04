package com.ice.cj_ice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.leyaoyao.OpenNettyDemo;
import com.ice.cj_ice.leyaoyao.eventbus.PayResultEvent;
import com.ice.cj_ice.leyaoyao.eventbus.QcodeEvent;
import com.ice.cj_ice.model.db.DBManager;
import com.ice.cj_ice.model.entity.CargoInfoBean;
import com.ice.cj_ice.util.BitmapUtils;
import com.ice.cj_ice.util.CreateUserDialog;
import com.ice.cj_ice.util.ZxingUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout r4,r5,r6,r7,r8;
    private RelativeLayout r1, r2, r3;
    private TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8;
    private Button ok;
    private CreateUserDialog createUserPopWin;
    Timer mTimer;
    private TimerTask mTimerTask;
    int recLen = 90;
    private DBManager dbManager;
    private List<CargoInfoBean> queryAll;
    String info;
    private String qcode = "000000";
    private int TAG = 1001;
    private int FLAG = 0;
    private List<String> price;
    private List<String> shopName;
    private List<String> cs;


    @Override
    protected void layoutId() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView7 = (TextView) findViewById(R.id.textView7);
        textView8 = (TextView) findViewById(R.id.textView8);

        imageView1 = (ImageView) findViewById(R.id.image_1);
        imageView2 = (ImageView) findViewById(R.id.image_2);
        imageView3 = (ImageView) findViewById(R.id.image_3);
        imageView4 = (ImageView) findViewById(R.id.image_4);
        imageView5 = (ImageView) findViewById(R.id.image_5);
        imageView6 = (ImageView) findViewById(R.id.image_6);
        imageView7 = (ImageView) findViewById(R.id.image_7);
        imageView8 = (ImageView) findViewById(R.id.image_8);

        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);
        r3 = (RelativeLayout) findViewById(R.id.r3);
        r4 = (LinearLayout) findViewById(R.id.r4);
        r5 = (LinearLayout) findViewById(R.id.r5);
        r6 = (LinearLayout) findViewById(R.id.r6);
        r7 = (LinearLayout) findViewById(R.id.r7);
        r8 = (LinearLayout) findViewById(R.id.r8);
        ok = (Button) findViewById(R.id.okBtn);
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

    @Override
    protected void initData() {
        price = new ArrayList<>();
        shopName = new ArrayList<>();
        cs = new ArrayList<>();
        dbManager = new DBManager(this);
        createUserPopWin = new CreateUserDialog(this, R.style.DialogTheme, onClickListener);
    }

    @Override
    protected void initListener() {
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r6.setOnClickListener(this);
        r7.setOnClickListener(this);
        r8.setOnClickListener(this);
        ok.setOnClickListener(this);
    }


    @Override
    protected void loadData() {
        if(dbManager.query().size() == 0){
            ToastDialog("数据库无数据");
        }else {
            queryAll = dbManager.query();
            for (CargoInfoBean data:queryAll){
                String shop_price = data.getShop_price();
                String shop_name = data.getShop_name();
                String channel_surplus = data.getChannel_surplus();
                if("0".equals(channel_surplus) || channel_surplus == null || "".equals(channel_surplus)){
                    channel_surplus = "0";
                }
                if("".equals(shop_price) || shop_price == null){
                    shop_price = "暂无数据";
                }
                if("".equals(shop_name) || shop_name == null){
                    shop_name = "暂无数据";
                }
                price.add(shop_price);
                shopName.add(shop_name);
                cs.add(channel_surplus);
            }
            for(int i = 0;i<cs.size();i++){
                String kucun = cs.get(i);
                if("0".equals(kucun)){
                    if(i+1 == 1){
                        r1.setClickable(false);
                        r1.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 2){
                        r2.setClickable(false);
                        r2.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 3){
                        r3.setClickable(false);
                        r3.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 4){
                        r4.setClickable(false);
                        r4.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 5){
                        r5.setClickable(false);
                        r5.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 6){
                        r6.setClickable(false);
                        r6.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 7){
                        r7.setClickable(false);
                        r7.setBackgroundResource(R.drawable.shop_bg);
                    }else if(i+1 == 8){
                        r8.setClickable(false);
                        r8.setBackgroundResource(R.drawable.shop_bg);
                    }
                }
            }
            textView1.setText(shopName.get(0));
            textView2.setText(shopName.get(1));
            textView3.setText(shopName.get(2));
            textView4.setText(shopName.get(3));
            textView5.setText(shopName.get(4));
            textView6.setText(shopName.get(5));
            textView7.setText(shopName.get(6));
            textView8.setText(shopName.get(7));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r1:
                int x = changeStateIce("X");
                TAG = x;
                //货道编号
                payShop();
                break;
            case R.id.r2:
                int y = changeStateIce("Y");
                TAG = y;
                payShop();
                break;
            case R.id.r3:
                int z = changeStateIce("Z");
                TAG = z;
                payShop();
                break;
            case R.id.r4:
                nullState();
                changeState(imageView4,0);
                payShop();
                break;
            case R.id.r5:
                nullState();
                changeState(imageView5,1);
                payShop();
                break;
            case R.id.r6:
                nullState();
                changeState(imageView6,2);
                payShop();
                break;
            case R.id.r7:
                nullState();
                changeState(imageView7,3);
                payShop();
                break;
            case R.id.r8:
                nullState();
                changeState(imageView8,4);
                payShop();
                break;
            case R.id.okBtn:
                pay();
                break;
        }
    }



    //获取二维码
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(QcodeEvent event) throws IOException, WriterException {
        qcode = event.getQcode();
        if(qcode != null){
            Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.drawable.icon);
            Bitmap code = BitmapUtils.createCode(App.activity, qcode,logo);
            createUserPopWin.imageView.setImageBitmap(code);
        }else {
            Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.drawable.icon);
            Bitmap code = BitmapUtils.createCode(App.activity, "暂无二维码信息，请查看网络",logo);
            createUserPopWin.imageView.setImageBitmap(code);
        }
    }

    public void initsTimer() {
        //计数器，每次减一秒。
        mTimerTask = new TimerTask() {
              @Override
              public void run() {
                  if (recLen == 0) {
                      recLen = 90;
                  } else {
                      //计数器，每次减一秒。
                      recLen -= 1;
                  }
                  Message message = new Message();
                  message.what = 0;
                  message.obj = recLen;
                  mHandler.sendMessage(message);
              }
          };
        mTimer = new Timer();
    }


    //实现更新主线程UI
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int sRecLen = (int) msg.obj;
                    createUserPopWin.textView.setText("倒计时"+recLen+"s");
                    //如果当前时间等于0时，则运行结束。
                    if (sRecLen <= 0) {
                        mTimer.cancel();
                        recLen = 0;
                        createUserPopWin.dismiss();
                    }
                    break;
            }
        }
    };


    public void pay(){
        //closeBtn
        destroyTimer();
        recLen = 90;
        createUserPopWin.dismiss();
        //pay
        String payShop = payShop();
        info = payShop;
        if(payShop == null || "".equals(payShop)){
            ToastDialog("请选择主料");
        }else {
            OpenNettyDemo.getPayCode(Arrays.asList(payShop));
            createUserPopWin.show();
            Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.drawable.icon);
            Bitmap code = null;
            try {
                code = BitmapUtils.createCode(App.activity, "暂无二维码信息，网络无连接。不可购买",logo);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            createUserPopWin.imageView.setImageBitmap(code);
            initsTimer();
            mTimer.schedule(mTimerTask, 1000, 1000);
        }
    }

    @Override
    protected void in(Class tClass) {
        Intent intent = new Intent(this, tClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             switch (v.getId()){
                 case R.id.closeBtn:
                     destroyTimer();
                     recLen = 90;
                     createUserPopWin.dismiss();
                     break;
             }
        }
    };


    @Subscribe
    public void onEvent(PayResultEvent event) {
        String result = event.getResult();
        Log.d("qianqian",result+"-----------------------");
        destroyTimer();
        Intent intent = new Intent(this, PayActivity.class);
        Bundle bundle = new Bundle();
        if(info.length() > 6){
            bundle.putInt("location_z",Integer.parseInt(info.substring(0, 1)));
            bundle.putInt("location_p",Integer.parseInt(info.substring(6, 7)));
            bundle.putString("info", info);
            bundle.putString("key",result);
        }else {
            bundle.putInt("location_z",Integer.parseInt(info.substring(0, 1)));
            bundle.putString("info", info);
            bundle.putString("key",result);
        }
        intent.putExtra("message",bundle);
        startActivity(intent);
        finish();
    }

    private void changeState(ImageView imageView,int TAG){

        switch (TAG){
            case 0:
                if(FLAG != 1){
                    imageView.setImageResource(R.drawable.two_check);
                    FLAG = 1;
                }else {
                    FLAG = 0;
                }
                break;
            case 1:
                if(FLAG != 2){
                    imageView.setImageResource(R.drawable.two_check);
                    FLAG = 2;
                }else {
                    FLAG = 0;
                }
                break;
            case 2:
                if(FLAG != 3){
                    imageView.setImageResource(R.drawable.two_check);
                    FLAG = 3;
                }else {
                    FLAG = 0;
                }
                break;
            case 3:
                if(FLAG != 4) {
                    imageView.setImageResource(R.drawable.two_check);
                    FLAG = 4;
                }else {
                    FLAG = 0;
                }
                break;
            case 4:
                if(FLAG != 5){
                    imageView.setImageResource(R.drawable.two_check);
                    FLAG = 5;
                }else {
                    FLAG = 0;
                }
                break;
        }
    }


    public void destroyTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //改变状态Ice
    private int changeStateIce(String s){
        if("X".equals(s)){
            imageView1.setBackgroundResource(R.drawable.shape_ring);
            imageView2.setBackgroundResource(R.drawable.null_shape);
            imageView3.setBackgroundResource(R.drawable.null_shape);
            textView1.setTextColor(Color.parseColor("#fffe6986"));
            textView2.setTextColor(Color.parseColor("#ffffff"));
            textView3.setTextColor(Color.parseColor("#ffffff"));
            return 10;
        }else if("Y".equals(s)) {
            imageView2.setBackgroundResource(R.drawable.shape_ring);
            imageView1.setBackgroundResource(R.drawable.null_shape);
            imageView3.setBackgroundResource(R.drawable.null_shape);
            textView2.setTextColor(Color.parseColor("#fffe6986"));
            textView1.setTextColor(Color.parseColor("#ffffff"));
            textView3.setTextColor(Color.parseColor("#ffffff"));
            return 100;
        }else if("Z".equals(s)){
            imageView3.setBackgroundResource(R.drawable.shape_ring);
            imageView2.setBackgroundResource(R.drawable.null_shape);
            imageView1.setBackgroundResource(R.drawable.null_shape);
            textView3.setTextColor(Color.parseColor("#fffe6986"));
            textView2.setTextColor(Color.parseColor("#ffffff"));
            textView1.setTextColor(Color.parseColor("#ffffff"));
            return 1000;
        }else {
            imageView1.setBackgroundResource(R.drawable.null_shape);
            imageView2.setBackgroundResource(R.drawable.null_shape);
            imageView3.setBackgroundResource(R.drawable.null_shape);
            textView3.setTextColor(Color.parseColor("#ffffff"));
            textView2.setTextColor(Color.parseColor("#ffffff"));
            textView1.setTextColor(Color.parseColor("#ffffff"));
            return 1001;
        }
    }

    /**
     * 配料状态改为取消选中
     */
    private void nullState(){
        imageView4.setImageResource(R.drawable.null_shape);
        imageView5.setImageResource(R.drawable.null_shape);
        imageView6.setImageResource(R.drawable.null_shape);
        imageView7.setImageResource(R.drawable.null_shape);
        imageView8.setImageResource(R.drawable.null_shape);
    }


    private void ToastDialog(String info){
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();
    }


    private String payShop(){
        if(FLAG != 0){
            if(TAG == 10){
                ok.setText("￥"+(getPrice(0)+getPrice(FLAG+2)));
                //仓位   数量   总金额
                return 1 + "," + 1 + "," + price.get(0) + ";" + (FLAG+3) + "," + 1 + "," + price.get(FLAG+2);
            }else if(TAG == 100){
                ok.setText("￥"+(getPrice(1)+getPrice(FLAG+2)));
                return 2 + "," + 1 + "," + price.get(1) + ";" + (FLAG+3) + "," + 1 + "," + price.get(FLAG+2);
            }else if(TAG == 1000){
                ok.setText("￥"+(getPrice(2)+getPrice(FLAG+2)));
                return 3 + "," + 1 + "," + price.get(2) + ";" + (FLAG+3) + "," + 1 + "," + price.get(FLAG+2);
            }else {
                ok.setText("立即购买");
                return null;
            }
        }else {
            if(TAG == 10){
                ok.setText("￥"+getPrice(0));
                return 1 + "," + 1 + "," + price.get(0);
            }else if(TAG == 100){
                ok.setText("￥"+getPrice(1));
                return 2 + "," + 1 + "," + price.get(1);
            }else if(TAG == 1000){
                ok.setText("￥"+getPrice(2));
                return 3 + "," + 1 + "," + price.get(2);
            }else {
                ok.setText("请选择主料");
                return null;
            }
        }
    }

    private float getPrice(int cargoNum){
        float v = Float.parseFloat(price.get(cargoNum)) / 100;
        return v;
    }

}

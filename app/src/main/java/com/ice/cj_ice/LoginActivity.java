package com.ice.cj_ice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.base.BaseActivity;
import com.ice.cj_ice.util.Params;



public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private Button one,two,three,four,five,six,seven,eight,nine,zero,login;
    private EditText editPassword;
    volatile String result = "";
    private StringBuffer sb;


    @Override
    protected void layoutId() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView(){
        editPassword = (EditText) findViewById(R.id.edit_password);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        zero = (Button) findViewById(R.id.zero);
        login = (Button) findViewById(R.id.login);
    }

    @Override
    protected void initData() {
        sb = new StringBuffer();
    }

    @Override
    protected void initListener() {
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void in(Class tClass) {
        Intent intent = new Intent(this, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.one:
                clickBtn("1");
                break;
            case R.id.two:
                clickBtn("2");
                break;
            case R.id.three:
                clickBtn("3");
                break;
            case R.id.four:
                clickBtn("4");
                break;
            case R.id.five:
                clickBtn("5");
                break;
            case R.id.six:
                clickBtn("6");
                break;
            case R.id.seven:
                clickBtn("7");
                break;
            case R.id.eight:
                clickBtn("8");
                break;
            case R.id.nine:
                clickBtn("9");
                break;
            case R.id.zero:
                clickBtn("0");
                break;
            case R.id.login:
                sb.delete(0,sb.length());
                result = editPassword.getText().toString();
                if (result == null || "".equals(result)) {
                    editPassword.setText("");
                    editPassword.setHint("请输入设备管理员密码");
                    editPassword.setHintTextColor(Color.parseColor("#ff0000"));
                } else {
                    Log.d("xuezhiyuan",result);
                    if (Params.PASSWORD.equals(result)) {
                        editPassword.setText("");
                        in(App.class);
                    } else {
                        editPassword.setText("");
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                        result = null;
                    }
                }
                break;
        }
    }



    public void back(View view){
        Intent intent = new Intent(this, BannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("state",0);
        startActivity(intent);
    }

    private void clickBtn(String number){
        result = sb.append(number).toString();
        editPassword.setText(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

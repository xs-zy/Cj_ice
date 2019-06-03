package com.ice.cj_ice.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ice.cj_ice.R;


/**
 * Created by Administrator on 2019/4/9.
 */

public class CreateUserDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;
    public TextView textView;
    public ImageView imageView,imageViewClose;


    private View.OnClickListener mClickListener;

    public CreateUserDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public CreateUserDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.pay_dialog);
        imageView =  findViewById(R.id.imageEr);
        textView =  findViewById(R.id.timeId);
        imageViewClose = findViewById(R.id.closeBtn);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.3); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        // 为按钮绑定点击事件监听器
        imageView.setOnClickListener(mClickListener);
        imageViewClose.setOnClickListener(mClickListener);
        this.setCancelable(false);
    }
}

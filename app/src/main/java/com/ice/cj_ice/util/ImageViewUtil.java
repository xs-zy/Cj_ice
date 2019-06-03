package com.ice.cj_ice.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ice.cj_ice.R;

/**
 * Created by Administrator on 2019/5/17.
 */

public class ImageViewUtil extends RelativeLayout {

    private ImageView imageView;
    private TextView textView;

    public ImageViewUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.image_layout,this);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.textView);
    }

}

package com.ice.cj_ice.util;

import android.content.SharedPreferences;
import com.ice.cj_ice.base.App;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2019/5/24.
 */

public class ShipMentUtil {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    private String dropCup;
    private String ice_left;
    private String ice_middle;
    private String ice_right;
    private String blank_one;
    private String blank_two;
    private String blank_three;
    private String blank_four;
    private String blank_five;
    //private String blank_six;
    private String rotate_platform;


    //获取坐标
    public int get_all_location(){
        sharedPreferences = App.activity.getSharedPreferences("location", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dropCup = sharedPreferences.getString("drop_cup", null);
        ice_left = sharedPreferences.getString("ice_left", null);
        ice_middle = sharedPreferences.getString("ice_middle", null);
        ice_right = sharedPreferences.getString("ice_right", null);
        blank_one = sharedPreferences.getString("blank_one", null);
        blank_two = sharedPreferences.getString("blank_two", null);
        blank_three = sharedPreferences.getString("blank_three", null);
        blank_four = sharedPreferences.getString("blank_four", null);
        blank_five = sharedPreferences.getString("blank_five", null);
        //blank_six = sharedPreferences.getString("blank_six", null);
        rotate_platform = sharedPreferences.getString("rotate_platform", null);
        if(dropCup == null && ice_left == null && ice_middle == null
                && ice_right == null && blank_one == null && blank_two == null
                && blank_three == null && blank_four == null && blank_five == null
                && rotate_platform == null){
            return ErrorCode.null_location_error;
        }else {
            return ErrorCode.have_location_success;
        }
    }
}

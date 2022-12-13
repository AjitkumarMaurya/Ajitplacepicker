package com.ajit.pingplacepicker.galleryimagepicker.helper;

import android.app.Activity;

import com.ajit.pingplacepicker.galleryimagepicker.bean.PickerError;
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener;
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener2;

/**
 * Time: 2019/10/18 9:53
 * Author:ypx
 * Description: 调用选择器失败回调
 */
public class PickerErrorExecutor {

    public static void executeError(Activity activity, int code) {
        activity.setResult(code);
        activity.finish();
    }

    public static void executeError(OnImagePickCompleteListener listener, int code) {
        if (listener instanceof OnImagePickCompleteListener2) {
            ((OnImagePickCompleteListener2) listener).onPickFailed(PickerError.valueOf(code));
        }
    }
}

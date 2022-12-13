package com.ajit.pingplacepicker.galleryimagepicker.data;

import com.ajit.pingplacepicker.galleryimagepicker.bean.PickerError;

/**
 * Description: 图片选择器回调
 * <p>
 * Author: peixing.yang
 * Date: 2019/2/21
 */
public interface OnImagePickCompleteListener2 extends OnImagePickCompleteListener {
    void onPickFailed(PickerError error);
}

package com.ajit.pingplacepicker.galleryimagepicker.views;

import android.content.Context;

import com.ajit.pingplacepicker.galleryimagepicker.views.base.PickerControllerView;
import com.ajit.pingplacepicker.galleryimagepicker.views.base.PickerFolderItemView;
import com.ajit.pingplacepicker.galleryimagepicker.views.base.PickerItemView;
import com.ajit.pingplacepicker.galleryimagepicker.views.base.PreviewControllerView;
import com.ajit.pingplacepicker.galleryimagepicker.views.base.SingleCropControllerView;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXBottomBar;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXFolderItemView;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXItemView;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXPreviewControllerView;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXSingleCropControllerView;
import com.ajit.pingplacepicker.galleryimagepicker.views.wx.WXTitleBar;


/**
 * Time: 2019/10/27 22:22
 * Author:ypx
 * Description: 选择器UI提供类,默认为微信样式
 */
public class PickerUiProvider {


    public PickerControllerView getTitleBar(Context context) {
        return new WXTitleBar(context);
    }

    public PickerControllerView getBottomBar(Context context) {
        return new WXBottomBar(context);
    }


    public PickerItemView getItemView(Context context) {
        return new WXItemView(context);
    }


    public PickerFolderItemView getFolderItemView(Context context) {
        return new WXFolderItemView(context);
    }


    public PreviewControllerView getPreviewControllerView(Context context) {
        return new WXPreviewControllerView(context);
    }


    public SingleCropControllerView getSingleCropControllerView(Context context) {
        return new WXSingleCropControllerView(context);
    }
}

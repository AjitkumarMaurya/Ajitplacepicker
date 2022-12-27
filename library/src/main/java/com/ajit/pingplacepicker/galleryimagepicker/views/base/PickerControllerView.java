package com.ajit.pingplacepicker.galleryimagepicker.views.base;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;
import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageSet;
import com.ajit.pingplacepicker.galleryimagepicker.bean.selectconfig.BaseSelectConfig;

import java.util.ArrayList;

/**
 * Time: 2019/11/7 13:24
 * Author:ypx
 * Description: 选择器控制类
 */
public abstract class PickerControllerView extends PBaseLayout {

    public abstract int getViewHeight();


    public abstract View getCanClickToCompleteView();


    public abstract View getCanClickToIntentPreviewView();


    public abstract View getCanClickToToggleFolderListView();


    public abstract void setTitle(String title);


    public abstract void onTransitImageSet(boolean isOpen);


    public abstract void onImageSetSelected(ImageSet imageSet);


    public abstract void refreshCompleteViewState(ArrayList<ImageItem> selectedList, BaseSelectConfig selectConfig);

    public boolean isAddInParent() {
        return getViewHeight() > 0;
    }

    public PickerControllerView(Context context) {
        super(context);
    }

    public PickerControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PickerControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

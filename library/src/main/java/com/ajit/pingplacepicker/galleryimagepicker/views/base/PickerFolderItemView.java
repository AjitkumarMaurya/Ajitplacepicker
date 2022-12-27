package com.ajit.pingplacepicker.galleryimagepicker.views.base;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageSet;
import com.ajit.pingplacepicker.galleryimagepicker.presenter.IPickerPresenter;

/**
 * Time: 2019/11/13 14:39
 * Author:ypx
 * Description:自定义文件夹item
 */
public abstract class PickerFolderItemView extends PBaseLayout {


    public abstract int getItemHeight();


    public abstract void displayCoverImage(ImageSet imageSet, IPickerPresenter presenter);

    public abstract void loadItem(ImageSet imageSet);

    public PickerFolderItemView(Context context) {
        super(context);
    }

    public PickerFolderItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PickerFolderItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

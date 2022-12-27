package com.ajit.pingplacepicker.galleryimagepicker.views.base;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ajit.pingplacepicker.galleryimagepicker.widget.cropimage.CropImageView;

public abstract class SingleCropControllerView extends PBaseLayout {

    /**
     * 设置状态栏
     */
    public abstract void setStatusBar();

    public abstract View getCompleteView();


    public abstract void setCropViewParams(CropImageView cropImageView, MarginLayoutParams params);

    public SingleCropControllerView(Context context) {
        super(context);
    }

    public SingleCropControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleCropControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

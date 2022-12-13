package com.ajit.pingplacepicker.galleryimagepicker.data;

import androidx.annotation.Nullable;

import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;

public interface ICameraExecutor {

    void takePhoto();

    void takeVideo();

    void onTakePhotoResult(@Nullable ImageItem imageItem);
}

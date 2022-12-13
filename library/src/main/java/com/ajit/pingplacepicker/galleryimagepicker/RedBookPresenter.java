package com.ajit.pingplacepicker.galleryimagepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.ajit.pingplacepicker.R;
import com.ajit.pingplacepicker.galleryimagepicker.adapter.PickerItemAdapter;
import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;
import com.ajit.pingplacepicker.galleryimagepicker.bean.selectconfig.BaseSelectConfig;
import com.ajit.pingplacepicker.galleryimagepicker.data.ICameraExecutor;
import com.ajit.pingplacepicker.galleryimagepicker.data.IReloadExecutor;
import com.ajit.pingplacepicker.galleryimagepicker.data.ProgressSceneEnum;
import com.ajit.pingplacepicker.galleryimagepicker.presenter.IPickerPresenter;
import com.ajit.pingplacepicker.galleryimagepicker.utils.PViewSizeUtils;
import com.ajit.pingplacepicker.galleryimagepicker.views.PickerUiConfig;
import com.ajit.pingplacepicker.galleryimagepicker.views.redbook.RedBookUiProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RedBookPresenter implements IPickerPresenter {

    @Override
    public void displayImage(View view, ImageItem item, int size, boolean isThumbnail) {
        Object object = item.getUri() != null ? item.getUri() : item.path;

        Glide.with(view.getContext()).load(object).apply(new RequestOptions()
                .format(isThumbnail ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888))
                .override(isThumbnail ? size : Target.SIZE_ORIGINAL)
                .into((ImageView) view);
    }

    @NotNull
    @Override
    public PickerUiConfig getUiConfig(Context context) {
        PickerUiConfig uiConfig = new PickerUiConfig();
        uiConfig.setShowStatusBar(false);
        uiConfig.setThemeColor(ContextCompat.getColor(context, R.color.colorPrimary));
        uiConfig.setStatusBarColor(Color.WHITE);
        uiConfig.setPickerBackgroundColor(Color.WHITE);
        uiConfig.setFolderListOpenDirection(PickerUiConfig.DIRECTION_TOP);
        uiConfig.setFolderListOpenMaxMargin(PViewSizeUtils.dp(context, 200));
        uiConfig.setPickerUiProvider(new RedBookUiProvider());
        return uiConfig;
    }

    @Override
    public void tip(Context context, String msg) {
        if (context == null) {
            return;
        }
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void overMaxCountTip(Context context, int maxCount) {
        if (context == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Most selection " + maxCount + " Files");
        builder.setPositiveButton("ok",
                (dialogInterface, i) -> dialogInterface.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public DialogInterface showProgressDialog(@Nullable Activity activity, ProgressSceneEnum progressSceneEnum) {
        return null;
    }

    @Override
    public boolean interceptPickerCompleteClick(final Activity activity, final ArrayList<ImageItem> selectedList,
                                                BaseSelectConfig selectConfig) {
        return false;
    }

    @Override
    public boolean interceptPickerCancel(final Activity activity, ArrayList<ImageItem> selectedList) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        }

        activity.finish();


        return true;
    }

    @Override
    public boolean interceptItemClick(@Nullable Activity activity, ImageItem imageItem, ArrayList<ImageItem> selectImageList, ArrayList<ImageItem> allSetImageList, BaseSelectConfig selectConfig, PickerItemAdapter adapter, boolean isClickCheckBox, @Nullable IReloadExecutor reloadExecutor) {
        return false;
    }

    @Override
    public boolean interceptCameraClick(@Nullable Activity activity, ICameraExecutor takePhoto) {
        return false;
    }
}
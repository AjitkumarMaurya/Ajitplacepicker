package com.ajit.pingplacepicker.galleryimagepicker.views.base;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ajit.pingplacepicker.R;
import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;
import com.ajit.pingplacepicker.galleryimagepicker.bean.selectconfig.BaseSelectConfig;
import com.ajit.pingplacepicker.galleryimagepicker.helper.DetailImageLoadHelper;
import com.ajit.pingplacepicker.galleryimagepicker.presenter.IPickerPresenter;
import com.ajit.pingplacepicker.galleryimagepicker.utils.PViewSizeUtils;
import com.ajit.pingplacepicker.galleryimagepicker.views.PickerUiConfig;
import com.ajit.pingplacepicker.galleryimagepicker.widget.cropimage.CropImageView;

import java.util.ArrayList;

/**
 * Time: 2019/11/13 14:39
 * Author:ypx
 * Description:自定义预览页面
 */
public abstract class PreviewControllerView extends PBaseLayout {

    /**
     * 设置状态栏
     */
    public abstract void setStatusBar();

    /**
     * 初始化数据
     *
     * @param selectConfig 选择配置项
     * @param presenter    presenter
     * @param uiConfig     ui配置类
     * @param selectedList 已选中列表
     */
    public abstract void initData(BaseSelectConfig selectConfig, IPickerPresenter presenter,
                                  PickerUiConfig uiConfig, ArrayList<ImageItem> selectedList);


    public abstract View getCompleteView();


    public abstract void singleTap();


    public abstract void onPageSelected(int position, ImageItem imageItem, int totalPreviewCount);


    public PreviewControllerView(Context context) {
        super(context);
    }

    public PreviewControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public View getItemView(Fragment fragment, final ImageItem imageItem, IPickerPresenter presenter) {
        if (imageItem == null) {
            return new View(fragment.getContext());
        }

        RelativeLayout layout = new RelativeLayout(getContext());
        final CropImageView imageView = new CropImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setBounceEnable(true);
        imageView.enable();
        imageView.setShowImageRectLine(false);
        imageView.setCanShowTouchLine(false);
        imageView.setMaxScale(7.0f);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        layout.setLayoutParams(params);
        layout.addView(imageView);

        ImageView mVideoImg = new ImageView(getContext());
        mVideoImg.setImageDrawable(getResources().getDrawable(R.mipmap.picker_icon_video));
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(PViewSizeUtils.dp(getContext(), 80), PViewSizeUtils.dp(getContext(), 80));
        mVideoImg.setLayoutParams(params1);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(mVideoImg, params1);

        if (imageItem.isVideo()) {
            mVideoImg.setVisibility(View.VISIBLE);
        } else {
            mVideoImg.setVisibility(View.GONE);
        }

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageItem.isVideo()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(imageItem.getUri(), "video/*");
                    getContext().startActivity(intent);
                    return;
                }
                singleTap();
            }
        });
        DetailImageLoadHelper.displayDetailImage(false, imageView, presenter, imageItem);
        return layout;
    }
}

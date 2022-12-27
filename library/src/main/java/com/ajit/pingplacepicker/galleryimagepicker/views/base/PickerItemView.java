package com.ajit.pingplacepicker.galleryimagepicker.views.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ajit.pingplacepicker.galleryimagepicker.bean.ImageItem;
import com.ajit.pingplacepicker.galleryimagepicker.bean.selectconfig.BaseSelectConfig;
import com.ajit.pingplacepicker.galleryimagepicker.presenter.IPickerPresenter;

public abstract class PickerItemView extends PBaseLayout {

    public abstract View getCameraView(BaseSelectConfig selectConfig, IPickerPresenter presenter);

    public abstract View getCheckBoxView();


    public abstract void initItem(ImageItem imageItem, IPickerPresenter presenter, BaseSelectConfig selectConfig);


    public abstract void disableItem(ImageItem imageItem, int disableCode);


    public abstract void enableItem(ImageItem imageItem, boolean isChecked, int indexOfSelectedList);

    private RecyclerView.Adapter adapter;
    private int position;

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void notifyDataSetChanged() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public int getPosition() {
        return position;
    }

    public PickerItemView(Context context) {
        super(context);
    }

    public PickerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PickerItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

package com.ajit.pingplacepicker.pix.interfaces;

import android.view.View;

import com.ajit.pingplacepicker.pix.modals.Img;


public interface OnSelectionListener {
    void onClick(Img Img, View view, int position);

    void onLongClick(Img img, View view, int position);
}

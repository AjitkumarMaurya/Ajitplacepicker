package com.ajit.pingplacepicker.pix.utility.ui;

import com.ajit.pingplacepicker.pix.Pix;

public interface FastScrollStateChangeListener {

    /**
     * Called when fast scrolling begins
     */
    void onFastScrollStart(Pix fastScroller);

    /**
     * Called when fast scrolling ends
     */
    void onFastScrollStop(Pix fastScroller);
}

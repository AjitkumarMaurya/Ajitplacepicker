package com.ajit.pingplacepicker.pix.utility;

public class RegexUtil {
    private static final String GIF_PATTERN = "(.+?)\\.gif$";

    public boolean checkGif(String path) {
        return path.matches(GIF_PATTERN);
    }
}

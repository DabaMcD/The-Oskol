package org.khanacademy.theoskol;

import android.util.DisplayMetrics;
import android.view.WindowManager;

class Screen {
    static int width;
    static int height;

    static void setScreenDims(WindowManager wm) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }
}

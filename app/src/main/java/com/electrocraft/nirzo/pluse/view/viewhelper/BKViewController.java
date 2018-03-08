package com.electrocraft.nirzo.pluse.view.viewhelper;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by nirzo on 3/7/2018.
 */

public class BKViewController {

    public static ButterKnife.Action<View> ENABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(true);
        }
    };

    public static ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setEnabled(false);
        }
    };

    public static ButterKnife.Action<View> GONE = new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    public static ButterKnife.Action<View> VISIABLE=new ButterKnife.Action<View>() {
        @Override
        public void apply(@NonNull View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };
}

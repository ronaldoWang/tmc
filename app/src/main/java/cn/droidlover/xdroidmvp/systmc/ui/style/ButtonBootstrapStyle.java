package cn.droidlover.xdroidmvp.systmc.ui.style;

import android.content.Context;
import android.support.annotation.ColorInt;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

import cn.droidlover.xdroidmvp.systmc.R;


/**
 * Created by ronaldo on 2017/10/11.
 */

public class ButtonBootstrapStyle implements BootstrapBrand {

    @ColorInt
    private final int defaultFill;
    @ColorInt
    private final int defaultEdge;
    @ColorInt
    private final int defaultTextColor;
    @ColorInt
    private final int activeFill;
    @ColorInt
    private final int activeEdge;
    @ColorInt
    private final int activeTextColor;
    @ColorInt
    private final int disabledFill;
    @ColorInt
    private final int disabledEdge;
    @ColorInt
    private final int disabledTextColor;

    @SuppressWarnings("deprecation")
    public ButtonBootstrapStyle(Context context) {
        defaultFill = context.getResources().getColor(R.color.custom_button_default_fill);
        defaultEdge = context.getResources().getColor(R.color.custom_button_default_edge);
        defaultTextColor = context.getResources().getColor(R.color.white);
        activeFill = context.getResources().getColor(R.color.custom_button_active_fill);
        activeEdge = context.getResources().getColor(R.color.custom_button_active_edge);
        activeTextColor = context.getResources().getColor(R.color.white);
        disabledFill = context.getResources().getColor(R.color.custom_disabled_fill);
        disabledEdge = context.getResources().getColor(R.color.custom_disabled_edge);
        disabledTextColor = context.getResources().getColor(R.color.white);
    }

    @Override
    public int defaultFill(Context context) {
        return defaultFill;
    }

    @Override
    public int defaultEdge(Context context) {
        return defaultEdge;
    }

    @Override
    public int defaultTextColor(Context context) {
        return defaultTextColor;
    }

    @Override
    public int activeFill(Context context) {
        return activeFill;
    }

    @Override
    public int activeEdge(Context context) {
        return activeEdge;
    }

    @Override
    public int activeTextColor(Context context) {
        return activeTextColor;
    }

    @Override
    public int disabledFill(Context context) {
        return disabledFill;
    }

    @Override
    public int disabledEdge(Context context) {
        return disabledEdge;
    }

    @Override
    public int disabledTextColor(Context context) {
        return disabledTextColor;
    }

    @Override
    public int getColor() {
        return defaultFill;
    }
}

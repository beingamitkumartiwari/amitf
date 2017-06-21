package com.mtechno.freerechargeswipe.app_tour;

/**
 * Created by Ankita on 02-05-2015.
 */

import android.view.View;

public class CubeOutTransformer extends DevenBaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0f ? view.getWidth() : 0f);
        view.setPivotY(view.getHeight() * 0.5f);
        view.setRotationY(90f * position);
    }

    @Override
    public boolean isPagingEnabled() {
        return true;
    }

}
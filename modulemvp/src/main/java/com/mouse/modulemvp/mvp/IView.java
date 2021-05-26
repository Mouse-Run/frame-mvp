package com.mouse.modulemvp.mvp;

import android.os.Bundle;
import android.view.View;

/**
 * Created by mouse on 2016/12/29.
 */

public interface IView<P> {
    void bindUI(View rootView);

    void bindEvent();

    void initData(Bundle savedInstanceState);

    int getOptionsMenuId();

    int getLayoutId();

    boolean useEventBus();

    P newP();
}

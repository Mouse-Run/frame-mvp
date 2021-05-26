package com.mouse.modulemvp.router;

import android.app.Activity;

/**
 * Created by mouse on 2016/11/29.
 */

public interface RouterCallback {

    void onBefore(Activity from, Class<?> to);

    void onNext(Activity from, Class<?> to);

    void onError(Activity from, Class<?> to, Throwable throwable);

}

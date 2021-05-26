package com.mouse.modulemvp.event;

/**
 * Created by mouse on 2016/12/22.
 */

public interface IBus {

    void register(Object object);

    void unregister(Object object);

    void post(AbsEvent event);

    void postSticky(AbsEvent event);


    abstract class AbsEvent {
        public abstract int getTag();
    }

}

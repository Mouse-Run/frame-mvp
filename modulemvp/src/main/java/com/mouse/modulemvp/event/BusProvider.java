package com.mouse.modulemvp.event;

/**
 * Created by mouse on 2016/12/22.
 */

public class BusProvider {

    static class BusProviderHolder {
        static RxBusImpl bus = RxBusImpl.get();
    }

    public static RxBusImpl getBus(){
        return BusProviderHolder.bus;
    }

//    private volatile  static RxBusImpl bus;
//
//    public static RxBusImpl getBus() {
//        if (bus == null) {
//            synchronized (BusProvider.class) {
//                if (bus == null) {
//                    bus = RxBusImpl.get();
//                }
//            }
//        }
//        return bus;
//    }

}

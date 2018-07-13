package com.wm.toec.microenv.eventbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wm on 2018/6/6.
 * 用rxjava封装的事件总线 非粘性
 */

public class Rxbus {
    private static volatile Rxbus instance;
    private final Subject<Object> bus;

    public Rxbus() {
        bus = PublishSubject.create();
    }

    public static Rxbus getInstance() {
        if (instance == null) {
            synchronized (Rxbus.class) {
                if (instance == null) {
                    instance = new Rxbus();
                }
            }
        }
        return instance;
    }

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}

package com.grayditch.netarea.domain.interactor.interfaces;

/**
 * Created by gerard on 18/04/16.
 */
public interface Interactor extends Runnable {
    /**
     * Everything inside this method will be executed asynchronously.
     */
    void run();
}

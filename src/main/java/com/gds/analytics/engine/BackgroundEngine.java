package com.gds.analytics.engine;

/**
 * @author Sujith Ramanathan
 */
public abstract class BackgroundEngine<T> {

    public void runBackground(T event, long ts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                analyze(event, ts);
            }
        }).start();
    }

    protected abstract void analyze(T object, long ts);

}

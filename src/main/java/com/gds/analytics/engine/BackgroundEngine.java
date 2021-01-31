package com.gds.analytics.engine;

/**
 * @author Sujith Ramanathan
 */
public abstract class BackgroundEngine<T> {

    protected String threadName;

    public void runBackground(T event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                analyze(event);
            }
        }, threadName);
    }

    protected abstract void analyze(T object);

}

package krit.pro.consumer.scheduler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.random;

public class SchedulerRunnable implements Runnable {

    private UUID uuid;
    private long idx;

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public long getIdx() {
        return idx;
    }

    public SchedulerRunnable(long idx) {
        super();
        this.idx = idx;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep((int) (10 * random()));
            System.out.println("ВНУТРЕННИЙ ПОТОК " + idx + " ЗАВЕРШЁН");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
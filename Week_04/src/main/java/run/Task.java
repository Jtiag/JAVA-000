package run;

import domain.Result;

public class Task implements Runnable {
    private Result result;

    public Task(Result result) {
        this.result = result;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        int sum = Homework03.sum();
        result.setResult(sum);
    }
}

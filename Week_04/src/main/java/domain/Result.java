package domain;

import com.google.common.eventbus.Subscribe;

public class Result {
    private int result;

    @Subscribe
    public void consumer(int event) {
        result = event;
    }

    public int getResult() {
        return result;
    }
}

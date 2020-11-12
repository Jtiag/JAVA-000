package caller;

import run.Homework03;

import java.util.concurrent.Callable;

public class MyCaller implements Callable<Integer> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
        return Homework03.sum();
    }

}

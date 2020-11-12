package run;

import caller.MyCaller;
import caller.MyThreadFactory;
import domain.Result;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {
    private static int sum;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                1000, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(100), new MyThreadFactory());
        // 方法零 使用Runnable + submit(R,r)
//        sum = m0(threadPoolExecutor);
        // 方法一 使用Callable future.get()阻塞获取
//        sum = m1(threadPoolExecutor);
        // 方法二 使用 futureTask
//        sum = m2(threadPoolExecutor);
        // 方法三 使用countDownLatch
//        sum = m3(threadPoolExecutor);
        // 方法四 使用 CyclicBarrier
//        sum = m4(threadPoolExecutor);
        // 方法五 使用Semaphore
//        sum = m5(threadPoolExecutor);
        // 方法六 使用CompletableFuture
//        sum = m6();
        // 方法七 使用wait notify机制
//        sum = m7(threadPoolExecutor);
        // 方法八 使用join
        sum = m8();
        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + sum);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        threadPoolExecutor.shutdown();
        // 然后退出main线程
        System.out.println("退出主线程");
    }

    private static int m8() {
        final int[] sum = {0};
        Thread thread = new Thread(() -> {
            sum[0] = sum();
            System.out.println("子线程执行完毕");
        });
        thread.start();
        try {
            System.out.println("等待子线程执行完毕");
            thread.join();
            System.out.println("主线程开始执行");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sum[0];
    }

    private static int m7(ThreadPoolExecutor threadPoolExecutor) {
        final int[] sum = {0};
        byte[] lock1 = new byte[1];
        threadPoolExecutor.execute(() -> {
            sum[0] = sum();
            // 唤醒主线程
            synchronized (lock1) {
                lock1.notify();
            }
            System.out.println(Thread.currentThread().getName() + " 执行完成将唤醒主线程");
        });
        // 阻塞主线程
        try {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " 将被阻塞");
                lock1.wait();
                System.out.println(Thread.currentThread().getName() + " 被唤醒");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sum[0];
    }

    private static int m6() {
        final int[] sum = {0};
        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> {
            sum[0] = sum();
            System.out.println(Thread.currentThread().getName() + " 子线程处理完成");
        });

        CompletableFuture<Void> allOf = CompletableFuture.allOf(task);
        System.out.println(Thread.currentThread().getName() + " all of 阻塞开始");
        allOf.join();
        System.out.println(Thread.currentThread().getName() + " all of 阻塞结束");
        return sum[0];
    }

    private static int m5(ThreadPoolExecutor threadPoolExecutor) {
        final int[] sum = {0};
        int worker = 0;
        // Semaphore 维护的是一个计数器， 如果计数器值小于等于0，线程进入休眠
        // 释放信号量 信号量内部的计数器加1，当计数器的值大于0时，之前进入休眠的线程将被唤醒并再次试图获得信号量
        Semaphore semaphore = new Semaphore(worker);
        threadPoolExecutor.execute(() -> {
            sum[0] = sum();
            // 在子线程中控制资源释放
            semaphore.release();
            int availablePermits = semaphore.availablePermits();
            System.out.println("子线程 availablePermits1 = " + availablePermits + " 大于0休眠的线程被唤醒");
        });
        try {
            int availablePermits = semaphore.availablePermits();
            System.out.println("主线程 availablePermits = " + availablePermits + " 主线程休眠");
            // 主线程中控制资源占用
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sum[0];
    }

    private static int m4(ThreadPoolExecutor threadPoolExecutor) {
        final int[] sum = {0};
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        threadPoolExecutor.execute(() -> {
            try {
                sum[0] = sum();
            } finally {
                try {
                    cyclicBarrier.await();
                    int numberWaiting = cyclicBarrier.getNumberWaiting();
                    System.out.println("子线程 " + Thread.currentThread().getName() + " 任务结束继续处理其他任务 numberWaiting = "
                            + numberWaiting);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        // CyclicBarrier自身并不保证主线程在子线程完成之后执行。CyclicBarrier仅仅是为调用await方法的线程设置一个集合点
        // CyclicBarrier可以让多个执行流程并行化后达到一个集合点后再一起并行化执行
        try {
            cyclicBarrier.await();
            System.out.printf("numberWaiting = %d 主线程开始调用\n", cyclicBarrier.getNumberWaiting());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        return sum[0];
    }

    private static int m3(ThreadPoolExecutor threadPoolExecutor) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final int[] sum = {0};
        threadPoolExecutor.submit(() -> {
            try {
                sum[0] = sum();
            } finally {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sum[0];
    }

    private static int m2(ThreadPoolExecutor threadPoolExecutor) {
        FutureTask<Integer> futureTask = new FutureTask<>(Homework03::sum);
        threadPoolExecutor.submit(futureTask);
        int result = 0;
        try {
            result = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static int m1(ThreadPoolExecutor threadPoolExecutor) {
        int sum = 0;
        Future<Integer> sumFuture = threadPoolExecutor.submit(new MyCaller());
        try {
            sum = sumFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sum;
    }

    private static int m0(ThreadPoolExecutor threadPoolExecutor) {
        Result res = new Result();
        Result result = new Result();
        Task task = new Task(result);
        Future<Result> submit = threadPoolExecutor.submit(task, result);
        try {
            res = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res.getResult();
    }

    public static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

}

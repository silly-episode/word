package com.boot;

import com.boot.entity.Plan;
import com.boot.entity.WordModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/15 14:02
 * @FileName: CommonTest
 * @Description: 普通test
 */
@Slf4j
public class CommonTest {


    //同步一个静态方法
    public synchronized static void fun() {
        // ...
    }

    //    反射
    @Test
    public void test1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Plan plan = (Plan) Class.forName("com.boot.entity.Plan").getDeclaredConstructor().newInstance();
        Method[] methods = plan.getClass().getMethods();

        log.info(Arrays.toString(methods));

    }

    //    容器
    @Test
    public void test2() {

        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
        treeSet.add(3);
        treeSet.add(2);
        treeSet.add(0);
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(1);
        hashSet.add(3);
        hashSet.add(2);
        hashSet.add(4);
        for (Integer o : hashSet) {
            System.out.println(o);
        }
//        获取不可变的集合
        Collections.unmodifiableSet(treeSet);

    }

    @Test
    public void test3() throws InterruptedException {
//多线程实现方式
        MyRun myRun = new MyRun();
        Thread thread1 = new Thread(myRun);
        thread1.start();

        MyCall myCall = new MyCall();
        FutureTask<Integer> ft = new FutureTask<>(myCall);
        Thread thread2 = new Thread(ft);
        thread2.start();

        Mythread mythread = new Mythread();
        mythread.start();

        //executor:管理多个异步任务的执行，而无需程序员显式地管理线程的生命周期。这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。
        ExecutorService executorService = Executors.newCachedThreadPool();
        //等待线程都执行完毕之后再关闭
        executorService.shutdown();
        //相当于调用每个线程的 interrupt() 方法。
        executorService.shutdownNow();
        //Daemon :守护线程是程序运行时在后台提供服务的线程，不属于程序中不可或缺的部分。
        //当所有非守护线程结束时，程序也就终止，同时会杀死所有守护线程。
        //main() 属于非守护线程。
        //在线程启动之前使用 setDaemon() 方法可以将一个线程设置为守护线程。
        mythread.setDaemon(true);
        //sleep 5s 睡眠
        Thread.sleep(5000);
        //yield 让步
        Thread.yield();
        // join （当前线程掉另外一个线程的join）
        thread2.join();

//        wait() 是 Object 的方法，而 sleep() 是 Thread 的静态方法；
//        wait() 会释放锁，sleep() 不会。
        //wait 线程在等待时会被挂起 await(java.util.concurrent) 可以指定等待的条件
        wait();
        //notify signal
        notify();
        //notifyAll signalAll
        notifyAll();

//        ReentrantLock 可中断，而 synchronized 不行。
        //ReentrantLock(Jdk实现)，不释放锁可能会产生死锁问题，synchronized不会
        Lock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();

//      Juc
//        CountdownLatch
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        countDownLatch.countDown();
        countDownLatch.await();
//        ......

//        原子类型
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.getAndIncrement();


        //Java 提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是 JVM 实现的 synchronized，而另一个是 JDK 实现的 ReentrantLock
        // 同步代码块
        synchronized (this) {
            // ...
        }


    }

    //    同步一个方法
    public synchronized void func() {
        // ...
    }

    //    IO
    @Test
    public void test4() {

        File file = new File("static/wordResources/test.json");

    }

    //    测试@Accessors(chain = true)的用法
    @Test
    public void test5() {
        WordModule wordModule = new WordModule().setModuleId(1L).setWordPath("123");
        wordModule.setModuleImagePath("123").setRemark("123");
        System.out.println(wordModule);
    }

    //    多线程
    public class MyRun implements Runnable {
        @Override
        public void run() {

        }
    }

    public class MyCall implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            return null;
        }
    }

    public class Mythread extends Thread {
        @Override
        public void run() {

        }
    }

    //同步一个类
    public class SynchronizedExample {
        public void func2() {
            synchronized (SynchronizedExample.class) {

            }
        }
    }

}

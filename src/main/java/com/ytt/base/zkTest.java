package com.ytt.base;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ytt on 2018/10/10.
 */
public class zkTest implements Watcher {
    /**
     * CountDownLatch能够使一个线程在等待另外一些线程完成各自工作之后，再继续执行。
     * 使用一个计数器进行实现。计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。
     * 当计数器的值为0时，表示所有的线程都已经完成了任务，然后在CountDownLatch上等待的线程就可以恢复执行任务
     */
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        // 创建一个普通会话
        ZooKeeper zooKeeper = new ZooKeeper("192.168.137.111:2181",
                5000, new zkTest()
        );
        System.out.println(zooKeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {

        }

        // 根据 sessionId 和passwd 创建会话，实现复用
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();
        System.out.print(sessionId);
//        zooKeeper = new ZooKeeper("192.168.137.111:2181",5000,
//                new zkTest(),1l,"test".getBytes());
//        ZooKeeper zooKeeper2 = new ZooKeeper("192.168.137.111:2181", 5000,
//                new zkTest(), sessionId, passwd);
        Thread.sleep(Integer.MAX_VALUE);
    }

    // 监控所有被处罚的事件
    public void process(WatchedEvent watchedEvent) {
        System.out.println("已经触发了：" + watchedEvent.getState() + "事件！");
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}

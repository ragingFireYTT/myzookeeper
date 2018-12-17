package com.ytt.base;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.ZooDefs.Ids;

/**
 * Created by ytt on 2018/10/10.
 */
public class zkCreateTest implements Watcher {
    /**
     * CountDownLatch能够使一个线程在等待另外一些线程完成各自工作之后，再继续执行。
     * 使用一个计数器进行实现。计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。
     * 当计数器的值为0时，表示所有的线程都已经完成了任务，然后在CountDownLatch上等待的线程就可以恢复执行任务
     */
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        // 创建一个普通会话
        ZooKeeper zooKeeper = new ZooKeeper("192.168.137.111:2181",
                5000, new zkCreateTest()
        );
        System.out.println(zooKeeper.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {

        }
        System.out.println(zooKeeper.getSessionId());

        String path1 = zooKeeper.create("/zk-test-ephemeral-","".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL); //临时
        String path2 = zooKeeper.create("/zk-test-ephemeral-","".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);// 临时顺序
        System.out.println("Success create znode:"+path1);
        System.out.println("Success create znode:"+path2);
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

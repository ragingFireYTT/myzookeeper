package com.ytt.base;

import org.apache.zookeeper.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by ytt on 2018/12/17.
 */
public class TestZookeeper {
    private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeOut = 2000; // 毫秒
    ZooKeeper zkClient;

    // before 用于初始化一些数据
    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeOut, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println("=========================start========================");
                try {
                    System.out.println("---------状态--------"+event.getState());
                    List<String> children = null;
                    children = zkClient.getChildren("/", true);
                    for (String child : children) {
                        System.out.println("child = " + child);
                    }
                    System.out.println("=========================end========================");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 1. 创建节点
    @Test
    public void createNode() throws KeeperException, InterruptedException {
        String path = zkClient.create("/atguigu1","dahai".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("path = " + path);
    }
    // 1.1 异步创建节点
    @Test
    public void createNodeYiBu() throws InterruptedException {
        zkClient.create("/zkEphemeral", "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new AsyncCallback.StringCallback() {
                    /**
                     * @param rc 状态码
                     * @param path
                     * @param ctx
                     * @param name 创建的Znode的名称。成功时，name和path通常是相等的，除非已经创建了顺序节点。
                     */
                    public void processResult(int rc, String path, Object ctx, String name) {
                        System.out.println("创建结果:"+rc+"===:"+path+"===:"+ctx+"===真实名字:"+name);
                    }
                }, "我是谁"); //异步创建
        Thread.sleep(Long.MAX_VALUE);
    }

    // 2. 监控节点的变化
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
    // 3. 客户端复用,使用 sessionId
    @Test
    public void clientSame() throws IOException, InterruptedException {
        long sessionId = zkClient.getSessionId();
        byte[] passwd = zkClient.getSessionPasswd();
        ZooKeeper zkClient2 = new ZooKeeper(connectString, 2000, null,sessionId,passwd);
        Thread.sleep(Long.MAX_VALUE);
    }
    // 4. 有验证权限的客户端
    @Test
    public void clientAuth() throws IOException, KeeperException, InterruptedException {
        final  String PATH= "/zk-book-auth_test";
        ZooKeeper zooKeeper1= new ZooKeeper(connectString,
                2000,null);
        zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        //region   无权访问
//        ZooKeeper zooKeeper2= new ZooKeeper("192.168.137.111:2181",
//                5000,null);
//        zooKeeper2.getData(PATH,false,null);
        // endregion

        ZooKeeper zooKeeper2= new ZooKeeper(connectString,2000,null);
        zooKeeper2.addAuthInfo("digest","foo:true".getBytes());
        byte[] data = zooKeeper2.getData(PATH, false, null);
        System.out.println("-------验证权限，客户端------------"+new String(data));
        Thread.sleep(Long.MAX_VALUE);
    }
}

package com.ytt.case1;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by ytt on 2018/12/17.
 */
public class DistributeServer {


    public static void main(String[] args) throws Exception {
        DistributeServer server = new DistributeServer();
        // 1，创建连接
        server.getConnect();
        // 2. 注册节点
        server.regist(args[0]);
        // 3. 处理业务逻辑
        server.business();
    }

    private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeOut = 2000; // 毫秒
    private ZooKeeper zkClient;

    /**
     * 初始化，客户端
     *
     * @throws IOException
     */
    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeOut, null);
    }

    /**
     * 将自己注册到服务器
     */
    private void regist(String hostName) throws Exception {
        String path = zkClient.create("/servers/server", hostName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);// 带编号的临时节点
        System.out.println("hostName = " + hostName + "上线了");
    }
    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }
}

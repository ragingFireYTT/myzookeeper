package com.ytt.base;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Created by ytt on 2018/10/10.
 */
public class AuthSample {
    final static String PATH= "/zk-book-auth_test";
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper1= new ZooKeeper("192.168.137.111:2181",
                5000,null);
        zooKeeper1.addAuthInfo("digest","foo:true".getBytes());
        zooKeeper1.create(PATH,"init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        //region   无权访问
//        ZooKeeper zooKeeper2= new ZooKeeper("192.168.137.111:2181",
//                5000,null);
//        zooKeeper2.getData(PATH,false,null);
        // endregion

        ZooKeeper zooKeeper2= new ZooKeeper("192.168.137.111:2181",5000,null);
        zooKeeper2.addAuthInfo("digest","foo:fal".getBytes());
        System.out.println(zooKeeper2.getData(PATH,false,null));
    }
}

package com.ytt.base;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ytt on 2018/12/17.
 */
public class TestZookeeper {
    private String connectString = "hadoop102:2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeOut = 2000; // 毫秒
    ZooKeeper zooKeeper;

    @Test
    public void init() throws IOException {
        zooKeeper = new ZooKeeper(connectString, sessionTimeOut, new Watcher() {
            public void process(WatchedEvent event) {

            }
        });
    }


}

package com.ytt.publishsubscribe;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import com.alibaba.fastjson.JSON;

/**
 * 监控Config 节点变化
 *
 */
public class WorkServer {

    private ZkClient zkClient;
    private String configPath;
    private String serversPath;
    private ServerData serverData;
    private ServerConfig serverConfig;
    private IZkDataListener dataListener;

    public WorkServer(String configPath, String serversPath, ServerData serverData, ZkClient zkClient, ServerConfig initConfig) {
        this.zkClient = zkClient;
        this.configPath = configPath;
        this.serversPath = serversPath;
        this.serverData = serverData;
        this.serverConfig = initConfig;
        this.dataListener = new IZkDataListener() {
            public void handleDataDeleted(String dataPath) throws Exception {

            }

            public void handleDataChange(String dataPath, Object data)
                    throws Exception {
                String retJson = new String((byte[]) data);
                ServerConfig serverConfigLocal = (ServerConfig) JSON.parseObject(retJson, ServerConfig.class);
                updateConfig(serverConfigLocal);
                System.out.println("WorkServer 说： config 改变了 :" + serverConfig.toString());
            }
        };
    }

    public void start() {
        System.out.println("work server start...");
        initRunning();
    }

    public void stop() {
        System.out.println("work server stop...");
        zkClient.unsubscribeDataChanges(configPath, dataListener);
    }

    private void initRunning() {
        createMe();
        zkClient.subscribeDataChanges(configPath, dataListener);
    }

    private void createMe() {
        String mePath = serversPath.concat("/").concat(serverData.getAddress());
        System.out.println("WorkServer 说：mePath-----"+mePath);
        try {
            System.out.println("WorkServer 说：创建临时节点----"+mePath);
            zkClient.createEphemeral(mePath, JSON.toJSONString(serverData)
                    .getBytes());// 创建临时节点
        } catch (ZkNoNodeException e) {
            // 创建父节点
            zkClient.createPersistent(serversPath, true);// 创建持久节点
            createMe();
        }
    }

    private void updateConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}

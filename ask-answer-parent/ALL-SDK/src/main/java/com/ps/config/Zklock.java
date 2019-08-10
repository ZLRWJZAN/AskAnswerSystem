package com.ps.config;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/13 21:25
 */
public class Zklock {
    private final static String locksRootName = "/locks";
    private int timeout;
    private String lockName;
    private ZooKeeper zk;
    private String enodeName;

    public Zklock(String lockName, int timeout) throws IOException, KeeperException, InterruptedException {
        //判断锁名字是否规范
        lockName = locksRootName + "/" + lockName;

        this.lockName = lockName;
        this.timeout = timeout;

        //1.创建连接
        String hostPort = "localhost:2181";
        zk = new ZooKeeper(hostPort, 2000, e -> {
            //监听删除节点事件
            System.out.println("watcher....");
            //判断是否是删除节点动作
            synchronized (this.lockName) {
                this.lockName.notifyAll();
            }
        });

        //判断是否有locksRootName,如果没有则主动创建
        Stat exists = zk.exists(locksRootName, null);
        if (exists == null) {
            zk.create(locksRootName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        }
        //3.判断lockName
        exists = zk.exists(lockName, null);
        if (exists == null) {
            zk.create(lockName, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

    }

    public boolean getLock() throws KeeperException, InterruptedException {
        //1.创建临时顺序节点
        enodeName = zk.create(lockName + "/c", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //2.获取lockName的所有节点
        while (true) {
            List<String> children = zk.getChildren(lockName, null);
            //3.升序排序
            children = children.stream().sorted().collect(Collectors.toList());
            System.out.println("当前竞争节点:" + children);
            String s = children.get(0);
            if (enodeName.endsWith(s)) {
                return true;
            }
            //未获取到锁
            synchronized (lockName) {
                lockName.wait(1000);
            }
        }

    }

    public void unlock() throws KeeperException, InterruptedException {
        System.out.println("删除节点" + enodeName);
        zk.delete(enodeName, -1);
    }
}

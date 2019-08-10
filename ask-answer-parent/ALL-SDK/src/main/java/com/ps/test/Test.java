package com.ps.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/21 21:10
 */
public class Test {
    public static void main(String[] args) {
        CarCompetion carCompetion = new CarCompetion();
                                                                    //设置线程池里的线程数量
        final ExecutorService carPool = Executors.newFixedThreadPool(carCompetion.totalCarNum);
        for (int i = 0; i < carCompetion.totalCarNum; i++) {
            carPool.execute(new Cars(i, carCompetion));
        }
    }
}

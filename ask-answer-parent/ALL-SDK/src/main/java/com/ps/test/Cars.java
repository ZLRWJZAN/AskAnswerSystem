package com.ps.test;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/22 8:07
 */
public class Cars implements Runnable{
    private int i;
    private CarCompetion carCompetion;

    public Cars(int i, CarCompetion carCompetion) {
        this.i = i;
        this.carCompetion = carCompetion;
    }

    @Override
    public void run() {
        synchronized (carCompetion){
            carCompetion.nowCarNum++;
            while (carCompetion.nowCarNum<carCompetion.totalCarNum){
                try {
                    carCompetion.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            carCompetion.notifyAll();
        }
        begin();
    }

    private void begin() {
        System.out.println("开始:"+i);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("结束:"+i);
    }
}

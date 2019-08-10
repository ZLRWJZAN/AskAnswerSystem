package com.ps.test;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/21 21:09
 */
public class Car implements Runnable{
    private int carNum;
    private CarCompetion competion = null;
    public Car(int carNum, CarCompetion competion) {
        this.carNum = carNum;
        this.competion = competion;
    }
    @Override
    public void run() {
        synchronized (competion) {
            competion.nowCarNum++;
            while (competion.nowCarNum < competion.totalCarNum) {
                try {
                    competion.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            competion.notifyAll();
        }
        startCar();
    }
    private void startCar() {
        System.out.println("Car num " + this.carNum + " start to run.");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Car num " + this.carNum + " get to the finish line.");
    }
}

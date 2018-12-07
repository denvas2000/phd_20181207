package phd;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
/*public class Thread_Test implements Runnable {

    public void run() {
        for (int i=0;i<10;i++)
            System.out.println("Hello from ONE thread!"+Thread.currentThread().getName());
    }

    public static void main(String args[]) {
        Thread den_thr1=new Thread(new Thread_Test());
        Thread den_thr2=new Thread(new Thread_Test());
        Thread den_thr3=new Thread(new Thread_Test());
        den_thr1.start();
        den_thr2.start();
        den_thr3.start();
    }

}
*/
//DENNIS 1
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Thread_Test {
    public static void main(String[] args) {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Executor Service...");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        System.out.println("Creating a Runnable...");
        Runnable runnable = () -> {
            System.out.println("Inside : " + Thread.currentThread().getName());
        };

        System.out.println("Submit the task specified by the runnable to the executor service.");
        executorService.submit(runnable);
    }
}

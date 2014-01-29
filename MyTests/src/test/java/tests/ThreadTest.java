package tests;

import org.junit.*;

public class ThreadTest {

    static class Run implements Runnable {

        public long i;

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
    };

    @Test
    public void testInstantiation() {

        Run run = new Run();

        for (int i = 0; i < 10; i++) {
            (new Thread(run)).start();
        }

        System.err.println(String.format("i: %l", run.i));
    }

    public static void main(String [] args){
        Run run = new Run();

        for (int i = 0; i < 10; i++) {
            (new Thread(run)).start();
        }

        System.err.println(String.format("i: %l", run.i));
    }
}
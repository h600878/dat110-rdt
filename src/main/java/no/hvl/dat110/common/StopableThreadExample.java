package no.hvl.dat110.common;

public class StopableThreadExample extends Stopable {

    public StopableThreadExample() {
        super("stopable thread");
    }

    private int i = 0;

    @Override
    public void doProcess() {

        System.out.println("stopable thread working:" + i);

        try {

            // simulate some processing time
            Thread.sleep(1000);

        }
        catch (InterruptedException ex) {

            System.out.println("Stopable thread " + ex.getMessage());
            ex.printStackTrace();
        }

        i++;
    }
}

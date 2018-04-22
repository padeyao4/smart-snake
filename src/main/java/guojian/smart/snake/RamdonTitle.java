package guojian.smart.snake;

import java.util.Random;

public class RamdonTitle {


    public String getTitle() {

        Random r = new Random();

        String value;
        int key = r.nextInt(10000);
        switch (key) {
            case 2013:
                value = "happy";
                break;
            default:
                value = "smart-snake!";
        }

        return value;
    }


}

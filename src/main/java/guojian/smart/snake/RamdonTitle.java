/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/19 8:38 AM
 * PS: Not easy to write code, please indicate.
 */
package guojian.smart.snake;

import java.util.Random;

/**
 * Author by guojian, Email guojian_k@qq.com, Date on 2018/6/21 10:34 AM
 * PS: Not easy to write code, please indicate.
 *
 * for fun
 */
public class RamdonTitle {


    public String getTitle() {

        Random r = new Random();

        String value;
        int key = r.nextInt(10000);
        switch (key) {
            case 2013:
                value = "happy";
                break;
            case 1123:
                value = "nice to meet you!";
                break;
            default:
                value = "smart-snake";
        }

        return value;
    }


}

package guojian.smart.snake;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RamdonTitle {


    public String getTitle() {

        Integer key = Integer.parseInt(new SimpleDateFormat("MMdd").format(new Date()));

        String value;
        switch (key) {
            case 719:
                value = "生日快乐!";
                break;
            case 1010:
                value = "Skycity，Happy Birthday!";
                break;
            case 1224:
                value = "圣诞节快乐!";
                break;
            case 101:
                value = "元旦快乐!";
                break;
            case 411:
                value = "我又回来了 ^_^";
                break;
            default:
                //TODO 随机出现祝福语
                value = "你好啦";
        }

        return value;
    }


}

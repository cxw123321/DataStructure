package dayFive;

import java.sql.Timestamp;
import java.util.TimeZone;

public class TimeExperiment {
    public void getTime(){
        // 当前时间毫秒数
        long current=System.currentTimeMillis();
        // 今天零点零分零秒的毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
        // 今天23点59分59秒的毫秒数
        long twelve=zero+24*60*60*1000-1;
        // 昨天的这一时间的毫秒数
        long yesterday=System.currentTimeMillis()-24*60*60*1000;
        System.out.println("当前时间: " + new Timestamp(current));
        System.out.println("昨天这一时间点: " + new Timestamp(yesterday));
        System.out.println("今天零点零分零秒: " + new Timestamp(zero));
        System.out.println("今天23点59分59秒: " + new Timestamp(twelve));

        System.out.println("当前时间: " + current);
        System.out.println("昨天这一时间点: " + yesterday);
        System.out.println("今天零点零分零秒的毫秒数: " + zero);
        System.out.println("今天23点59分59秒的毫秒数: " + twelve);
        System.out.println(current - yesterday);
        System.out.println(twelve - zero);
    }

    // 测试
    public static void main(String[] args){
        TimeExperiment te = new TimeExperiment();
        te.getTime();
    }
}

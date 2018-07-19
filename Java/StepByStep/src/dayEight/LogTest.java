package dayEight;

import org.apache.log4j.*;

public class LogTest {
    // 创建日志器，日志器的名称为当前类的类名
//    static Logger log = Logger.getLogger("TESTLOG");
    static Logger log1 = Logger.getLogger("test.test");

    public static void main(String[] args) {
        // 创建发送器，使用简单格式化器
        Appender appender = new ConsoleAppender(new SimpleLayout());
        // 加载这个发送器
        BasicConfigurator.configure(appender);
        // 设置日志级别
//        log.setLevel(Level.WARN);
//        // 输出info消息，级别从低到高
//        log.debug("debug!");
//        log.info("info!");
//        log.warn("warn!");
//        log.error("error!");
//        log.fatal("fatal!");

        log1.info("看不见!");
        log1.warn("看得见!");
    }

}

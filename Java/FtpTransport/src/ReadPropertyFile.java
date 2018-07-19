import java.util.Properties;
import java.io.*;
import org.apache.log4j.*;

public class ReadPropertyFile {
    static Logger log = Logger.getLogger(ReadPropertyFile.class.getName());
    public final String filePath = "D:/test.properties";

    public String path = "";
    public String uploadPath = "";
    public String ip = "";
    public int port = 0;
    public String username = "";
    public String password = "";

    public void readProperties(){
        // 配置日志
        // 创建发送器，使用简单格式化器
        Appender appender = new ConsoleAppender(new SimpleLayout());
        // 加载这个发送器
        BasicConfigurator.configure(appender);
        // 设置日志级别
        log.setLevel(Level.DEBUG);

        Properties prop = new Properties();
        String[] value = new String[5];
        try{
            // 通过输入缓冲流进行读取配置文件
            InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            // 加载输入流
            prop.load(new InputStreamReader(InputStream, "utf-8"));
            // 根据关键字获取value值
            path = prop.getProperty("path").toString();
            uploadPath = prop.getProperty("uploadPath").toString();
            ip = prop.getProperty("ip").toString();
            port = Integer.parseInt(prop.getProperty("port").toString());
            username = prop.getProperty("username").toString();
            password = prop.getProperty("password").toString();
            log.debug("*************读取配置文件信息*************");
            log.debug("path: " + path);
            log.debug("uploadPath: " + uploadPath);
            log.debug("ip: " + ip);
            log.debug("port: " + port);
            log.debug("username: " + username);
            log.debug("password: " + password);
            log.debug("****************************************");
//            System.out.println("*************读取配置文件信息*************");
//            System.out.println("path: " + path);
//            System.out.println("uploadPath: " + uploadPath);
//            System.out.println("ip: " + ip);
//            System.out.println("port: " + port);
//            System.out.println("username: " + username);
//            System.out.println("password: " + password);
//            System.out.println("****************************************");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

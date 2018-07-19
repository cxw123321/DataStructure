package dayFive;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class TestProperties {
    String filePath = "D:/media/test.properties";
    public void readProperties(){
        Properties prop = new Properties();
        String[] value = new String[4];
        try{
            // 通过输入缓冲流进行读取配置文件
            InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            // 加载输入流
            prop.load(InputStream);
            // 根据关键字获取value值
            value[0] = prop.getProperty("ip");
            value[1] = prop.getProperty("port");
            value[2] = prop.getProperty("username");
            value[3] = prop.getProperty("password");
            for(int i = 0; i < value.length; i++){
                System.out.println(value[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        TestProperties tps = new TestProperties();
        tps.readProperties();
    }
}

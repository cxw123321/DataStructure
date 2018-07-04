package dayFour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.charset.Charset;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpTransport {
    //ftp对象
    private FTPClient ftp;
    //需要连接到的ftp端的ip
    private String ip;
    //连接端口，默认21
    private int port;
    //要连接到的ftp端的名字
    private String name;
    //要连接到的ftp端的对应得密码
    private String pwd;

    //调用此方法，输入对应得ip，端口，要连接到的ftp端的名字，要连接到的ftp端的对应得密码。连接到ftp对象，并验证登录进入fto
    public void ftp1(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;

        //验证登录
        try {
            ftp.connect(ip, port);
            System.out.println(ftp.login(name, pwd));
            ftp.setCharset(Charset.forName("UTF-8"));
            ftp.setControlEncoding("UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //上传文件
    public void putFile() {
        try {
            //将本地的"D:/1.zip"文件上传到ftp的根目录文件夹下面，并重命名为"aaa.zip"
            System.out.println(ftp.storeFile("aaa.zip", new FileInputStream(new File("D:/media/test2.zip"))));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //上传文件的第二种方法，优化了传输速度
    public void putFile2() {
        try {
            OutputStream os = ftp.storeFileStream("aaa.zip");
            FileInputStream fis = new FileInputStream(new File("D:/1.zip"));

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                os.write(b,0,len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //下载文件
    public void getFile() {
        try {
            //将ftp根目录下的"jsoup-1.10.2.jar"文件下载到本地目录文件夹下面，并重命名为"1.jar"
            ftp.retrieveFile("科大讯飞接口调试.txt", new FileOutputStream(new File("科大讯飞接口调试.txt")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //下载文件的第二种方法，优化了传输速度
    public void getFile2() {
        try {
            InputStream is = ftp.retrieveFileStream("/home/test/科大讯飞接口调试.txt");

            FileOutputStream fos = new FileOutputStream(new File("D:/科大讯飞接口调试.txt"));

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                fos.write(b,0,len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void downLoad(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;

        //验证登录
        try {
            ftp.connect(ip, port);
            ftp.login(name, pwd);
            ftp.setCharset(Charset.forName("UTF-8"));
            ftp.setControlEncoding("UTF-8");

            InputStream is = ftp.retrieveFileStream("科大讯飞接口调试.txt");
            FileOutputStream fos = new FileOutputStream(new File("D:/科大讯飞接口调试.txt"));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                fos.write(b,0,len);
            }

            is.close();
            ftp.completePendingCommand();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void downLoad2(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;

        //验证登录
        try {
            ftp.connect(ip, port);
            ftp.login(name, pwd);
            ftp.setCharset(Charset.forName("UTF-8"));
            ftp.setControlEncoding("UTF-8");
            System.out.println(ftp.retrieveFile("科大讯飞接口调试.txt", new FileOutputStream(new File("科大讯飞接口调试.txt"))));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //上传文件
    public void uploadFile(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;

        //验证登录
        try {
            ftp.connect(ip, port);
            ftp.login(name, pwd);
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 成功输出true，失败输出false
            System.out.println(ftp.storeFile("test2.zip", new FileInputStream(new File("D:/media/test2.zip"))));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //上传文件
    public void uploadFile2(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;

        //验证登录
        try {
            ftp.connect(ip, port);
            ftp.login(name, pwd);
            ftp.setCharset(Charset.forName("UTF-8"));
            ftp.setControlEncoding("UTF-8");

            OutputStream os = ftp.storeFileStream("test2.zip");
            FileInputStream fis = new FileInputStream(new File("D:/media/test2.zip"));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                os.write(b,0,len);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testFtp1(){
        //创建客户端对象
        FTPClient ftp = new FTPClient();
        InputStream local=null;
        try {
            //连接ftp服务器
            ftp.connect("47.104.85.180", 21);
            //登录
            ftp.login("ftpuser", "ftpuser");
            //设置上传路径
            String path="/home/ftpuser/test";
            //检查上传路径是否存在 如果不存在返回false
            boolean flag = ftp.changeWorkingDirectory(path);
            if(!flag){
                //创建上传的路径  该方法只能创建一级目录，在这里如果/home/ftpuser存在则可创建image
                ftp.makeDirectory(path);
            }
            //指定上传路径
            ftp.changeWorkingDirectory(path);
            //指定上传文件的类型  二进制文件
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //读取本地文件
            File file = new File("D:/media/test2.zip");
            local = new FileInputStream(file);
            //第一个参数是文件名
            ftp.storeFile(file.getName(), local);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭文件流
                local.close();
                //退出
                ftp.logout();
                //断开连接
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

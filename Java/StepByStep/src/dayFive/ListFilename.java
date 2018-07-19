package dayFive;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.sql.Timestamp;
import java.util.TimeZone;
import dayThree.FileZip;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ListFilename {

    // 获取指定文件夹下文件名称
    public void getFilenames(){
        String path = "D:/media";
        File file = new File(path);
        File[] fl = file.listFiles();
        for(File f:fl){
            System.out.println(f.getName());
        }
    }

    // 获取指定文件夹下文件创建时间
    public void getFimeCtime(){
        String path = "D:/media";
        File file = new File(path);
        File[] fl = file.listFiles();

        Path p;
        BasicFileAttributeView basicview;
        BasicFileAttributes attr;

        Date createDate;
        Date modifyDate;

        for(File f:fl){
            p = Paths.get(path + "/" + f.getName());
            basicview = Files.getFileAttributeView(p, BasicFileAttributeView.class,LinkOption.NOFOLLOW_LINKS );
            try {
                attr = basicview.readAttributes();
                createDate = new Date(attr.creationTime().toMillis());
                modifyDate = new Date(attr.lastModifiedTime().toMillis());
                System.out.println(modifyDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ifZip(){
        String path = "D:/media";
        File file = new File(path);
        File[] fl = file.listFiles();

        Path p;
        BasicFileAttributeView basicview;
        BasicFileAttributes attr;
        File zFile;

        // 当前时间毫秒数
        long current=System.currentTimeMillis();
        // 今天零点零分零秒的毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();

        for(File f:fl){
            p = Paths.get(path + "/" + f.getName());
            basicview = Files.getFileAttributeView(p, BasicFileAttributeView.class,LinkOption.NOFOLLOW_LINKS );
            try {
                attr = basicview.readAttributes();
//                createDate = new Date(attr.creationTime().toMillis());
//                if((zero - attr.creationTime().toMillis()) > 0 && (zero - attr.creationTime().toMillis()) < 86400000 ){
//                    System.out.println(f.getName());
//                }
                if((zero - attr.lastModifiedTime().toMillis()) > 0 && (zero - attr.lastModifiedTime().toMillis()) < 86400000 ){
                    System.out.println("需要压缩的文件为：" + p);
                    // 要压缩的文件
                    zFile = new File(p.toString());
                    // 压缩后保存的路径及文件名
                    new FileZip(new File( "D:/media",f.getName()+".zip")).zipFiles(f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 压缩文件并上传至ftp
    public void zipAndUpload(){
        // FTP协议里面，规定文件名编码为iso-8859-1
        String SERVER_CHARSET = "ISO-8859-1";

        /** 本地字符编码 */
        String LOCAL_CHARSET = "GBK";

        String path = "D:/media";
        File file = new File(path);
        File[] fl = file.listFiles();

        Path p;
        BasicFileAttributeView basicview;
        BasicFileAttributes attr;
        File zFile;

        //ftp对象
        FTPClient ftp = new FTPClient();;
        //需要连接到的ftp端的ip
        String ip = "47.104.85.180";
        //连接端口，默认21
        int port = 21;
        //要连接到的ftp端的名字
        String name = "ftpuser";
        //要连接到的ftp端的对应得密码
        String pwd = "ftpuser";

        String remoteName = "";
        String pathName = "";

        // 当前时间毫秒数
        long current=System.currentTimeMillis();
        // 今天零点零分零秒的毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();

        for(File f:fl){
            p = Paths.get(path + "/" + f.getName());
            basicview = Files.getFileAttributeView(p, BasicFileAttributeView.class,LinkOption.NOFOLLOW_LINKS );
            try {
                attr = basicview.readAttributes();
//                createDate = new Date(attr.creationTime().toMillis());
//                if((zero - attr.creationTime().toMillis()) > 0 && (zero - attr.creationTime().toMillis()) < 86400000 ){
//                    System.out.println(f.getName());
//                }
                if((zero - attr.lastModifiedTime().toMillis()) > 0 && (zero - attr.lastModifiedTime().toMillis()) < 86400000 ){
                    System.out.println("需要压缩的文件为：" + p);
                    // 要压缩的文件
                    zFile = new File(p.toString());
                    // 压缩后保存的路径及文件名
//                    new FileZip(new File( "D:/media",f.getName()+".zip")).zipFiles(f);
                    new FileZip(new File(path,f.getName()+".zip")).zipFiles(f);

                    System.out.println("压缩文件上传中...");
                    ftp.connect(ip, port);
                    ftp.login(name, pwd);

//                    if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
//                        // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
//                        LOCAL_CHARSET = "UTF-8";
//                    }

                    ftp.enterLocalPassiveMode();
                    ftp.setFileType(FTP.BINARY_FILE_TYPE);

                    ftp.setCharset(Charset.forName("UTF-8"));
                    ftp.setControlEncoding("UTF-8");
                    // 上传文件时，文件名称需要做编码转换
                    remoteName = f.getName().toString() + ".zip";
                    pathName = p.toString() + ".zip";
                    // 成功输出true，失败输出false
                    System.out.println(ftp.storeFile(new String(remoteName.getBytes("UTF-8"), "UTF-8"), new FileInputStream(new File(p + ".zip"))));
//                    System.out.println(ftp.storeFile("test2.zip", new FileInputStream(new File("D:/media/test2.zip"))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 测试
    public static void main(String[] args){
        ListFilename lfn = new ListFilename();
//        lfn.getFilenames();
//        lfn.getFimeCtime();
//        lfn.ifZip();
        lfn.zipAndUpload();
    }

}

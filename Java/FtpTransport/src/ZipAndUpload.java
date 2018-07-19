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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import java.util.regex.*;
import org.apache.log4j.*;

public class ZipAndUpload {

    // 创建日志器，日志器的名称为当前类的类名
    static Logger log = Logger.getLogger(ZipAndUpload.class.getName());
    private String rootPath = "";
    private String uploadPath="";
    private String ip = "";
    private int port = 0;
    private String username = "";
    private String password = "";

    public void zipAndUpload(){
        // 配置日志
        // 创建发送器，使用简单格式化器
        Appender appender = new ConsoleAppender(new SimpleLayout());
        // 加载这个发送器
        BasicConfigurator.configure(appender);
        // 设置日志级别
        log.setLevel(Level.DEBUG);

        // 读取配置文件，获取连接ftp配置及待读取文件夹路径
        ReadPropertyFile rpf = new ReadPropertyFile();
        rpf.readProperties();
        // D:/media/股市台投资快讯
        rootPath = rpf.path;
        uploadPath = rpf.uploadPath;
        ip = rpf.ip;
        port = rpf.port;
        username = rpf.username;
        password = rpf.password;

        // FTP协议里面，规定文件名编码为iso-8859-1
        String SERVER_CHARSET = "ISO-8859-1";

        // 本地字符编码
        String LOCAL_CHARSET = "GBK";
        // 正则表达式判断文件夹是否为rxxb形式
        String pattern = "[rR]\\d+[bB]";

        // 获取D:/media/股市台投资快讯下文件列表
        File file = new File(rootPath);
        File copyFile = null;
        File[] rnb = null;
        File orgDir = null;
        File copyDir = null;
        File uploadFile = null;
        File[] fl = file.listFiles();
        FileZip fileZip = null;

        Path journalPath = null;
        BasicFileAttributeView fileAttrs = null;
        BasicFileAttributes attr = null;
        File zFile = null;

        // ftp对象
        FTPClient ftp = new FTPClient();
        String ftpFileName = "";
        String zipFile = "";

        FileOperation fileOperation = new FileOperation();
        FileInputStream fis = null;
        boolean isFinished = false;
        boolean ifPathExist = false;
        boolean isLogin = false;
        boolean isRnb = true;

        // 当前时间毫秒数
        long currentTime = System.currentTimeMillis();
        // 今天零点零分零秒的毫秒数
        long zeroTime = currentTime/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();

        // f为每期的文件夹
        for(File f:fl){
            // 判断是否为文件夹，只处理文件夹
            if(f.isDirectory()){
                // p为每期的文件夹绝对路径，path为跟路径，即D:\tzkb\2018投资快报
                journalPath = Paths.get(rootPath + "/" + f.getName());
                fileAttrs = Files.getFileAttributeView(journalPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
                log.debug("开始处理文件:" + journalPath.toString());
//                System.out.println("开始处理文件:" + journalPath.toString());
                // arrt为每期文件夹的各种属性
                try {
                    attr = fileAttrs.readAttributes();
                }catch (IOException e){
                    log.error("读取文件夹属性失败");
//                    System.out.println("读取文件夹属性失败");
                    e.printStackTrace();
                }

                // 判断是否为前一天的文件夹
                if((zeroTime - attr.lastModifiedTime().toMillis()) > 0 && (zeroTime - attr.lastModifiedTime().toMillis()) < 86400000 ){
                    // 第一步，压缩符合条件的文件夹
                    orgDir = new File(journalPath.toString());
                    copyDir = new File(rootPath);
                    fileOperation.copy(orgDir, copyDir);
                    log.debug("复制文件夹" + orgDir.toString() + "到" + copyDir + "下");
//                    System.out.println("复制文件夹" + orgDir.toString() + "到" + copyDir + "下");
                    // subFile为每期文件夹的拷贝，如7604-copy
                    copyFile = new File(journalPath.toString() + "-copy");
                    // sfls代表每期文件夹下的文件，如r1b
                    rnb = copyFile.listFiles();
                    for(File rnbs:rnb){
                        isRnb = Pattern.matches(pattern, rnbs.getName());
                        if(!rnbs.isDirectory()){
                            // 如果不是文件夹，则删除
                            fileOperation.delete(new File(journalPath.toString() + "-copy" + "/" + rnbs.getName()));
                        } else {
                            // 是文件夹，但不符合rnb格式，同样删除
                            if(!isRnb){
                                fileOperation.delete(new File(journalPath.toString() + "-copy" + "/" + rnbs.getName()));
                            }
                        }
                    }

                    // 要压缩的文件，如7604-copy
                    zFile = new File(journalPath.toString() + "-copy");
                    // 压缩后保存的路径及文件名，f代表每期的文件夹
                    uploadFile = new File(rootPath,f.getName()+".zip");
                    fileZip = new FileZip(uploadFile);
                    fileZip.zipFiles(zFile);

                    // 第二步，上传压缩文件夹
                    ftp.setControlEncoding("UTF-8");
                    try{
                        ftp.connect(ip, port);
                    }catch (Exception e){
                        log.error("连接ip、端口时出错");
//                        System.out.println("连接ip、端口时出错");
                        e.printStackTrace();
                    }

                    try{
                        isLogin = ftp.login(username, password);
                    }catch (IOException e){
                        log.error("登录ftp服务器出错");
//                        System.out.println("登录ftp服务器出错");
                        e.printStackTrace();
                    }

                    if(isLogin){
                        log.debug("连接至ftp服务器");
//                        System.out.println("连接至ftp服务器");
                        try{
                            // 检查上传路径是否存在 如果不存在返回false
                            ifPathExist = ftp.changeWorkingDirectory(uploadPath);
                            if(!ifPathExist){
                                // 创建上传的路径，该方法只能创建一级目录，在这里如果/home/ftpuser存在则可创建image
                                ftp.makeDirectory(uploadPath);
                            }
                            // 指定上传路径
                            ftp.changeWorkingDirectory(uploadPath);
                            ftp.enterLocalPassiveMode();
                            ftp.setFileType(FTP.BINARY_FILE_TYPE);
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        // 上传文件时，文件名称需要做编码转换
                        ftpFileName = f.getName() + ".zip";
                        zipFile = journalPath.toString() + ".zip";

                        try{
                            fis = new FileInputStream(new File(zipFile));
                            log.debug("压缩文件" + zipFile + "上传中...");
//                            System.out.println("压缩文件" + zipFile + "上传中...");
                            isFinished = ftp.storeFile(new String(ftpFileName.getBytes("UTF-8"), "UTF-8"), fis);
                            if(isFinished){
                                log.debug("文件上传成功");
//                                System.out.println("文件上传成功");
                            }else {
                                log.error("文件上传失败");
//                                System.out.println("文件上传失败");
                            }
                        }catch (IOException e){
                            log.error("文件上传出错");
//                            System.out.println("文件上传出错");
                            e.printStackTrace();
                        }finally {
                            try {
                                fis.close();
                            }catch (IOException e){
                                log.error("关闭fis流失败");
//                                System.out.println("关闭fis流失败");
                                e.printStackTrace();
                            }
                        }

                        if (ftp.isConnected()) {
                            try {
                                ftp.disconnect();
                                log.debug("关闭FTP连接");
//                                System.out.println("关闭FTP连接");
                            } catch (IOException e) {
                                log.error("关闭FTP连接出错");
//                                System.out.println("关闭FTP连接出错");
                                e.printStackTrace();
                            }
                        }

                        try{
                            log.debug("ftp服务器文件名称为：" + new String(ftpFileName.getBytes("UTF-8"), "UTF-8"));
                            log.debug("删除复制文件夹: " + journalPath.toString() + "-copy");
//                            System.out.println( "ftp服务器文件名称为：" + new String(ftpFileName.getBytes("UTF-8"), "UTF-8"));
//                            System.out.println("删除复制文件夹: " + journalPath.toString() + "-copy");
                            fileOperation.delete(new File(journalPath.toString() + "-copy"));
                            log.debug("删除压缩文件: " + journalPath.toString() + ".zip");
//                            System.out.println("删除压缩文件: " + journalPath.toString() + ".zip");
                            fileOperation.delete(new File(journalPath.toString() + ".zip"));
                            log.debug(journalPath.toString() + "处理完毕");
                            log.debug("****************************************");
//                            System.out.println(journalPath.toString() + "处理完毕");
//                            System.out.println("****************************************");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } // if时间判断条件结束
                else{
                    log.debug(journalPath.toString() + "不是昨天最新修改的文件夹");
                    log.debug("****************************************");
//                    System.out.println(journalPath.toString() + "不是昨天最新修改的文件夹");
//                    System.out.println("****************************************");
                }
            }
        }
    }

    public static void main(String[] args){
        ZipAndUpload zau = new ZipAndUpload();
        zau.zipAndUpload();
    }

}

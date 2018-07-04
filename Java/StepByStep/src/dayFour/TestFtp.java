package dayFour;

public class TestFtp {
    public static void main(String[] args){
        FtpTransport ftpTrs = new FtpTransport();
//        String ip = "139.159.247.154";
        String ip = "47.104.85.180";
        int port = 21;
//        String name = "test";
//        String pwd = "test";
        String name = "ftpuser";
        String pwd = "ftpuser";
//        ftpTrs.ftp1(ip,port,name,pwd);
//        ftpTrs.downLoad2(ip,port,name,pwd);
        ftpTrs.uploadFile(ip,port,name,pwd);
    }
}

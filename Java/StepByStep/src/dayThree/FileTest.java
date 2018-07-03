package dayThree;
import java.io.File;

public class FileTest {
    private static int count=0;

    public static void main(String[] args) {
        // 获取目录
        File file = new File("D:/media/test");
        // 把文件作为参数进行遍历
        market(file);
    }

    // 判断是目录还是文件内容。
    public static void market(File file){
        // 如果是文件，则打印出文件名
        System.out.println(makeSpace(count) + "-" + file.getName());
        // 获取这个路径的下所有的文件以及目录
        File[] fil = file.listFiles();
        // 是目录，则使用递归继续遍历
        if(fil != null && fil.length>0){
            count++;// 如果是目录加1
            // 说明是目录利用递归继续遍历
            for(File fi:fil){
                market(fi);
//                System.out.println(fi.getName());
            }
            count--;//上面遍历加1以后还得退回来，继续遍历
        }
    }

    // 让目录有层次感，必须有一个标记。
    public static String  makeSpace(int count){
        // 创建空格字符串
        String spack = "";
        for(int i = 0; i < count; i++){
            spack += " ";
        }
        return spack;
    }

}


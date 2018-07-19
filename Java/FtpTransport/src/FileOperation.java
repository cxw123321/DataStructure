import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperation {

    /**
     * 复制文件
     * 从源路径到目标文件夹路径，文件名保持一致
     * 如果目标文件夹不存在则自动创建
     * 如果文件已经存在则自动编号-copy n
     *
     * @param srcFile 源文件绝对路径
     * @param dstDir  目标文件夹绝对路径
     * @return 是否成功复制文件
     */
    public boolean copyFile(File srcFile, File dstDir) {
        // 如果源文件不存在或不是文件，则返回false
        if (!srcFile.exists() || srcFile.isDirectory()) {
            return false;
        }
        // 如果目标文件夹不存在，则创建文件夹
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        // 获取源文件文件名
        String oldFileName = srcFile.getName();
        // suffixPattern为后缀
        Pattern suffixPattern = Pattern.compile("\\.\\w+");
        Matcher matcher = suffixPattern.matcher(oldFileName);
        String nameBody;
        String suffix;
        if (matcher.find()) {
            // 获取文件名（不含文件类型）
            nameBody = oldFileName.substring(0, matcher.start());
            // 获取后缀名（是否包含"."?）
            suffix = oldFileName.substring(matcher.start());
        } else {
            // 没有后缀的情况
            nameBody = oldFileName;
            suffix = "";
        }
        int fileNumber = 0;
        File newFile = new File(dstDir, oldFileName);
        while (newFile.exists()) {
            fileNumber++;
            String newFileName = nameBody + "-copy" + fileNumber + suffix;
            newFile = new File(dstDir, newFileName);
        }
        try {
            FileChannel fileIn = new FileInputStream(srcFile).getChannel();
            FileChannel fileOut = new FileOutputStream(newFile).getChannel();
            fileIn.transferTo(0, fileIn.size(), fileOut);
            fileIn.close();
            fileOut.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 复制文件或文件夹
     * 如果目标文件夹不存在则自动创建
     * 如果文件或文件夹已经存在则自动编号-copy n
     *
     * @param src    源文件或文件夹绝对路径
     * @param dstDir 目标文件夹绝对路径
     * @return 是否成功复制文件或文件夹
     */
    public boolean copy(File src, File dstDir) {
        if (!src.exists()) {
            return false;
        }
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        if (src.isFile()) {// 文件
            copyFile(src, dstDir);
        } else {
            // 文件夹
            String oldSrcName = src.getName();
//            int srcNumber = 0;
            File newSrcDir = new File(dstDir, oldSrcName);
            while (newSrcDir.exists()) {
//                srcNumber++;
//                String newSrcName = oldSrcName + "-copy" + srcNumber;
                String newSrcName = oldSrcName + "-copy";
                newSrcDir = new File(dstDir, newSrcName);
            }
            newSrcDir.mkdirs();
            for (File srcSub : src.listFiles()) {
                // 递归复制源文件夹下子文件和文件夹
                copy(srcSub, newSrcDir);
            }
        }
        return true;
    }

    /**
     * 移动(剪切)文件
     *
     * @param srcFile
     * @param dstDir
     * @return
     */
    public boolean moveFile(File srcFile, File dstDir) {
        if (!srcFile.exists() || srcFile.isDirectory()) {
            return false;
        }
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        String oldFileName = srcFile.getName();
        File dstFile = new File(dstDir, oldFileName);
        if (srcFile.renameTo(dstFile)) {// 直接重命名绝对路径速度更快
            return true;
        } else {// 文件已经存在，需要自动编号复制再删除源文件
            copyFile(srcFile, dstDir);
            srcFile.delete();
        }
        return true;
    }

    /**
     * 移动文件或文件夹
     * 如果目标文件夹不存在则自动创建
     * 如果文件或文件夹已经存在则自动编号-copy n
     *
     * @param src    源文件或文件夹绝对路径
     * @param dstDir 目标文件夹绝对路径
     * @return 是否成功移动文件或文件夹
     */
    public boolean move(File src, File dstDir) {
        if (!src.exists()) {
            return false;
        }
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }
        if (src.isFile()) {// 文件
            moveFile(src, dstDir);
        } else {// 文件夹
            String oldSrcName = src.getName();
            int srcNumber = 0;
            File newSrcDir = new File(dstDir, oldSrcName);
            while (newSrcDir.exists()) {
                srcNumber++;
                String newSrcName = oldSrcName + "-copy" + srcNumber;
                newSrcDir = new File(dstDir, newSrcName);
            }
            newSrcDir.mkdirs();
            for (File srcSub : src.listFiles()) {
                move(srcSub, newSrcDir);// 递归移动源文件夹下子文件和文件夹
            }
            src.delete();
        }
        return true;
    }

    /**
     * 删除文件或文件夹
     *
     * @param src 源文件或文件夹绝对路径
     * @return 是否成功删除文件或文件夹
     */
    public boolean delete(File src) {
        if (!src.exists()) {
            return false;
        }
        if (src.isFile()) {
            src.delete();
        } else {
            for (File srcSub : src.listFiles()) {
                // 递归删除源文件夹下子文件和文件夹
                delete(srcSub);
            }
            src.delete();
        }
        return true;
    }

}
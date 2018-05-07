package gaoyanshan.client.test;

import android.util.Log;

/**
 * Created by Ciacho on 2018/3/23.
 */

public class NetFileData {

    private long fileSize = 0;// 文件长度应该long型数据，否则大于2GB的文件大小无法表达
    private String fileName = "$error";// 文件名称，不含目录信息,默认值用于表示文件出错
    private String filePath = ".\\";// 该文件对象所处的目录，默认值为当前相对目录
    private String fileSizeStr = "0";// 文件的大小，用字符串表示，能智能地选择B、KB、MB、GB来表达
    private boolean isDirectory = false;// true为文件夹，false为文件
    private String fileModifiedDate = "1970-01-01 00:00:00";// 文件最近修改日期，默认值为1970年基准时间

    public NetFileData(String fileInfo, String filePath) {
        String[] fileData = fileInfo.split(">");
        if (fileData.length == 4) {
            this.filePath = filePath;
            fileName = fileData[0];
            fileModifiedDate = fileData[1];
            fileSizeStr = format(fileData[2]);
            isDirectory = (fileData[3].equals("1"));

        }

    }

    private String format(String fileDatum) {

        double KB = 1024.0;
        double MB = KB * 1024.0;
        double GB = MB * 1024.0;
        if (Double.parseDouble(fileDatum) > GB) {
            return String.format("%.3fGB", Double.parseDouble(fileDatum)/GB);
        } else if (Double.parseDouble(fileDatum) > MB) {
            return String.format("%.2fMB", Double.parseDouble(fileDatum)/MB);
        } else if (Double.parseDouble(fileDatum) > KB) {
            return String.format("%.1fKB", Double.parseDouble(fileDatum)/KB);
        } else
            return String.format("%.4fK",Double.parseDouble(fileDatum));
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(String fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }
}

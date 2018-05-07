package gaoyanshan.client.data;

public class FileData {
    private String filename;
    private String filePath;
    private String fileSizeStr;
    private int isDirectory=0;
    private String fileModifileDate="1970-01-01";
    private String parentPath;
    private String suffix;
    private int level;

    public String getSuffix() {
        return suffix;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {

        if(parentPath!=null )
        {
            parentPath=parentPath.replace("\\","\\\\");
        }
        this.parentPath = parentPath;
    }

    public FileData(String filename, String filePath, String fileSizeStr, int isDirectory, String fileModifileDate) {

        this.filename = filename;
        this.filePath = filePath;
        this.fileSizeStr = fileSizeStr;
        this.isDirectory = isDirectory;
        this.fileModifileDate = fileModifileDate;
    }
    public FileData() {

    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {


        filePath=filePath.replace("\\","\\\\");
        this.filename = filename;
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

    public int getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(int isDirectory) {
        this.isDirectory = isDirectory;
    }

    public String getFileModifileDate() {
        return fileModifileDate;
    }

    public void setFileModifileDate(String fileModifileDate) {
        this.fileModifileDate = fileModifileDate;
    }

    @Override
    public String toString() {
        return "文件名："+getFilename()+" 文件路径："+getFilePath()+" 文件大小："+getFileSizeStr()+" fileModifileDate:"+getFileModifileDate()+"后缀："+getSuffix()
                +"父目录："+getParentPath()+"文件层级:"+getLevel()+"是否是文件夹"+isDirectory;
    }
}


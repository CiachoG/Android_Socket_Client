package gaoyanshan.client.download;

public class DownloadData {
    private String fileName;
    private String downloadLen;
    private int progress=0;
    private String ip;
    private String  port;
    private int downloadflag=0;
    private boolean isChecked=false;
    private boolean isPause=false;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getDownloadflag() {
        return downloadflag;
    }

    public void setDownloadflag(int downloadflag) {
        this.downloadflag = downloadflag;
    }

    public DownloadData(String fileName, String downloadLen, int progress, String ip, String port) {
        this.fileName = fileName;
        this.downloadLen = downloadLen;
        this.progress = progress;
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDownloadLen() {
        return downloadLen;
    }

    public void setDownloadLen(String downloadLen) {
        this.downloadLen = downloadLen;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}

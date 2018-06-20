package gaoyanshan.client.download;

public interface DownloadListener {

    void onProgress(String filename,int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}

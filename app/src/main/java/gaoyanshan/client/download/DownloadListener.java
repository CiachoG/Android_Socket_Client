package gaoyanshan.client.download;

public interface DownloadListener {

    void onProgress(int port,int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}

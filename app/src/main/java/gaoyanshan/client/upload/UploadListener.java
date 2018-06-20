package gaoyanshan.client.upload;

public interface UploadListener {

    void onProgress( int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();

}

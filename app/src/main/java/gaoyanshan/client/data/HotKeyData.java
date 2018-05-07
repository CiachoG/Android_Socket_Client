package gaoyanshan.client.data;

/**
 * Created by Ciacho on 2018/5/4.
 */

public class HotKeyData {
    private String hotkeyName="";
    private String hotkeyCmd="";
    public HotKeyData(String hotkeyName, String hotkeyCmd){
        this.hotkeyName=hotkeyName;
        this.hotkeyCmd=hotkeyCmd;
    }

    public String getHotkeyName() {
        return hotkeyName;
    }

    public void setHotkeyName(String hotkeyName) {
        this.hotkeyName = hotkeyName;
    }

    public String getHotkeyCmd() {
        return hotkeyCmd;
    }

    public void setHotkeyCmd(String hotkeyCmd) {
        this.hotkeyCmd = hotkeyCmd;
    }
}

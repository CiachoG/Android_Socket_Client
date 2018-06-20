package gaoyanshan.client.data;

import java.util.ArrayList;

/**
 * Created by Ciacho on 2018/5/4.
 */

public class Movie_HotKey {
    public ArrayList<HotKeyData> getHotkeyList(){
        ArrayList<HotKeyData> hotKeyDataArrayList=new ArrayList<>();

        HotKeyData h1=new HotKeyData("全屏/退出全屏","VK_ALT+VK_ENTER");
        hotKeyDataArrayList.add(h1);

        HotKeyData h2=new HotKeyData("播放/暂停","VK_SPACE");
        hotKeyDataArrayList.add(h2);

        HotKeyData h3=new HotKeyData("停止","VK_CONTROL+VK_S");
        hotKeyDataArrayList.add(h3);

        HotKeyData h4=new HotKeyData("快进","VK_RIGHT");
        hotKeyDataArrayList.add(h4);

        HotKeyData h5=new HotKeyData("快退","VK_LEFT");
        hotKeyDataArrayList.add(h5);

        HotKeyData h6=new HotKeyData("增大音量","VK_UP");
        hotKeyDataArrayList.add(h6);

        HotKeyData h7=new HotKeyData("减少音量","VK_DOWN");
        hotKeyDataArrayList.add(h7);

        HotKeyData h8=new HotKeyData("静音","VK_CONTROL+VK_8");
        hotKeyDataArrayList.add(h8);

        return hotKeyDataArrayList;
    }
}

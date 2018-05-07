package gaoyanshan.client.data;

import java.util.ArrayList;

/**
 * Created by Ciacho on 2018/5/4.
 */

public class PPT_HotKey {
    public ArrayList<HotKeyData> getHotkeyList(){
        ArrayList<HotKeyData> hotKeyDataArrayList=new ArrayList<>();

        HotKeyData h1=new HotKeyData("切换程序","VK_ALT+VK_TAB");
        hotKeyDataArrayList.add(h1);

        HotKeyData h2=new HotKeyData("ESC","VK_ESCAPE");
        hotKeyDataArrayList.add(h2);

        HotKeyData h3=new HotKeyData("上一页","VK_PAGE_UP");
        hotKeyDataArrayList.add(h3);

        HotKeyData h4=new HotKeyData("下一页","VK_PAGE_DOWN");
        hotKeyDataArrayList.add(h4);

        HotKeyData h5=new HotKeyData("从头放映","VK_F5");
        hotKeyDataArrayList.add(h5);

        HotKeyData h6=new HotKeyData("当前放映","VK_SHIFT+VK_F5");
        hotKeyDataArrayList.add(h6);

        HotKeyData h7=new HotKeyData("退出程序","VK_SHIFT+VK_F4");
        hotKeyDataArrayList.add(h7);

        HotKeyData h8=new HotKeyData("黑屏","VK_B");
        hotKeyDataArrayList.add(h8);

        HotKeyData h9=new HotKeyData("亮屏","VK_W");
        hotKeyDataArrayList.add(h9);

        return hotKeyDataArrayList;
    }
}

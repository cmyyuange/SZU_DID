package org.example;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowListen extends WindowAdapter {
    JFrame frame;
    public WindowListen(JFrame frame){
        this.frame=frame;
    }
    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("窗口打开！");
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int Option = JOptionPane.showConfirmDialog(frame,"是否要退出程序？","退出程序",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
        if (Option == JOptionPane.OK_OPTION){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }else {
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("窗口已经关闭!");
    }

    @Override
    public void windowActivated(WindowEvent e) {
        System.out.println("窗口激活！");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("窗口变为未激活！");
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        System.out.println("窗口取消最小化！");
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        System.out.println("窗口聚焦！");
    }

    @Override
    public void windowIconified(WindowEvent e) {
        System.out.println("窗口最小化！");
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        System.out.println("窗口失去聚焦！");
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        System.out.println("窗口状态变化！");
    }
}

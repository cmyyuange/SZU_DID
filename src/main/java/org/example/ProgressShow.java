package org.example;

import javax.swing.*;
import java.awt.*;

public class ProgressShow {

    JProgressBar progressBar;
    JFrame progress;

    public void show(){
        // 进度条设置
        progress = new JFrame();
        progress.setLayout(new BorderLayout());
        progress.setResizable(false);
        progress.setAlwaysOnTop(true);
        JLabel text = new JLabel("深圳大学DID",JLabel.CENTER);
        progress.add(text,BorderLayout.NORTH);
        ImageIcon szu = new ImageIcon("src\\main\\resources\\image\\soogif.gif");
        JLabel label = new JLabel(szu);
        progress.add(label,BorderLayout.CENTER);
        progress.setUndecorated(true);
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setString("正在与服务器进行连接......");
        progress.add(progressBar,BorderLayout.SOUTH);
        progress.pack();
        progress.setVisible(true);
        progress.setLocationRelativeTo(null);

        // 进度条进度
        Thread progressThread = new ProgressThread(progressBar);
        progressThread.start();
    }

    public void setValue(int value) {
        progressBar.setValue(value);
    }

    public void close() {
        progress.dispose();
    }
}

package org.example;

import com.webank.weid.rpc.*;
import com.webank.weid.service.impl.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 进度条设置
        JFrame progress = new JFrame();
        progress.setLayout(new BorderLayout());
        progress.setResizable(false);
        progress.setAlwaysOnTop(true);
        Image szu = new ImageIcon("src\\main\\resources\\szu.png").getImage();
        progress.setIconImage(szu);
        progress.setUndecorated(true);
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setString("正在与服务器进行连接......");
        progress.add(progressBar,BorderLayout.SOUTH);
        progress.add(new JLabel(new ImageIcon("src\\main\\resources\\szu.png")),BorderLayout.CENTER);
        progress.pack();
        progress.setVisible(true);
        progress.setLocationRelativeTo(null);

        // 进度条进度
        Thread progressThread = new ProgressThread(progressBar);
        progressThread.start();
        EvidenceService evidenceService = new EvidenceServiceImpl();
        progressBar.setValue(80);
        Thread.sleep(100);
        AuthorityIssuerService authorityIssuerService = new AuthorityIssuerServiceImpl();
        progressBar.setValue(85);
        Thread.sleep(100);
        CptService cptService = new CptServiceImpl();
        progressBar.setValue(90);
        Thread.sleep(100);
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        progressBar.setValue(95);
        Thread.sleep(100);
        WeIdService weIdService = new WeIdServiceImpl();
        DIDService didService = new DIDService(weIdService);
        progressBar.setValue(100);
        Thread.sleep(100);

        // 进度条关闭并打开主窗口
        progress.dispose();
        new Windows(evidenceService,authorityIssuerService,cptService,credentialPojoService,didService);
    }
}
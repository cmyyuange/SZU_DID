package org.example;

import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.*;
import com.webank.weid.service.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Windows extends JFrame {
    public Windows() throws InterruptedException {

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
        setVisible(true);

        // 窗体设置
        addWindowListener(new WindowListen(this));
        setSize(500,400);
        setResizable(false);
        setIconImage(szu);
        setTitle("深圳大学DID");
        setLocationRelativeTo(null);

        // 布局设置
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        add(mainPanel);

        // 主菜单布局
        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BorderLayout());
        JPanel buttonPanel =new JPanel();
        mainMenu.add(buttonPanel,BorderLayout.SOUTH);
        JButton DIDServiceButton = new JButton("DID服务");
        DIDServiceButton.addActionListener(e -> cardLayout.show(mainPanel,"DID菜单"));
        buttonPanel.add(DIDServiceButton);
        mainPanel.add("主菜单",mainMenu);

        // DID菜单布局
        JPanel DIDPanel = new JPanel();
        DIDPanel.setLayout(new BorderLayout());
        JPanel DIDbuttonPanel =new JPanel();
        DIDPanel.add(DIDbuttonPanel,BorderLayout.SOUTH);
        JButton createDIDButton = new JButton("创建DID");
        createDIDButton.addActionListener(e -> {
            cardLayout.show(mainPanel,"创建DID菜单");
        });
        JButton returnButton = new JButton("返回主菜单");
        returnButton.addActionListener(e -> cardLayout.show(mainPanel,"主菜单"));
        JButton countDIDButton = new JButton("查询DID数量");
        countDIDButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"DID的总数为：" + didService.getCount(),"DID数量查询结果",JOptionPane.INFORMATION_MESSAGE);
        });
        JButton DIDDocumentButton = new JButton("获取DID文档");
        DIDDocumentButton.addActionListener(e -> {
        });
        DIDbuttonPanel.add(createDIDButton);
        DIDbuttonPanel.add(countDIDButton);
        DIDbuttonPanel.add(DIDDocumentButton);
        DIDbuttonPanel.add(returnButton);
        mainPanel.add("DID菜单",DIDPanel);

        // 创建DID菜单布局
        JPanel createDIDPanel = new JPanel();
        createDIDPanel.setLayout(new BorderLayout());
        JPanel createButtonPanel =new JPanel();
        createDIDPanel.add(createButtonPanel,BorderLayout.SOUTH);
        JButton noKeyButton = new JButton("无秘钥创建DID");
        noKeyButton.addActionListener(e -> {
            String[] option = {"学生","教师","课程"};
            int n = JOptionPane.showOptionDialog(this,"请选择一个角色","选择角色",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
            if (n == 0){
                String[] optionStudent = {"身份","学业"};
                int m = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionStudent,optionStudent[0]);
                if (m == 0) {

                }else {

                }
            } else if (n == 1) {
                String[] optionTeacher = {"身份","教学"};
                int j = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionTeacher,optionTeacher[0]);
                if (j == 0) {

                }else {

                }
            }else {
                String key = JOptionPane.showInputDialog(this,"请输入教务处秘钥：","教务处登录",JOptionPane.INFORMATION_MESSAGE);
                if (key.equals("22345678")){
                    JOptionPane.showMessageDialog(this,"登陆成功！","成功",JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(this,"秘钥错误！","失败",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        JButton KeyButton = new JButton("有秘钥创建DID");
        KeyButton.addActionListener(e -> {
            createButtonPanel.setVisible(false);
        });
        JButton returnButton1 = new JButton("返回主菜单");
        returnButton1.addActionListener(e -> cardLayout.show(mainPanel,"主菜单"));
        createButtonPanel.add(noKeyButton);
        createButtonPanel.add(KeyButton);
        createButtonPanel.add(returnButton1);
        mainPanel.add(createDIDPanel,"创建DID菜单");
    }
}

class ProgressThread extends Thread{
    JProgressBar progressBar;
    public ProgressThread(JProgressBar progressBar){
        this.progressBar = progressBar;
    }
    public void run(){
        for (int i = 0; i < 80; i++) {
            progressBar.setValue(i);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
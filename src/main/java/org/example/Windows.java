package org.example;

import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.ServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
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
        JButton curriculumButton = new JButton("课程服务");
        curriculumButton.addActionListener(e -> cardLayout.show(mainPanel,"课程服务菜单"));
        buttonPanel.add(curriculumButton);
        mainPanel.add("主菜单",mainMenu);

        // DID菜单布局
        JPanel DIDPanel = new JPanel();
        DIDPanel.setLayout(new BorderLayout());
        JPanel DIDbuttonPanel =new JPanel();
        DIDPanel.add(DIDbuttonPanel,BorderLayout.SOUTH);
        JButton createDIDButton = new JButton("创建DID");
        createDIDButton.addActionListener(e -> cardLayout.show(mainPanel,"创建DID菜单"));
        JButton returnButton = new JButton("返回主菜单");
        returnButton.addActionListener(e -> cardLayout.show(mainPanel,"主菜单"));
        JButton countDIDButton = new JButton("查询DID数量");
        countDIDButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,"DID的总数为：" + didService.getCount(),"DID数量查询结果",JOptionPane.INFORMATION_MESSAGE);
        });
        JButton DIDDocumentButton = new JButton("获取DID文档");
        DIDDocumentButton.addActionListener(e -> {
            String DID = JOptionPane.showInputDialog(this,"请输入要查询的DID：","查询DID",JOptionPane.INFORMATION_MESSAGE);
            if (DID != null){
                String didDocument = didService.getDIDDocument(DID);
                showDIDDocument(didDocument);
            }
        });
        DIDbuttonPanel.add(createDIDButton);
        DIDbuttonPanel.add(countDIDButton);
        DIDbuttonPanel.add(DIDDocumentButton);
        DIDbuttonPanel.add(returnButton);
        mainPanel.add("DID菜单",DIDPanel);

        // 课程服务菜单布局
        JPanel curriculumPanel = new JPanel();
        curriculumPanel.setLayout(new BorderLayout());
        JPanel curriculumTextPanel = new JPanel();
        JTextArea showDID = new JTextArea();
        curriculumPanel.add(showDID,BorderLayout.CENTER);
        curriculumTextPanel.setLayout(new GridLayout(3,3));
        curriculumTextPanel.add(new JLabel("          课程DID："));
        JTextField text1 = new JTextField();
        curriculumTextPanel.add(text1);
        JButton check1 = new JButton("查询");
        check1.addActionListener(e -> showDID.setText(didService.getDIDDocument(text1.getText())));
        curriculumTextPanel.add(check1);
        curriculumTextPanel.add(new JLabel("          选课人DID："));
        JTextField text2 = new JTextField();
        curriculumTextPanel.add(text2);
        JButton check2 = new JButton("查询");
        check2.addActionListener(e -> showDID.setText(didService.getDIDDocument(text2.getText())));
        curriculumTextPanel.add(check2);
        curriculumPanel.add(curriculumTextPanel,BorderLayout.NORTH);
        curriculumTextPanel.add(new JLabel("选课人DID私钥"));
        JPasswordField password = new JPasswordField();
        curriculumTextPanel.add(password);
        JPanel curriculumMenuButton = new JPanel();
        JButton yes = new JButton("确定选课");
        yes.addActionListener(e -> {
            // TODO:课程也需要写入
            boolean result = didService.addMessage(text1.getText(), text2.getText(), new String(password.getPassword()));
            if (result) {
                JOptionPane.showMessageDialog(this,"选课成功！","选课成功！",JOptionPane.INFORMATION_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(this,"选课失败！","选课失败！",JOptionPane.WARNING_MESSAGE);
            }
        });
        curriculumMenuButton.add(yes);
        curriculumMenuButton.add(returnButton);
        curriculumPanel.add(curriculumMenuButton,BorderLayout.SOUTH);
        mainPanel.add("课程服务菜单",curriculumPanel);

        // 创建DID菜单布局
        JPanel createDIDPanel = new JPanel();
        createDIDPanel.setLayout(new BorderLayout());
        JPanel createButtonPanel =new JPanel();
        createDIDPanel.add(createButtonPanel,BorderLayout.SOUTH);
        JButton noKeyButton = new JButton("无秘钥创建DID");
        noKeyButton.addActionListener(e -> {
            String[] option = {"学生","教师","课程"};
            int n = JOptionPane.showOptionDialog(this,"请选择一个角色","选择角色",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,-1);
            if (n == 0){
                String[] optionStudent = {"身份","学业"};
                int m = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionStudent,-1);
                if (m == 0) {
                    createStudentDialog(didService);
                }else if (m == 1){
                    String data = "学生课程DID";
                    didService.create(data);
                    // TODO:对返回的数据进行展示，和生成文件保存
                }
            } else if (n == 1) {// 教师
                String[] optionTeacher = {"身份","教学"};
                int j = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionTeacher,-1);
                if (j == 0) {
                    createTeacherDialog(didService);
                }else if(j == 1){
                    String data = "老师教学DID";
                    didService.create(data);
                    // TODO:对返回的数据进行展示，和生成文件保存
                }
            }else if(n == 2){
                JPasswordField passwordField = new JPasswordField();
                Object[] message = {"请输入教务处秘钥：",passwordField};
                JOptionPane.showMessageDialog(this, message, "教务处登录",  JOptionPane.INFORMATION_MESSAGE);
                if (new String(passwordField.getPassword()).equals("123456")){
                    JOptionPane.showMessageDialog(this,"登陆成功！","成功",JOptionPane.INFORMATION_MESSAGE);
                    createCurriculumDialog(didService);
                }else {
                    JOptionPane.showMessageDialog(this,"秘钥错误！","失败",JOptionPane.INFORMATION_MESSAGE);
                }
                passwordField.setText("");
            }
        });

        JButton KeyButton = new JButton("有秘钥创建DID");
        KeyButton.addActionListener(e -> {
            String[] option = {"学生","教师"};
            int n = JOptionPane.showOptionDialog(this,"请选择一个角色","选择角色",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,-1);
            if (n == 0){
                String[] optionStudent = {"身份","学业"};
                int m = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionStudent,-1);
                if (m == 0) {
                    // TODO:1
                }else {
                    // TODO:2
                }
            } else if (n == 1) {
                String[] optionTeacher = {"身份","教学"};
                int j = JOptionPane.showOptionDialog(this,"请选择一个种类","选择种类",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,optionTeacher,-1);
                if (j == 0) {
                    // TODO:3
                }else {
                    // TODO:4
                }
            }
        });
        JButton returnButton1 = new JButton("返回主菜单");
        returnButton1.addActionListener(e -> cardLayout.show(mainPanel,"主菜单"));
        createButtonPanel.add(noKeyButton);
        createButtonPanel.add(KeyButton);
        createButtonPanel.add(returnButton1);
        mainPanel.add(createDIDPanel,"创建DID菜单");
    }

    void createStudentDialog(DIDService didService){
        JDialog dialog = new JDialog(this,"输入学生信息",true);
        dialog.setSize(400,200);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(5,2));
        textPanel.add(new JLabel("                    姓名："));
        JTextField nameField = new JTextField();
        textPanel.add(nameField);
        textPanel.add(new JLabel("                    学号："));
        JTextField numberField = new JTextField();
        textPanel.add(numberField);
        textPanel.add(new JLabel("                    专业："));
        JComboBox<String> subject = new JComboBox<>();
        subject.setMaximumRowCount(4);
        subject.addItem("通信工程");
        subject.addItem("集成电路");
        subject.addItem("计算机科学与技术");
        subject.addItem("软件工程");
        subject.addItem("信息与通信工程");
        subject.addItem("金融科技");
        textPanel.add(subject);
        textPanel.add(new JLabel("                    出生日期："));
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,3));
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.setMaximumRowCount(4);
        for (int i = 1990; i < 2022; i++) {
            yearCombo.addItem(i + "年");
        }
        JComboBox<String> monthCombo = new JComboBox<>();
        monthCombo.setMaximumRowCount(4);
        for (int i = 1; i < 13; i++) {
            monthCombo.addItem(i + "月");
        }
        JComboBox<String> dayCombo = new JComboBox<>();
        dayCombo.setMaximumRowCount(4);
        for (int i = 1; i < 32; i++) {
            dayCombo.addItem(i + "日");
        }
        dataPanel.add(yearCombo);
        dataPanel.add(monthCombo);
        dataPanel.add(dayCombo);
        textPanel.add(dataPanel);
        textPanel.add(new JLabel("                     性别："));
        ButtonGroup genderGroup = new ButtonGroup();
        JPanel genderPanel = new JPanel();
        JRadioButton man = new JRadioButton("男");
        JRadioButton girl = new JRadioButton("女");
        genderGroup.add(man);
        genderGroup.add(girl);
        genderPanel.add(man);
        genderPanel.add(girl);
        textPanel.add(genderPanel);

        JPanel panelButton = new JPanel();
        JButton panel1Button1= new JButton("确定");
        panel1Button1.addActionListener(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String job = (String) subject.getSelectedItem();
            String year = (String) yearCombo.getSelectedItem();
            String month = (String) monthCombo.getSelectedItem();
            String day = (String) dayCombo.getSelectedItem();
            String gender = man.isSelected() ?  "男" : "女";
            String data = "学生证" + "\n" +
                    "名字:" + name + "\n" +
                    "学号:" + number + "\n" +
                    "专业:" + job + "\n" +
                    "出生日期:" + year + month + day + "\n" +
                    "性别:" + gender;

            didService.create(data);
            // TODO:对返回的数据进行展示，和生成文件保存
        });
        JButton panel1Button2= new JButton("取消");
        panel1Button2.addActionListener(event -> dialog.dispose());
        panelButton.add(panel1Button1);
        panelButton.add(panel1Button2);

        panel.add(textPanel,BorderLayout.CENTER);
        panel.add(panelButton,BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    void createTeacherDialog(DIDService didService){
        JDialog dialog = new JDialog(this,"输入教师信息",true);
        dialog.setSize(400,200);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(4,2));
        textPanel.add(new JLabel("                    姓名："));
        JTextField nameField = new JTextField();
        textPanel.add(nameField);
        textPanel.add(new JLabel("                    工号："));
        JTextField numberField = new JTextField();
        textPanel.add(numberField);
        textPanel.add(new JLabel("                    出生日期："));
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,3));
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.setMaximumRowCount(4);
        for (int i = 1990; i < 2022; i++) {
            yearCombo.addItem(i + "年");
        }
        JComboBox<String> monthCombo = new JComboBox<>();
        monthCombo.setMaximumRowCount(4);
        for (int i = 1; i < 13; i++) {
            monthCombo.addItem(i + "月");
        }
        JComboBox<String> dayCombo = new JComboBox<>();
        dayCombo.setMaximumRowCount(4);
        for (int i = 1; i < 32; i++) {
            dayCombo.addItem(i + "日");
        }
        dataPanel.add(yearCombo);
        dataPanel.add(monthCombo);
        dataPanel.add(dayCombo);
        textPanel.add(dataPanel);
        textPanel.add(new JLabel("                     性别："));
        ButtonGroup genderGroup = new ButtonGroup();
        JPanel genderPanel = new JPanel();
        JRadioButton man = new JRadioButton("男");
        JRadioButton girl = new JRadioButton("女");
        genderGroup.add(man);
        genderGroup.add(girl);
        genderPanel.add(man);
        genderPanel.add(girl);
        textPanel.add(genderPanel);

        JPanel panelButton = new JPanel();
        JButton panel1Button1= new JButton("确定");
        panel1Button1.addActionListener(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String year = (String) yearCombo.getSelectedItem();
            String month = (String) monthCombo.getSelectedItem();
            String day = (String) dayCombo.getSelectedItem();
            String gender = man.isSelected() ?  "男" : "女";
            String data = "教师证" + "\n" +
                    "名字:" + name + "\n" +
                    "工号:" + number + "\n" +
                    "出生日期:" + year + month + day + "\n" +
                    "性别:" + gender;

            didService.create(data);
            // TODO:对返回的数据进行展示，和生成文件保存
        });
        JButton panel1Button2= new JButton("取消");
        panel1Button2.addActionListener(event -> dialog.dispose());
        panelButton.add(panel1Button1);
        panelButton.add(panel1Button2);

        panel.add(textPanel,BorderLayout.CENTER);
        panel.add(panelButton,BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    void createCurriculumDialog(DIDService didService){
        JDialog dialog = new JDialog(this,"输入课程信息",true);
        dialog.setSize(400,200);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // TODO:课程信息改进
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(4,2));
        textPanel.add(new JLabel("                    课程名称："));
        JTextField nameField = new JTextField();
        textPanel.add(nameField);
        textPanel.add(new JLabel("                    工号："));
        JTextField numberField = new JTextField();
        textPanel.add(numberField);
        textPanel.add(new JLabel("                    开课学期："));
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(1,2));
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.setMaximumRowCount(4);
        for (int i = 2022; i < 2050; i++) {
            yearCombo.addItem(i + "年");
        }
        JComboBox<String> semesterCombo = new JComboBox<>();
        for (int i = 1; i < 3; i++) {
            semesterCombo.addItem("第" + i + "学期");
        }
        dataPanel.add(yearCombo);
        dataPanel.add(semesterCombo);
        textPanel.add(dataPanel);
        textPanel.add(new JLabel("                     类别："));
        ButtonGroup categoryGroup = new ButtonGroup();
        JPanel categoryPanel = new JPanel();
        JRadioButton compulsory = new JRadioButton("必修");
        JRadioButton elective = new JRadioButton("选修");
        categoryGroup.add(compulsory);
        categoryGroup.add(elective);
        categoryPanel.add(compulsory);
        categoryPanel.add(elective);
        textPanel.add(categoryPanel);

        JPanel panelButton = new JPanel();
        JButton panel1Button1= new JButton("确定");
        panel1Button1.addActionListener(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String year = (String) yearCombo.getSelectedItem();
            String semester = (String) semesterCombo.getSelectedItem();
            String category = compulsory.isSelected() ?  "必修" : "选修";
            String data = "大学课程" + "\n" +
                    "课程名称:" + name + "\n" +
                    "工号:" + number + "\n" +
                    "开课学期:" + year + semester + "\n" +
                    "类别:" + category;

            didService.create(data);
            // TODO:对返回的数据进行展示，和生成文件保存
        });
        JButton panel1Button2= new JButton("取消");
        panel1Button2.addActionListener(event -> dialog.dispose());
        panelButton.add(panel1Button1);
        panelButton.add(panel1Button2);

        panel.add(textPanel,BorderLayout.CENTER);
        panel.add(panelButton,BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    void showDIDDocument(String didDocument){
        JDialog dialog = new JDialog(this,"DID Document",true);
        dialog.setSize(400,200);
        dialog.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        // TODO:展示DID Document
    }
}
package org.example;

import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.*;
import com.webank.weid.service.BaseService;
import com.webank.weid.suite.api.persistence.inf.Persistence;
import com.webank.weid.util.DataToolUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


public class Windows {

    String role = "";// 学生,教师,教务处之一
    String user = "";// 用户名
    String adminKey;// 教务处秘钥

    public Windows(Persistence persistence,EvidenceService evidenceService, AuthorityIssuerService authorityIssuerService, CptService CPTService, CredentialPojoService credentialPojoService, WeIdService weIdService,Stage primaryStage) {

        DIDService didService = new DIDService(weIdService);
        CPTService cptService = new CPTService(CPTService);
        credentialPojo credentialPojo = new credentialPojo(credentialPojoService);

        File account = new File("src\\main\\resources\\account");
        // 初始化账户信息文件夹
        if (!account.exists()) {
            boolean result = account.mkdirs();
            if (!result) {
                alertShow("account文件创建失败！");
            }
        }
        // TODO:窗体设置
        Image szu= new Image("image/szu.png");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(szu);
        primaryStage.setOnCloseRequest(new WindowListen());
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("深圳大学DID");

        // TODO:布局设置
        AnchorPane anchorPane = new AnchorPane();
        AnchorPane mainScenePane = new AnchorPane();
        AnchorPane DIDPanel = new AnchorPane();
        AnchorPane curriculumPanel = new AnchorPane();
        AnchorPane createDIDPanel = new AnchorPane();
        AnchorPane CPTPanel = new AnchorPane();
        AnchorPane credentialPanel = new AnchorPane();
        anchorPane.setBackground(new Background(new BackgroundImage(new Image("image/log.png"),null,null,null,null)));
        Scene rootScene = new Scene(anchorPane,500,400);
        Scene mainScene = new Scene(mainScenePane,500,400);
        Scene DIDPanelScene = new Scene(DIDPanel,500,400);
        Scene curriculumScene = new Scene(curriculumPanel,600,400);
        Scene createDIDScene = new Scene(createDIDPanel,500,400);
        Scene CPTScene = new Scene(CPTPanel,500,400);
        Scene credentialScene = new Scene(credentialPanel,500,400);
        primaryStage.setScene(rootScene);

        // TODO:登录页面
        AnchorPane log = new AnchorPane();
        log.setId("log1");
        log.setPrefSize(300,200);
        log.setLayoutX(100);
        log.setLayoutY(100);
        Label name = new Label("用户名");
        name.setLayoutX(140);
        name.setLayoutY(150);
        Label password = new Label("密码");
        password.setLayoutX(150);
        password.setLayoutY(200);
        TextField nameField = new TextField();
        nameField.setLayoutX(200);
        nameField.setLayoutY(150);
        nameField.setOpacity(0.5);
        PasswordField passwordField = new PasswordField();
        passwordField.setLayoutX(200);
        passwordField.setLayoutY(200);
        passwordField.setOpacity(0.5);
        Button signIn = new Button("登录");
        signIn.setPrefSize(100,30);
        signIn.setLayoutX(130);
        signIn.setLayoutY(240);
        signIn.setOnAction(event -> {
            user = nameField.getText();
            File file = new File("src\\main\\resources\\account\\" + user + ".txt");
            String userPassword = "";
            if (file.exists()){
                try {
                    // 获取用户密码和role
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    userPassword = bufferedReader.readLine();
                    role = bufferedReader.readLine().contains("学生") ? "学生" : "教师";
                    System.out.println(role);
                    bufferedReader.close();
                }catch (Exception e){
                    alertShow("文件读取失败！");
                    e.printStackTrace();
                }
                userPassword = userPassword.replace("\n","");
                if (passwordField.getText().equals(userPassword.substring(9))) {
                    primaryStage.setScene(mainScene);
                }else {
                    alertShow("用户名与密码不匹配！");
                }
            }else{
                alertShow("用户名不存在！");
            }
        });
        Button signUp = new Button("注册");
        signUp.setPrefSize(100,30);
        signUp.setLayoutX(270);
        signUp.setLayoutY(240);
        signUp.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,400,300));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("注册");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            AnchorPane anchorPane2 = new AnchorPane();
            anchorPane2.setId("log2");
            anchorPane2.setPrefSize(300,240);
            anchorPane2.setLayoutX(50);
            anchorPane2.setLayoutY(30);
            Label name1 = new Label("用户名");
            name1.setLayoutX(80);
            name1.setLayoutY(60);
            Label password1 = new Label("密码");
            password1.setLayoutX(90);
            password1.setLayoutY(110);
            Label role = new Label("角色");
            role.setLayoutX(90);
            role.setLayoutY(160);
            TextField nameField1 = new TextField();
            nameField1.setLayoutX(160);
            nameField1.setLayoutY(60);
            nameField1.setOpacity(0.5);
            PasswordField passwordField1 = new PasswordField();
            passwordField1.setLayoutX(160);
            passwordField1.setLayoutY(110);
            passwordField1.setOpacity(0.5);
            ChoiceBox<String> roles = new ChoiceBox<>(FXCollections.observableArrayList("学生","教师"));
            roles.setPrefSize(160,20);
            roles.setLayoutX(160);
            roles.setLayoutY(160);
            roles.setOpacity(0.5);
            Button button = new Button("注册");
            button.setPrefSize(200,30);
            button.setLayoutX(100);
            button.setLayoutY(200);
            button.setOnAction(event1 -> {
                // 注册新账户
                if ((!nameField1.getText().equals("")) && (!passwordField1.getText().equals("") && (roles.getValue() != null))) {
                    File user = new File("src\\main\\resources\\account\\" + nameField1.getText() + ".txt");
                    if (!user.exists()) {
                        try {
                            // 写入新账户秘钥和角色信息
                            boolean result = user.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(user));
                            bufferedWriter.write("password:" + passwordField1.getText() + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.write("role:" + roles.getValue() + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            if (!result) {
                                alertShow("注册失败，文件创建失败！");
                            }else {
                                alertShow("注册成功！");
                            }
                            stage.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        alertShow("注册失败，用户名已存在！");
                    }
                }else {
                    alertShow("用户名或密码或角色为空！");
                }
            });
            anchorPane1.getChildren().addAll(anchorPane2,name1,password1,button,nameField1,passwordField1,role,roles);
            anchorPane1.getStylesheets().add("css/log.css");
            anchorPane2.getStylesheets().add("css/log.css");
            stage.show();
        });
        Label logTitle = new Label("SZU-DID");
        logTitle.setStyle("-fx-font-size:22;-fx-text-fill:rgba(12, 80, 38, 0.7);-fx-font-weight:bold;-fx-font-family:\"Times New Roman\"");
        logTitle.setLayoutX(210);
        logTitle.setLayoutY(110);
        Button deanOffice = new Button("教务处登录");
        deanOffice.setPrefSize(200,30);
        deanOffice.setLayoutX(150);
        deanOffice.setLayoutY(330);
        deanOffice.setOpacity(0.8);
        deanOffice.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,400,200));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("教务处登录");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            AnchorPane anchorPane2 = new AnchorPane();
            anchorPane2.setId("log2");
            anchorPane2.setPrefSize(300,140);
            anchorPane2.setLayoutX(50);
            anchorPane2.setLayoutY(30);
            Label name1 = new Label("秘钥");
            name1.setLayoutX(80);
            name1.setLayoutY(70);
            TextField Field1 = new TextField();
            Field1.setPrefSize(200,25);
            Field1.setLayoutX(120);
            Field1.setLayoutY(70);
            Field1.setOpacity(0.5);
            Button button = new Button("登录");
            button.setPrefSize(200,30);
            button.setLayoutX(100);
            button.setLayoutY(120);
            button.setOnAction(event1 -> {
                // TODO:教务处登录逻辑
                CryptoSuite cryptoSuite = new CryptoSuite(((Client) BaseService.getClient()).getCryptoType());
                CryptoKeyPair cryptokeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(new BigInteger(Field1.getText()));
                String publicKey = DataToolUtils.hexStr2DecStr(cryptokeyPair.getHexPublicKey());
                ResponseData<String> response = weIdService.getWeIdDocumentJson("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1");
                int index = response.getResult().indexOf("\"publicKey\"");
                if (publicKey.equals(response.getResult().substring(index + 15,index + 169))) {
                    user = "deanOffice";
                    role = "教务处";
                    adminKey = Field1.getText();
                    CurriculumThread curriculumThread = new CurriculumThread(persistence,didService,credentialPojo,adminKey);
                    curriculumThread.start();
                    File userTxt = new File("src\\main\\resources\\account\\deanOffice.txt");
                    if (!userTxt.exists()){
                        try {
                            userTxt.createNewFile();
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                            bufferedWriter.write("password:" + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.write("role:" + "教务处" + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    stage.close();
                    primaryStage.setScene(mainScene);
                }else {
                    stage.close();
                    alertShow("秘钥错误！");
                }
            });
            anchorPane1.getChildren().addAll(anchorPane2,name1,button,Field1);
            anchorPane1.getStylesheets().add("css/log.css");
            anchorPane2.getStylesheets().add("css/log.css");
            stage.show();
        });
        anchorPane.getChildren().add(log);
        anchorPane.getStylesheets().add("css/log.css");
        anchorPane.getChildren().addAll(signIn,signUp,logTitle,name,password,nameField,passwordField,deanOffice);

        // TODO:课程服务菜单
        TextArea showDID = new TextArea();
        showDID.setPrefSize(595,255);
        showDID.setEditable(false);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(600,260);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(100);
        scrollPane.setContent(showDID);
        Label label1 = new Label("选课人DID：");
        label1.setLayoutX(20);
        label1.setLayoutY(20);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        ChoiceBox<String> DIDs = new ChoiceBox<>(observableList);
        DIDs.setLayoutX(100);
        DIDs.setLayoutY(20);
        Button check1 = new Button("查询");
        check1.setLayoutX(530);
        check1.setLayoutY(15);
        check1.setOnAction(event -> showDID.setText(didService.getDIDDocument(DIDs.getValue())));
        Label label2 = new Label("课程DID：");
        label2.setLayoutX(20);
        label2.setLayoutY(60);
        TextField textField2 = new TextField();
        textField2.setPrefSize(400,25);
        textField2.setLayoutX(100);
        textField2.setLayoutY(60);
        Button check2 = new Button("查询");
        check2.setLayoutX(530);
        check2.setLayoutY(55);
        check2.setOnAction(event -> {showDID.setText(didService.getDIDDocument(textField2.getText()));});
        Button yes = new Button("确定选课");
        yes.setPrefSize(200,30);
        yes.setLayoutX(75);
        yes.setLayoutY(363);
        yes.setOnAction(event -> {
            // 选课逻辑
            int i = 1;
            // 获取首个空的
            while (!persistence.get("domain.defaultInfo",String.valueOf(i)).getResult().equals("")) {
                i++;
            }
            if (DIDs.getValue() == null) {
                alertShow("未选择选课人DID！");
            }else if (textField2.getText().equals("")) {
                alertShow("未输入课程DID！");
            }else {
                persistence.addOrUpdate("domain.defaultInfo",String.valueOf(i), DIDs.getValue() + "&" + textField2.getText());
            }
            int start = didService.getDIDDocument(textField2.getText()).indexOf("名称:");
            int end = didService.getDIDDocument(textField2.getText()).indexOf("*");
            String priKey = "";
            File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new FileReader(userTxt));
                StringBuilder message = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    message.append(line).append("\n");
                }
                String temp = message.toString();
                // 获取课程DID对应的私钥
                temp = temp.substring(temp.indexOf("课程weId=" + DIDs.getValue()));
                priKey = temp.substring(temp.indexOf("privateKey=") + 11,temp.indexOf("privateKey=") + 11 + 77);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean result = didService.addMessage(DIDs.getValue(),priKey,"深圳大学课程","课程名称:" + didService.getDIDDocument(textField2.getText()).substring(start,end)  + " &DID:" + textField2.getText());
            if (result) {
                alertShow("选课成功！");
            }else {
                alertShow("选课失败！");
            }
        });
        Button returnButton2 = new Button("返回主菜单");
        returnButton2.setPrefSize(200,30);
        returnButton2.setLayoutX(325);
        returnButton2.setLayoutY(363);
        returnButton2.setOnAction(event -> primaryStage.setScene(mainScene));
        curriculumPanel.getChildren().addAll(scrollPane,label1,DIDs,check1,label2,textField2,check2,yes,returnButton2);
        curriculumPanel.getStylesheets().add("css/button.css");

        // TODO:主菜单
        Button DIDServiceButton = new Button("DID服务");
        DIDServiceButton.setPrefSize(200,30);
        DIDServiceButton.setLayoutX(150);
        DIDServiceButton.setLayoutY(50);
        DIDServiceButton.setOnAction(event -> primaryStage.setScene(DIDPanelScene));
        Button curriculumButton = new Button("课程服务");
        curriculumButton.setPrefSize(200,30);
        curriculumButton.setLayoutX(150);
        curriculumButton.setLayoutY(100);
        curriculumButton.setOnAction(event -> {
            // 获取所有课程DID
            textField2.setText("");
            showDID.setText("");
            observableList.clear();
            File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
            BufferedReader bufferedReader;
            try {
                bufferedReader = new BufferedReader(new FileReader(userTxt));
                StringBuilder message = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    message.append(line).append("\n");
                }
                String temp = message.toString();
                while (temp.contains("课程weId=")) {
                        observableList.add(temp.substring(temp.indexOf("课程weId=") + 7,temp.indexOf("课程weId=") + 7 + 55));
                        temp = temp.substring(temp.indexOf("课程weId=") + 7 + 55);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            primaryStage.setScene(curriculumScene);
        });
        Button CptServiceButton = new Button("CPT服务");
        CptServiceButton.setPrefSize(200,30);
        CptServiceButton.setLayoutX(150);
        CptServiceButton.setLayoutY(150);
        CptServiceButton.setOnAction(event -> primaryStage.setScene(CPTScene));
        Button authorityService = new Button("授权服务");
        authorityService.setPrefSize(200,30);
        authorityService.setLayoutX(150);
        authorityService.setLayoutY(200);
        authorityService.setOnAction(event -> {
            // TODO:授权服务
        });
        Button credentialService = new Button("凭证服务");
        credentialService.setPrefSize(200,30);
        credentialService.setLayoutX(150);
        credentialService.setLayoutY(250);
        credentialService.setOnAction(event -> primaryStage.setScene(credentialScene));
        mainScenePane.getChildren().addAll(DIDServiceButton,curriculumButton,CptServiceButton,authorityService,credentialService);
        mainScenePane.getStylesheets().add("css/button.css");

        // TODO:DID菜单
        Button createDIDButton = new Button("创建DID");
        createDIDButton.setPrefSize(200,30);
        createDIDButton.setLayoutX(150);
        createDIDButton.setLayoutY(100);
        createDIDButton.setOnAction(event -> primaryStage.setScene(createDIDScene));
        Button countDIDButton = new Button("查询DID数量");
        countDIDButton.setPrefSize(200,30);
        countDIDButton.setLayoutX(150);
        countDIDButton.setLayoutY(150);
        countDIDButton.setOnAction(event -> alertShow("DID总数为：" + didService.getCount()));
        Button DIDDocumentButton = new Button("获取DID文档");
        DIDDocumentButton.setPrefSize(200,30);
        DIDDocumentButton.setLayoutX(150);
        DIDDocumentButton.setLayoutY(200);
        DIDDocumentButton.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,500,100));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("获取DID文档");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            Label label = new Label("DID:");
            label.setStyle("-fx-font-size:16;-fx-font-weight:bold");
            label.setLayoutX(20);
            label.setLayoutY(30);
            TextField textField = new TextField();
            textField.setPrefSize(400,25);
            textField.setLayoutX(60);
            textField.setLayoutY(30);
            Button button = new Button("确定");
            button.setLayoutX(240);
            button.setLayoutY(70);
            button.setOnAction(event1 -> {
                stage.close();
                String DID = textField.getText();
                if (DID != null) {
                    String didDocument = didService.getDIDDocument(DID);
                    showMessage(didDocument);
                }
            });
            anchorPane1.getChildren().addAll(label,textField,button);
            stage.show();
        });
        Button returnButton = new Button("返回主菜单");
        returnButton.setPrefSize(200,30);
        returnButton.setLayoutX(150);
        returnButton.setLayoutY(250);
        returnButton.setOnAction(event -> primaryStage.setScene(mainScene));
        DIDPanel.getChildren().addAll(createDIDButton,countDIDButton,DIDDocumentButton,returnButton);
        DIDPanel.getStylesheets().add("css/button.css");

        // TODO:创建DID菜单
        Button noKeyButton = new Button("无秘钥创建DID");
        noKeyButton.setPrefSize(200,30);
        noKeyButton.setLayoutX(150);
        noKeyButton.setLayoutY(100);
        noKeyButton.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,300,100));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("DID文档");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            anchorPane1.getStylesheets().add("css/button.css");
            Button identity = new Button("身份");
            identity.setPrefSize(100,30);
            identity.setLayoutX(25);
            identity.setLayoutY(35);
            if (role.equals("学生")) {
                identity.setOnAction(event1 -> {
                    stage.close();
                    createStudent(didService);
                });
                Button lessons = new Button("学业");
                lessons.setPrefSize(100,30);
                lessons.setLayoutX(175);
                lessons.setLayoutY(35);
                lessons.setOnAction(event1 -> {
                    String result = cutResult(didService.create("学生课程DID","深圳大学"));
                    File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
                    if (userTxt.exists()) {
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
                            StringBuilder oldMessage = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                oldMessage.append(line).append("\n");
                            }
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                            bufferedWriter.write(oldMessage + "\n" + "课程" + result + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        alertShow("创建成功！");
                    }else {
                        alertShow("文件丢失！");
                    }
                    stage.close();
                });
                anchorPane1.getChildren().addAll(identity,lessons);
            }else if (role.equals("教师")){
                identity.setOnAction(event1 -> {
                    stage.close();
                    createTeacher(didService);
                });
                Button lessons = new Button("课程");
                lessons.setPrefSize(100,30);
                lessons.setLayoutX(175);
                lessons.setLayoutY(35);
                lessons.setOnAction(event1 -> {
                    String result = cutResult(didService.create("教师课程DID","深圳大学"));
                    File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
                    if (userTxt.exists()) {
                        try {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
                            StringBuilder oldMessage = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                oldMessage.append(line).append("\n");
                            }
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                            bufferedWriter.write(oldMessage + "\n" + "课程" + result + "\n");
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        alertShow("创建成功！");
                    }else {
                        alertShow("文件丢失！");
                    }
                    stage.close();
                });
                anchorPane1.getChildren().addAll(identity,lessons);
            } else if (role.equals("教务处")){
                Button curriculum = new Button("课程");
                curriculum.setPrefSize(100,30);
                curriculum.setLayoutX(100);
                curriculum.setLayoutY(35);
                curriculum.setOnAction(event1 -> {
                stage.close();
                createCurriculum(didService);
                });
                anchorPane1.getChildren().addAll(curriculum);
            }
            stage.show();
        });

        Button KeyButton = new Button("有秘钥创建DID");
        KeyButton.setPrefSize(200,30);
        KeyButton.setLayoutX(150);
        KeyButton.setLayoutY(150);
        KeyButton.setOnAction(event -> {});
        Button returnButton1 = new Button("返回主菜单");
        returnButton1.setPrefSize(200,30);
        returnButton1.setLayoutX(150);
        returnButton1.setLayoutY(200);
        returnButton1.setOnAction(event -> primaryStage.setScene(mainScene));
        createDIDPanel.getChildren().addAll(noKeyButton,KeyButton,returnButton1);
        createDIDPanel.getStylesheets().add("css/button.css");

        // TODO:CPT
        Button registerCpt = new Button("注册CPT");
        registerCpt.setPrefSize(200,30);
        registerCpt.setLayoutX(150);
        registerCpt.setLayoutY(100);
        registerCpt.setOnAction(event -> {
            if (role.equals("教务处")) {
                Stage stage = new Stage();
                AnchorPane anchorPane1 = new AnchorPane();
                stage.setScene(new Scene(anchorPane1,300,200));
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("添加键值对");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                anchorPane1.getStylesheets().add("css/button.css");
                Button add = new Button("添加");
                add.setPrefSize(100,30);
                add.setLayoutX(25);
                add.setLayoutY(150);
                add.setOnAction(event1 -> {});
                Button send = new Button("提交");
                send.setPrefSize(100,30);
                send.setLayoutX(175);
                send.setLayoutY(150);
                send.setOnAction(event1 -> {});
                anchorPane1.getChildren().addAll(add,send);
                stage.show();
            }else {
                alertShow("不是教务处无法使用该功能！");
            }
        });
        Button queryCpt = new Button("查询CPT");
        queryCpt.setPrefSize(200,30);
        queryCpt.setLayoutX(150);
        queryCpt.setLayoutY(150);
        queryCpt.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,300,200));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("查询CPT");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            anchorPane1.getStylesheets().add("css/button.css");
            Label label = new Label("请输入CPT编号：");
            label.setLayoutX(100);
            label.setLayoutY(30);
            TextField textField = new TextField();
            textField.setPrefSize(200,30);
            textField.setLayoutX(50);
            textField.setLayoutY(80);
            Button check = new Button("查询");
            check.setPrefSize(200,30);
            check.setLayoutX(50);
            check.setLayoutY(150);
            check.setOnAction(event1 -> showMessage(cptService.queryCpt(textField.getText())));
            anchorPane1.getChildren().addAll(check,label,textField);
            stage.show();
        });
        Button returnButton3 = new Button("返回主菜单");
        returnButton3.setPrefSize(200,30);
        returnButton3.setLayoutX(150);
        returnButton3.setLayoutY(200);
        returnButton3.setOnAction(event -> primaryStage.setScene(mainScene));
        CPTPanel.getChildren().addAll(registerCpt,queryCpt,returnButton3);
        CPTPanel.getStylesheets().add("css/button.css");

        // TODO:凭证菜单
        Button create = new Button("创建凭证");
        create.setPrefSize(200,30);
        create.setLayoutX(150);
        create.setLayoutY(100);
        create.setOnAction(event -> {
            if (role.equals("教师")) {
                observableList.clear();
                File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new FileReader(userTxt));
                    StringBuilder message = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        message.append(line).append("\n");
                    }
                    String temp = message.toString();
                    while (temp.contains("课程weId=")) {
                        observableList.add(temp.substring(temp.indexOf("课程weId=") + 7,temp.indexOf("课程weId=") + 7 + 55));
                        temp = temp.substring(temp.indexOf("课程weId=") + 7 + 55);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                AnchorPane anchorPane1 = new AnchorPane();
                stage.setScene(new Scene(anchorPane1,500,300));
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("创建学生成绩凭证");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                Label label11 = new Label("发行方DID");
                label11.setLayoutX(20);
                label11.setLayoutY(25);
                Label label22 = new Label("课程DID");
                label22.setLayoutX(20);
                label22.setLayoutY(65);
                TextField textField22 = new TextField();
                textField22.setPrefSize(400,25);
                textField22.setLayoutX(80);
                textField22.setLayoutY(60);
                Label label33 = new Label("学生DID");
                label33.setLayoutX(20);
                label33.setLayoutY(105);
                TextField textField33 = new TextField();
                textField33.setPrefSize(400,25);
                textField33.setLayoutX(80);
                textField33.setLayoutY(100);
                Label label44 = new Label("分数类型");
                label44.setLayoutX(20);
                label44.setLayoutY(145);
                TextField textField44 = new TextField();
                textField44.setPrefSize(400,25);
                textField44.setLayoutX(80);
                textField44.setLayoutY(140);
                Label label55 = new Label("分数");
                label55.setLayoutX(20);
                label55.setLayoutY(185);
                TextField textField55 = new TextField();
                textField55.setPrefSize(400,25);
                textField55.setLayoutX(80);
                textField55.setLayoutY(180);
                ChoiceBox<String> DID = new ChoiceBox<>(observableList);
                DID.setLayoutX(80);
                DID.setLayoutY(20);
                anchorPane1.getStylesheets().add("css/button.css");
                Button yes1 = new Button("确定");
                yes1.setPrefSize(100,30);
                yes1.setLayoutX(125);
                yes1.setLayoutY(250);
                yes1.setOnAction(event1 -> {
                    String cptID = "258";
                    String issuer = DID.getValue();
                    long expirationDate = 1000L * 60 * 60 * 24 * 365 * 100;
                    WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
                    weIdAuthentication.setWeId(issuer);
                    String priKey = "";
                    BufferedReader bufferedReader1;
                    try {
                        bufferedReader1 = new BufferedReader(new FileReader(userTxt));
                        StringBuilder message = new StringBuilder();
                        String line;
                        while ((line = bufferedReader1.readLine()) != null) {
                            message.append(line).append("\n");
                        }
                        String temp = message.toString();
                        // 获取所有课程DID对应的私钥
                        temp = temp.substring(temp.indexOf(issuer));
                        priKey = temp.substring(temp.indexOf("privateKey=") + 11,temp.indexOf("privateKey=") + 11 + 77);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
                    weIdPrivateKey.setPrivateKey(priKey);
                    weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
                    weIdAuthentication.setAuthenticationMethodId(issuer);
                    Map<String, Object> claim = new HashMap<>();
                    claim.put("课程DID", textField22.getText());
                    claim.put("学生DID", textField33.getText());
                    claim.put("分数类型", textField44.getText());
                    claim.put("分数", textField55.getText());
                    int i = 1;
                    // 获取首个空的
                    while (!persistence.get("domain.defaultInfo","studentScore" + i).getResult().equals("")) {
                        i++;
                    }
                    if (DID.getValue() == null || DID.getValue().equals("")) {
                        alertShow("未选择选课DID！");
                    }else if (textField22.getText().equals("")) {
                        alertShow("未输入课程DID！");
                    }else if (textField33.getText().equals("")) {
                        alertShow("未输入学生DID！");
                    }else if (textField44.getText().equals("")) {
                        alertShow("未输入分数类型！");
                    }else if (textField55.getText().equals("")) {
                        alertShow("未输入分数！");
                    }else {
                        String result = credentialPojo.createCredential(cptID,issuer,expirationDate,weIdAuthentication,claim);
                        showMessage(result);
                        persistence.addOrUpdate("domain.defaultInfo","studentScore" + i, result);
                        stage.close();
                    }
                });
                Button no = new Button("取消");
                no.setPrefSize(100,30);
                no.setLayoutX(275);
                no.setLayoutY(250);
                no.setOnAction(event1 -> stage.close());
                anchorPane1.getChildren().addAll(yes1,no,DID,label11,label22,label33,label44,label55,textField22,textField33,textField44,textField55);
                stage.show();
            }else if (role.equals("学生")) {
                // TODO:教务处生成凭证
                Stage stage = new Stage();
                AnchorPane anchorPane1 = new AnchorPane();
                stage.setScene(new Scene(anchorPane1,500,200));
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("申请学分修满凭证");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                anchorPane1.getStylesheets().add("css/button.css");
                ObservableList<String> observableList1 = FXCollections.observableArrayList();
                ObservableList<String> observableList2 = FXCollections.observableArrayList();
                File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
                BufferedReader bufferedReader;
                try {
                    observableList1.clear();
                    observableList2.clear();
                    bufferedReader = new BufferedReader(new FileReader(userTxt));
                    StringBuilder message = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        message.append(line).append("\n");
                    }
                    String temp = message.toString();
                    while (temp.contains("课程weId=")) {
                        observableList1.add(temp.substring(temp.indexOf("课程weId=") + 7,temp.indexOf("课程weId=") + 7 + 55));
                        temp = temp.substring(temp.indexOf("课程weId=") + 7 + 55);
                    }
                    temp = message.toString();
                    while (temp.contains("身份weId=")) {
                        observableList2.add(temp.substring(temp.indexOf("身份weId=") + 7,temp.indexOf("身份weId=") + 7 + 55));
                        temp = temp.substring(temp.indexOf("身份weId=") + 7 + 55);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Label label11 = new Label("课程");
                label11.setLayoutX(20);
                label11.setLayoutY(25);
                ChoiceBox<String> DID1 = new ChoiceBox<>(observableList1);
                DID1.setLayoutX(80);
                DID1.setLayoutY(20);
                Label label22 = new Label("身份");
                label22.setLayoutX(20);
                label22.setLayoutY(65);
                ChoiceBox<String> DID2 = new ChoiceBox<>(observableList2);
                DID2.setLayoutX(80);
                DID2.setLayoutY(60);
                Button yes1 = new Button("确定");
                yes1.setPrefSize(100,30);
                yes1.setLayoutX(125);
                yes1.setLayoutY(150);
                yes1.setOnAction(event2 -> {
                    if (DID1.getValue() == null || DID1.getValue().equals("")) {
                        alertShow("未选择课程DID！");
                    }else if (DID2.getValue() == null || DID2.getValue().equals("")) {
                        alertShow("未选择身份DID！");
                    }else {
                        int i = 1;
                        // 获取首个空的
                        while (!persistence.get("domain.defaultInfo","studentCredit" + i).getResult().equals("")) {
                            i++;
                        }
                        stage.close();
                        persistence.addOrUpdate("domain.defaultInfo","studentCredit" + i, DID1.getValue() + "&" + DID2.getValue());
                    }
                });
                Button no = new Button("取消");
                no.setPrefSize(100,30);
                no.setLayoutX(275);
                no.setLayoutY(150);
                no.setOnAction(event2 -> stage.close());
                anchorPane1.getChildren().addAll(yes1,no,DID1,DID2,label11,label22);
                stage.show();
            }else {
            }
        });
        Button verify = new Button("验证凭证");
        verify.setPrefSize(200,30);
        verify.setLayoutX(150);
        verify.setLayoutY(150);
        verify.setOnAction(event -> {
            Stage stage = new Stage();
            AnchorPane anchorPane1 = new AnchorPane();
            stage.setScene(new Scene(anchorPane1,300,200));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle("验证凭证");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            anchorPane1.getStylesheets().add("css/button.css");
            TextField fileName = new TextField();
            fileName.setPrefSize(100,30);
            fileName.setLayoutY(60);
            fileName.setLayoutX(150);
            final String[] fileMessage = new String[1];
            Button load = new Button("选择文件");
            load.setPrefSize(100,30);
            load.setLayoutX(30);
            load.setLayoutY(60);
            load.setOnAction(event1 -> {
                try {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showOpenDialog(primaryStage);
                    fileName.setText(file.getName());
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    StringBuilder message = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        message.append(line);
                    }
                    fileMessage[0] = message.toString();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Button yes1 = new Button("确定");
            yes1.setPrefSize(100,30);
            yes1.setLayoutX(25);
            yes1.setLayoutY(150);
            yes1.setOnAction(event1 -> {
                String issuer = fileMessage[0].substring(fileMessage[0].indexOf("issuer") + 9,fileMessage[0].indexOf("issuer") + 64);
                System.out.println(issuer);
                boolean result = credentialPojo.verifyCredential(issuer,fileMessage[0]);
                if (result) {
                    alertShow("验证通过！");
                }else {
                    alertShow("验证失败！");
                }
            });
            Button no = new Button("取消");
            no.setPrefSize(100,30);
            no.setLayoutX(175);
            no.setLayoutY(150);
            no.setOnAction(event1 -> stage.close());
            anchorPane1.getChildren().addAll(yes1,no,load,fileName);
            stage.show();
        });
        Button look = new Button("查看凭证");
        look.setPrefSize(200,30);
        look.setLayoutX(150);
        look.setLayoutY(200);
        look.setOnAction(event1 -> {
            Stage stage1 = new Stage();
            AnchorPane anchorPane2 = new AnchorPane();
            stage1.setScene(new Scene(anchorPane2,500,200));
            stage1.initStyle(StageStyle.UTILITY);
            stage1.setTitle("申请学分修满凭证");
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.setResizable(false);
            anchorPane2.getStylesheets().add("css/button.css");
            String userTxt = getTxt();
            String temp = userTxt;
            ObservableList<String> observableList3 = FXCollections.observableArrayList();
            while (temp.contains("身份weId=")) {
                observableList3.add(temp.substring(temp.indexOf("身份weId=") + 7,temp.indexOf("身份weId=") + 7 + 55));
                temp = temp.substring(temp.indexOf("身份weId=") + 7 + 55);
            }
            Label label11 = new Label("DID：");
            label11.setLayoutX(20);
            label11.setLayoutY(60);
            ChoiceBox<String> DID = new ChoiceBox<>(observableList3);
            DID.setLayoutX(80);
            DID.setLayoutY(60);
            Button yes11 = new Button("查询");
            yes11.setPrefSize(100,30);
            yes11.setLayoutX(200);
            yes11.setLayoutY(150);
            yes11.setOnAction(event -> showMessage(persistence.get("domain.defaultInfo",DID.getValue()).getResult()));
            anchorPane2.getChildren().addAll(DID,label11,yes11);
            stage1.show();
        });
        Button returnButton4 = new Button("返回主菜单");
        returnButton4.setPrefSize(200,30);
        returnButton4.setLayoutX(150);
        returnButton4.setLayoutY(250);
        returnButton4.setOnAction(event -> primaryStage.setScene(mainScene));
        credentialPanel.getChildren().addAll(create,verify,look,returnButton4);
        credentialPanel.getStylesheets().add("css/button.css");

        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(false);
    }

    // TODO:创建学生DID
    void createStudent(DIDService didService) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        stage.setScene(new Scene(anchorPane,400,250));
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("输入学生信息");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Label label1 = new Label("姓名");
        label1.setLayoutX(100);
        label1.setLayoutY(50);
        TextField nameField = new TextField();
        nameField.setLayoutX(150);
        nameField.setLayoutY(50);
        Label label2 = new Label("学号");
        label2.setLayoutX(100);
        label2.setLayoutY(100);
        TextField numberField = new TextField();
        numberField.setLayoutX(150);
        numberField.setLayoutY(100);
        Label label3 = new Label("专业");
        label3.setLayoutX(100);
        label3.setLayoutY(150);
        ChoiceBox<String> subject = new ChoiceBox<>(FXCollections.observableArrayList("通信工程","集成电路","计算机科学与技术        ","软件工程","信息与通信工程","金融科技"));
        subject.setLayoutX(150);
        subject.setLayoutY(150);
//        Label label4 = new Label("性别");
//        label4.setLayoutX(100);
//        label4.setLayoutY(200);
//        ToggleGroup group = new ToggleGroup();
//        RadioButton man = new RadioButton("男");
//        man.setLayoutX(180);
//        man.setLayoutY(200);
//        man.setToggleGroup(group);
//        RadioButton girl = new RadioButton("女");
//        girl.setLayoutX(230);
//        girl.setLayoutY(200);
//        girl.setToggleGroup(group);
//        Label label5 = new Label("生日");
//        label5.setLayoutX(100);
//        label5.setLayoutY(250);
//        DatePicker datePicker = new DatePicker();
//        datePicker.setLayoutX(150);
//        datePicker.setLayoutY(250);
        Button yes = new Button("确定");
        yes.setPrefSize(100,30);
        yes.setLayoutX(150);
        yes.setLayoutY(200);
        yes.setOnAction(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String job = subject.getValue();
//            String gender = man.isSelected() ?  "男" : "女";
//            String date = datePicker.getValue().toString();
//            String data = "|姓名:" + name + " |学号:" + number + " |专业:" + job + " |性别:" + gender + " |出生日期:" + date + "*";
            String data = "|姓名:" + name + " |学号:" + number + " |专业:" + job + "*";
            String result = cutResult(didService.create("学生DID",data));
            File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
            if (userTxt.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
                    StringBuilder oldMessage = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        oldMessage.append(line).append("\n");
                    }
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                    bufferedWriter.write(oldMessage + "\n" + "身份" + result + "\n");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertShow("创建成功！");
            }else {
                alertShow("文件丢失！");
            }
            stage.close();
        });
        anchorPane.getChildren().addAll(yes,label1,label2,label3,nameField,numberField,subject);
        stage.show();
    }

    // TODO:创建教师DID
    void createTeacher(DIDService didService) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        stage.setScene(new Scene(anchorPane,400,250));
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("输入教师信息");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Label label1 = new Label("姓名");
        label1.setLayoutX(100);
        label1.setLayoutY(50);
        TextField nameField = new TextField();
        nameField.setLayoutX(150);
        nameField.setLayoutY(50);
        Label label2 = new Label("工号");
        label2.setLayoutX(100);
        label2.setLayoutY(100);
        TextField numberField = new TextField();
        numberField.setLayoutX(150);
        numberField.setLayoutY(100);
        Label label3 = new Label("学院");
        label3.setLayoutX(100);
        label3.setLayoutY(150);
        ChoiceBox<String> subject = new ChoiceBox<>(FXCollections.observableArrayList("金融学院","马克义主义学院","电子与信息工程学院      ","文学院","艺术学院","计算机与软件学院"));
        subject.setLayoutX(150);
        subject.setLayoutY(150);
//        Label label4 = new Label("性别");
//        label4.setLayoutX(100);
//        label4.setLayoutY(200);
//        ToggleGroup group = new ToggleGroup();
//        RadioButton man = new RadioButton("男");
//        man.setLayoutX(180);
//        man.setLayoutY(200);
//        man.setToggleGroup(group);
//        RadioButton girl = new RadioButton("女");
//        girl.setLayoutX(230);
//        girl.setLayoutY(200);
//        girl.setToggleGroup(group);
//        Label label5 = new Label("生日");
//        label5.setLayoutX(100);
//        label5.setLayoutY(250);
//        DatePicker datePicker = new DatePicker();
//        datePicker.setLayoutX(150);
//        datePicker.setLayoutY(250);
        Button yes = new Button("确定");
        yes.setPrefSize(100,30);
        yes.setLayoutX(150);
        yes.setLayoutY(200);
        yes.setOnAction(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String job = subject.getValue();
//            String gender = man.isSelected() ?  "男" : "女";
//            String date = datePicker.getValue().toString();
//            String data = "|名字:" + name + " |学号:" + number + " |学院:" + job + " |性别:" + gender + " |出生日期:" + date + "*";
            String data = "|名字:" + name + " |学号:" + number + " |学院:" + job + "*";
            String result = cutResult(didService.create("教师DID",data));
            File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
            if (userTxt.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
                    StringBuilder oldMessage = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        oldMessage.append(line).append("\n");
                    }
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                    bufferedWriter.write(oldMessage + "\n" + "身份" +result + "\n");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertShow("创建成功！");
            }else {
                alertShow("文件丢失！");
            }
            stage.close();
        });
        anchorPane.getChildren().addAll(yes,label1,label2,label3,nameField,numberField,subject);
        stage.show();
    }

    // TODO:创建课程DID
    void createCurriculum(DIDService didService) {
        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();
        stage.setScene(new Scene(anchorPane,400,400));
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("输入课程信息");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        Label label1 = new Label("名称");
        label1.setLayoutX(100);
        label1.setLayoutY(50);
        TextField nameField = new TextField();
        nameField.setLayoutX(150);
        nameField.setLayoutY(50);
        Label label2 = new Label("课号");
        label2.setLayoutX(100);
        label2.setLayoutY(100);
        TextField numberField = new TextField();
        numberField.setLayoutX(150);
        numberField.setLayoutY(100);
        Label label3 = new Label("学分");
        label3.setLayoutX(100);
        label3.setLayoutY(150);
        TextField creditField = new TextField();
        creditField.setLayoutX(150);
        creditField.setLayoutY(150);
        Label label4 = new Label("性质");
        label4.setLayoutX(100);
        label4.setLayoutY(200);
        ToggleGroup group = new ToggleGroup();
        RadioButton major = new RadioButton("必修");
        major.setLayoutX(180);
        major.setLayoutY(200);
        major.setToggleGroup(group);
        RadioButton minor = new RadioButton("选修");
        minor.setLayoutX(230);
        minor.setLayoutY(200);
        minor.setToggleGroup(group);
        Label label5 = new Label("时间");
        label5.setLayoutX(100);
        label5.setLayoutY(250);
        DatePicker datePicker = new DatePicker();
        datePicker.setLayoutX(150);
        datePicker.setLayoutY(250);
        Button yes = new Button("确定");
        yes.setPrefSize(100,30);
        yes.setLayoutX(150);
        yes.setLayoutY(350);
        yes.setOnAction(event -> {
            String name = nameField.getText();
            String number = numberField.getText();
            String credit = creditField.getText();
            String gender = major.isSelected() ?  "必修" : "选修";
            String date = datePicker.getValue().toString();
            String data = "名称:" + name  + "*" + " 课号:" + number + " 学分:" + credit + "*" +" 性质 :" + gender + " 时间:" + date + "|";
            String result = cutResult(didService.create("课程DID",data));
            File userTxt = new File("src\\main\\resources\\account\\deanOffice.txt");
            if (userTxt.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
                    StringBuilder oldMessage = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        oldMessage.append(line).append("\n");
                    }
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(userTxt));
                    bufferedWriter.write(oldMessage + "\n" + "课程" +result + "\n");
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertShow("创建成功！");
            }else {
                alertShow("文件丢失！");
            }
            stage.close();
        });
        anchorPane.getChildren().addAll(yes,label1,label2,label3,label4,label5,nameField,numberField,creditField,major,minor,datePicker);
        stage.show();
    }

    // TODO:大框显示信息
    void showMessage(String message){
        Stage stage = new Stage();
        AnchorPane anchorPane1 = new AnchorPane();
        stage.setScene(new Scene(anchorPane1,600,700));
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("DID文档");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(600,650);
        TextArea textArea = new TextArea();
        textArea.setPrefSize(595,645);
        textArea.setEditable(false);
        textArea.setText(message);
        scrollPane.setContent(textArea);
        Button createFile = new Button("生成文件");
        createFile.setPrefSize(100,30);
        createFile.setLayoutX(250);
        createFile.setLayoutY(660);
        createFile.setOnAction(event -> {
            Stage stage1 = new Stage();
            AnchorPane anchorPane2 = new AnchorPane();
            stage1.setScene(new Scene(anchorPane2,300,200));
            stage1.initStyle(StageStyle.UTILITY);
            stage1.setTitle("输入文件名");
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.setResizable(false);
            Label label = new Label("请输入文件名");
            label.setLayoutX(100);
            label.setLayoutY(30);
            TextField textField = new TextField();
            textField.setPrefSize(200,30);
            textField.setLayoutX(50);
            textField.setLayoutY(80);
            Button button = new Button("确定");
            button.setPrefSize(100,30);
            button.setLayoutX(100);
            button.setLayoutY(150);
            button.setOnAction(event1 -> {
                String fileName = textField.getText();
                String path = "src\\..\\..\\" + fileName + ".txt";
                File file = new File(path);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                        bufferedWriter.write(message);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        alertShow("文件创建成功！");
                    }else {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                        bufferedWriter.write(message);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        alertShow("文件名已存在，已重新写入内容！");
                    }
                } catch (IOException e) {
                    alertShow("文件生成错误！");
                    e.printStackTrace();
                }
            });
            anchorPane2.getChildren().addAll(button,textField,label);
            stage1.show();
        });
        anchorPane1.getChildren().addAll(scrollPane,createFile);
        stage.show();
    }

    // TODO:小框显示信息
    void alertShow(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("提示信息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }

    // TODO:字符串修整
    String cutResult(String result) {
        result = result.replace(" ","\n");
        result = result.replace(",","");
        result = result.replace("(","");
        result = result.replace(")","");
        result = result.replace("CreateWeIdDataResult","");
        result = result.replace("userWeIdPublicKey=WeIdPublicKey","");
        result = result.replace("userWeIdPrivateKey=WeIdPrivateKey","");
        return result;
    }

    // TODO:获取txt文件
    String getTxt(){
        File userTxt = new File("src\\main\\resources\\account\\" + user + ".txt");
        StringBuilder message = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt));
            message = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                message.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.toString();
    }
}
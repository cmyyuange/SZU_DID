package org.example;

import com.webank.weid.rpc.*;
import com.webank.weid.service.impl.*;
import com.webank.weid.suite.api.persistence.PersistenceFactory;
import com.webank.weid.suite.api.persistence.inf.Persistence;
import com.webank.weid.suite.api.persistence.params.PersistenceType;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    EvidenceService evidenceService;
    AuthorityIssuerService authorityIssuerService;
    CptService cptService;
    CredentialPojoService credentialPojoService;
    WeIdService weIdService;
    Persistence persistence;

    public void init() throws Exception{
        super.init();
        ProgressShow progressShow = new ProgressShow();
        progressShow.show();
        persistence = PersistenceFactory.build(PersistenceType.Mysql);
        evidenceService = new EvidenceServiceImpl();
        progressShow.setValue(80);
        Thread.sleep(100);
        authorityIssuerService = new AuthorityIssuerServiceImpl();
        progressShow.setValue(85);
        Thread.sleep(100);
        cptService = new CptServiceImpl();
        progressShow.setValue(90);
        Thread.sleep(100);
        credentialPojoService = new CredentialPojoServiceImpl();
        progressShow.setValue(95);
        Thread.sleep(100);
        weIdService = new WeIdServiceImpl();
        progressShow.setValue(100);
        Thread.sleep(100);

        // 进度条关闭并打开主窗口
        progressShow.close();
    }

    @Override
    public void start(Stage primaryStage) {
        new Windows(persistence,evidenceService,authorityIssuerService,cptService,credentialPojoService,weIdService,primaryStage);
    }

    public void stop() throws Exception{
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
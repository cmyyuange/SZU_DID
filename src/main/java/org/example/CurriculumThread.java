package org.example;

import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.suite.api.persistence.inf.Persistence;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CurriculumThread extends Thread{

    Persistence persistence;
    DIDService didService;
    credentialPojo credentialPojo;
    String adminKey;

    public CurriculumThread(Persistence persistence,DIDService didService,credentialPojo credentialPojo,String adminKey) {
        this.persistence = persistence;
        this.didService = didService;
        this.credentialPojo = credentialPojo;
        this.adminKey = adminKey;
    }

    @Override
    public void run() {
        int i;
//        for (int j = 0; j < 100; j++) {
//            System.out.println(persistence.get("domain.defaultInfo",String.valueOf(j)).getResult());
//        }
        while (true) {
            try {
                i = 1;
                Thread.sleep(100);
                while (!persistence.get("domain.defaultInfo",String.valueOf(i)).getResult().equals("")) {
                    System.out.println("现在是：" + i);
                    String[] strings = persistence.get("domain.defaultInfo",String.valueOf(i)).getResult().split("&");
                    String userDID = strings[0];
                    String curriculumDID = strings[1];
                    String priKey = "";
                    File userTxt1 = new File("src\\main\\resources\\account\\deanOffice.txt");

                    BufferedReader bufferedReader = new BufferedReader(new FileReader(userTxt1));
                    StringBuilder message = new StringBuilder();
                    String line1;
                    while ((line1 = bufferedReader.readLine()) != null) {
                        message.append(line1).append("\n");
                    }
                    String temp = message.toString();
                    temp = temp.substring(temp.indexOf("课程weId=" + curriculumDID));
                    priKey = temp.substring(temp.indexOf("privateKey=") + 11,temp.indexOf("privateKey=") + 11 + 78).trim();
                    System.out.println(priKey);

                    if (didService.getDIDDocument(userDID).contains("学生")) {
                        System.out.println(didService.addMessage(curriculumDID,priKey,"深圳大学上课学生","学生:" + userDID));;
                    }else {
                        System.out.println(didService.addMessage(curriculumDID,priKey,"深圳大学任课教师","教师:" + userDID));;
                    }
                    persistence.delete("domain.defaultInfo",String.valueOf(i));
                    i++;
                }

                i = 1;
                while (!persistence.get("domain.defaultInfo","studentScore" + i).getResult().equals("")) {
                    System.out.println("studentScore" + i);
                    String score = persistence.get("domain.defaultInfo","studentScore" + i).getResult();
                    CredentialPojo credentialPojo = CredentialPojo.fromJson(score);

                    String txt = getTxt();
                    String temp = txt.substring(txt.indexOf("课程weId=" + credentialPojo.getClaim().get("课程DID").toString()));
                    String priKey = temp.substring(temp.indexOf("privateKey=") + 11,temp.indexOf("privateKey=") + 11 + 78).trim();
                    if (!didService.getDIDDocument(credentialPojo.getClaim().get("课程DID").toString()).contains(credentialPojo.getIssuer())){
                        System.out.println("课程与任课教师不对应！");
                    }else if (!didService.getDIDDocument(credentialPojo.getClaim().get("学生DID").toString()).contains(credentialPojo.getClaim().get("课程DID").toString())){
                        System.out.println("该学生并未选择这门课！");
                    }else {
                        String message = "学生DID:" + credentialPojo.getClaim().get("学生DID").toString() +
                                " 分数类型:" + credentialPojo.getClaim().get("分数类型").toString() +
                                " 分数:" + credentialPojo.getClaim().get("分数").toString() + "*";
                        didService.addMessage(credentialPojo.getClaim().get("课程DID").toString(),priKey,"深圳大学学生课程成绩",message);
                        persistence.delete("domain.defaultInfo","studentScore" + i);
                    }
                    i++;
                }

                i = 1;
                while (!persistence.get("domain.defaultInfo","studentCredit" + i).getResult().equals("")) {
                    System.out.println("studentCredit" + i);
                    String[] strings = persistence.get("domain.defaultInfo","studentCredit" + i).getResult().split("&");
                    String selectCurriculumDID = strings[0];
                    String userDID = strings[1];
                    String selectCurriculumDIDDoc = didService.getDIDDocument(selectCurriculumDID);
                    String userDIDDoc = didService.getDIDDocument(userDID);
                    String temp = selectCurriculumDIDDoc;
                    long sum = 0;
                    while (temp.contains("&DID:")) {
                        String did = temp.substring(temp.indexOf("&DID:") + 5,temp.indexOf("&DID:") + 5 + 55);
                        String curriculumDIDDoc = didService.getDIDDocument(did);
                        curriculumDIDDoc = curriculumDIDDoc.substring(curriculumDIDDoc.indexOf(selectCurriculumDID));
                        int start1 = curriculumDIDDoc.indexOf("分数:");
                        int end1 = curriculumDIDDoc.indexOf("*");
                        long score = Long.parseLong(curriculumDIDDoc.substring(start1 + 3,end1));
                        if (score >= 60) {
                            int start2 = didService.getDIDDocument(did).indexOf("学分:");
                            int end2 = didService.getDIDDocument(did).indexOf("* 性质");
                            System.out.println(start2);
                            System.out.println(end2);
                            long credit = Long.parseLong(didService.getDIDDocument(did).substring(start2 + 3,end2));
                            sum = sum + credit;
                        }
                        temp = temp.substring(temp.indexOf("&DID:") + 5 + 55);
                    }
                    if (sum >= 1){
                        String issuer = "did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1";
                        long expirationDate = 1000L * 60 * 60 * 24 * 365 * 100;
                        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
                        weIdAuthentication.setWeId(issuer);
                        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
                        weIdPrivateKey.setPrivateKey(adminKey);
                        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
                        weIdAuthentication.setAuthenticationMethodId(issuer + "#keys-75ad8301");
                        String information = userDIDDoc.substring(userDIDDoc.indexOf("|姓名"),userDIDDoc.indexOf("*"));
                        Map<String, Object> claim = new HashMap<>();
                        claim.put("信息", information);
                        claim.put("总学分", String.valueOf(sum));
                        claim.put("是否达到毕业要求","已达到毕业要求");
                        String vc = credentialPojo.createCredential("369",issuer,expirationDate,weIdAuthentication,claim);
                        persistence.addOrUpdate("domain.defaultInfo",userDID, vc);
                        persistence.delete("domain.defaultInfo","studentCredit" + i);
                        i++;
                    }else {
                        System.out.println("学分未达到毕业要求！");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String getTxt(){
        File userTxt = new File("src\\main\\resources\\account\\deanOffice.txt");
        BufferedReader bufferedReader;
        StringBuilder message = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(userTxt));
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
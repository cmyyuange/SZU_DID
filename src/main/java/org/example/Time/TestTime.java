package org.example.Time;

import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.base.WeIdPublicKey;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.request.ServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialPojoService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.util.CredentialPojoUtils;
import com.webank.weid.util.DataToolUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTime {

    public static void main(String[] args) throws IOException {
        System.out.println("时间测试启动！");

        System.out.println("初始化！");
        long start = System.currentTimeMillis();
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        long end = System.currentTimeMillis();
        System.out.println("初始化完成！用时：" + (end - start) + "ms");

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        int num = 1;
        for (int i = 0; i < num; i++) {
//            executorService.execute(new checkDidThread());
//            executorService.execute(new productDidThread());
//            executorService.execute(new addDidMessageThread());
//            executorService.execute(new CvThread());
//            executorService.execute(new CreditCvThread());
            executorService.execute(new proofCvThread());
        }
        executorService.shutdown();

        System.out.println("时间测试结束！");
    }
}

class checkDidThread implements Runnable {

    @Override
    public void run() {
        // TODO: 获取DID文档时间测试
        WeIdService weIdService = new WeIdServiceImpl();
        String DID = "did:weid:666:0xb4bc0879d2477dfd196d9fbcf1409a81dbd8088a";
        long startTime = System.currentTimeMillis();
        ResponseData<String> response1 = weIdService.getWeIdDocumentJson(DID);
        long endTime = System.currentTimeMillis();
        try {
            if (response1.getErrorCode() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("获取DID文档所需时间：" + (endTime - startTime) + "ms");
                writer.write("获取DID文档所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            } else {
                System.out.println(response1.getErrorMessage());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class productDidThread implements Runnable {

    @Override
    public void run() {
        // TODO: DID生成时间测试
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        long startTime = System.currentTimeMillis();
        ResponseData<CreateWeIdDataResult> response2 = weIdService.createWeId();
        long endTime = System.currentTimeMillis();
        try {
            if (response2.getErrorCode() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("生成DID所需时间：" + (endTime - startTime) + "ms");
                writer.write("生成DID所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class addDidMessageThread implements Runnable {

    @Override
    public void run() {
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        // TODO: 添加DID信息时间测试
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setType("999");
        serviceArgs.setServiceEndpoint("666");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey("31474180836355249488996657969957971383063476987516430525110106923919383817101");
        long startTime = System.currentTimeMillis();
        ResponseData<Boolean> response3 = weIdService.setService("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae", serviceArgs, weIdPrivateKey);
        long endTime = System.currentTimeMillis();
        try {
            if (response3.getErrorCode() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("添加DID信息所需时间：" + (endTime - startTime) + "ms");
                writer.write("添加DID信息所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class CvThread implements Runnable {

    @Override
    public void run() {
        // TODO: 成绩凭证生成时间测试
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs = new CreateCredentialPojoArgs<>();
        createCredentialPojoArgs.setCptId(258);
        createCredentialPojoArgs.setIssuer("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 100));
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey("31474180836355249488996657969957971383063476987516430525110106923919383817101");
        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
        weIdAuthentication.setAuthenticationMethodId("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        createCredentialPojoArgs.setWeIdAuthentication(weIdAuthentication);
        Map<String, Object> claim = new HashMap<>();
        claim.put("课程DID", "1");
        claim.put("学生DID", "1");
        claim.put("分数类型", "1");
        claim.put("分数", "1");
        createCredentialPojoArgs.setClaim(claim);
        long startTime = System.currentTimeMillis();
        ResponseData<CredentialPojo> response = credentialPojoService.createCredential(createCredentialPojoArgs);
        long endTime = System.currentTimeMillis();
        try {
            if (response.getErrorCode() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("成绩凭证生成所需时间：" + (endTime - startTime) + "ms");
                writer.write("成绩凭证生成所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class CreditCvThread implements Runnable {

    @Override
    public void run() {
        // TODO: 学分凭证生成时间测试
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        WeIdPrivateKey weIdPrivateKey1 = new WeIdPrivateKey();
        weIdPrivateKey1.setPrivateKey("31474180836355249488996657969957971383063476987516430525110106923919383817101");
        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey1);
        weIdAuthentication.setAuthenticationMethodId("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs1 = new CreateCredentialPojoArgs<>();
        createCredentialPojoArgs1.setCptId(369);
        createCredentialPojoArgs1.setIssuer("did:weid:666:0xca4a43b9edb74f57ddbd9fa11ad59e78c811a8ae");
        createCredentialPojoArgs1.setExpirationDate(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 100));
        createCredentialPojoArgs1.setWeIdAuthentication(weIdAuthentication);
        Map<String, Object> claim1 = new HashMap<>();
        claim1.put("信息", "1");
        claim1.put("总学分", "1");
        claim1.put("是否达到毕业要求","已达到毕业要求");
        createCredentialPojoArgs1.setClaim(claim1);
        long startTime = System.currentTimeMillis();
        // String stu = "did:weid:666:0x0f2a95c230bf54cbcd46fa6f19477a32a23b4ed8";
        String stu = "did:weid:666:0x0f2a95c230bf54cbcd46fa6f19477a32a23b4ed8";
        String temp = weIdService.getWeIdDocumentJson(stu).getResult();
        long sum = 0;
        while (temp.contains("&DID:")) {
            String did = temp.substring(temp.indexOf("&DID:") + 5,temp.indexOf("&DID:") + 5 + 55);
            String curriculumDIDDoc = weIdService.getWeIdDocumentJson(did).getResult();
            String curriculumDIDDoc1 = curriculumDIDDoc;
            curriculumDIDDoc = curriculumDIDDoc.substring(curriculumDIDDoc.indexOf("did:weid:666:0x0f2a95c230bf54cbcd46fa6f19477a32a23b4ed8"));
            int start1 = curriculumDIDDoc.indexOf("分数:");
            int end1 = curriculumDIDDoc.indexOf("*");
            long score = Long.parseLong(curriculumDIDDoc.substring(start1 + 3,end1));
            if (score >= 60) {
                int start2 = curriculumDIDDoc1.indexOf("学分:");
                int end2 = curriculumDIDDoc1.indexOf("* 性质");
                long credit = Long.parseLong(curriculumDIDDoc1.substring(start2 + 3,end2));
                sum = sum + credit;
            }
            temp = temp.substring(temp.indexOf("&DID:") + 5 + 55);
        }
        ResponseData<CredentialPojo> response5 = credentialPojoService.createCredential(createCredentialPojoArgs1);
        long endTime = System.currentTimeMillis();
        try {
            if (response5.getErrorCode() == 0) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("学分凭证生成所需时间：" + (endTime - startTime) + "ms");
                writer.write("学分凭证生成所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            }
        }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class proofCvThread implements Runnable {

    @Override
    public void run() {
        // TODO: 验证凭证时间测试
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        String cv = "{\"claim\":{\n" +
                "  \"信息\":\"|姓名:李四 |学号:87654321 |专业:信息与通信工程\",\n" +
                "  \"总学分\":\"120\",\n" +
                "  \"是否达到毕业要求\":\"已达到毕业要求\"\n" +
                "},\n" +
                "  \"context\":\"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1\",\n" +
                "  \"cptId\":369,\n" +
                "  \"expirationDate\":\"2123-03-16T20:23:40Z\",\n" +
                "  \"id\":\"720aaf57-44d3-451f-825b-530402fa21eb\",\n" +
                "  \"issuanceDate\":\"2023-04-09T20:23:40Z\",\n" +
                "  \"issuer\":\"did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1\",\n" +
                "  \"proof\":{\n" +
                "    \"created\":\"2023-04-09T20:23:40Z\",\n" +
                "    \"creator\":\"did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1\",\n" +
                "    \"salt\":{\n" +
                "      \"信息\":\"eJlx4\",\n" +
                "      \"总学分\":\"oJMO3\",\n" +
                "      \"是否达到毕业要求\":\"CB8b9\"\n" +
                "    },\n" +
                "    \"signatureValue\":\"E3c1c0YO9Wr/a212I1o7OJSW6gWU+g9/8sPitOuI9jp/OEXoDIeU9JuDLPKmIOobB3gHbdcu0kqiMXHQZ6ISlwA=\",\n" +
                "    \"type\":\"Secp256k1\"\n" +
                "  },\n" +
                "  \"type\":[\"VerifiableCredential\",\"original\"],\n" +
                "  \"$from\":\"toJson\"\n" +
                "}\n" +
                "\n";
        CredentialPojo credentialPojo = CredentialPojo.fromJson(cv);
        WeIdPublicKey weIdPublicKey = new WeIdPublicKey();
        String publicKey = "6122365348729397160003726788659502778392083440724683723035186229223943762892243053109533903020970955041765788187816756208363375919836534322025298953189634";
        weIdPublicKey.setPublicKey(publicKey);
        boolean result = false;
        long startTime = System.currentTimeMillis();
//        ResponseData<Boolean> response = credentialPojoService.verify(weIdPublicKey,credentialPojo);
        Map<String, Object> salt = credentialPojo.getSalt();
        String rawData = CredentialPojoUtils.getCredentialThumbprintWithoutSig(credentialPojo, salt, null);
        result = DataToolUtils.verifySignature(rawData, credentialPojo.getSignature(), new BigInteger(publicKey));
        long endTime = System.currentTimeMillis();
        try {
            if (result) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt",true));
                System.out.println("验证凭证所需时间：" + (endTime - startTime) + "ms");
                writer.write("验证凭证所需时间：" + (endTime - startTime) + "ms");
                writer.newLine();
                writer.close();
            }
        }catch (IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

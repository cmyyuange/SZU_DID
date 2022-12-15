package org.example;

import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.ServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.WeIdService;

public class DIDService {
    WeIdService weIdService;
    public DIDService(WeIdService weIdService){
        this.weIdService = weIdService;
    }

    int getCount(){
        ResponseData<Integer> count = weIdService.getWeIdCount();
        return count.getResult();
    }

    String create(String data) {
        ResponseData<CreateWeIdDataResult> responseData = weIdService.createWeId();
        int index = responseData.getResult().toString().indexOf("did:weid:666:");
        String weId = responseData.getResult().toString().substring(index,index +55);
        int indexStart = responseData.getResult().toString().indexOf("privateKey=");
        int indexEnd = responseData.getResult().toString().indexOf("))");
        String PrivateKey = responseData.getResult().toString().substring(indexStart + 11,indexEnd);
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setType(data);
        serviceArgs.setServiceEndpoint("深圳大学");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(PrivateKey);
        weIdService.setService(weId, serviceArgs, weIdPrivateKey);
        return responseData.getResult().toString();
    }

    String getDIDDocument(String DID){
        ResponseData<String> response = weIdService.getWeIdDocumentJson(DID);
        return response.getResult();
    }

    boolean addMessage(String curriculumDID,String did,String priKey,String curriculumPriKey){
        ServiceArgs serviceArgs1 = new ServiceArgs();
        // TODO:是否使用更详细的信息
        boolean isTeacher;
        isTeacher = getDIDDocument(curriculumDID).contains("教师教学DID");
        int start = getDIDDocument(curriculumDID).indexOf("课程名称:");
        int end = getDIDDocument(curriculumDID).indexOf("*");
        serviceArgs1.setType(getDIDDocument(curriculumDID).substring(start,end) + " 课程DID" + curriculumDID);
        serviceArgs1.setServiceEndpoint("深圳大学");
        WeIdPrivateKey weIdPrivateKey1 = new WeIdPrivateKey();
        weIdPrivateKey1.setPrivateKey(priKey);
        ResponseData<Boolean> response1 = weIdService.setService(did, serviceArgs1, weIdPrivateKey1);
        ResponseData<Boolean> response2;
        ServiceArgs serviceArgs2 = new ServiceArgs();
        serviceArgs2.setServiceEndpoint("深圳大学");
        WeIdPrivateKey weIdPrivateKey2 = new WeIdPrivateKey();
        weIdPrivateKey2.setPrivateKey(curriculumPriKey);
        if (!isTeacher) {
            serviceArgs2.setType("学生DID:" + did);
            response2 = weIdService.setService(curriculumDID, serviceArgs2, weIdPrivateKey2);
        }else {
            serviceArgs2.setType("教师DID:" + did);
            response2 = weIdService.setService(curriculumDID, serviceArgs2, weIdPrivateKey2);
        }
        return response1.getResult() && response2.getResult();
    }
}

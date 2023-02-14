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

    boolean addMessage(String DID,String priKey,String message){
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setType(message);
        serviceArgs.setServiceEndpoint("深圳大学");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey(priKey);
        ResponseData<Boolean> response = weIdService.setService(DID, serviceArgs, weIdPrivateKey);
        return response.getResult();
    }
}

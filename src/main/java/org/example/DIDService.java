package org.example;

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
}

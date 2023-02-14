package org.example;

import com.webank.weid.protocol.base.CredentialPojo;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialPojoService;

import java.util.Map;

public class credentialPojo {

    CredentialPojoService credentialPojoService;

    public credentialPojo(CredentialPojoService credentialPojoService){
        this.credentialPojoService = credentialPojoService;
    }

    String createCredential(String cptID, String issuer, long expirationDate, WeIdAuthentication weIdAuthentication, Map<String, Object> claim){
        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs = new CreateCredentialPojoArgs<>();
        createCredentialPojoArgs.setCptId(Integer.valueOf(cptID));
        createCredentialPojoArgs.setIssuer(issuer);
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + expirationDate);
        createCredentialPojoArgs.setWeIdAuthentication(weIdAuthentication);
        createCredentialPojoArgs.setClaim(claim);
        ResponseData<CredentialPojo> response = credentialPojoService.createCredential(createCredentialPojoArgs);
        return response.getResult().toJson();
    }

    boolean verifyCredential(String issuer,String json) {
        CredentialPojo credentialPojo = CredentialPojo.fromJson(json);
        ResponseData<Boolean> responseVerify = credentialPojoService.verify(issuer,credentialPojo);
        System.out.println(responseVerify);
        return responseVerify.getResult();
    }
}

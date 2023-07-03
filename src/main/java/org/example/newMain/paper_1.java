package org.example.newMain;

import com.webank.weid.protocol.base.Credential;
import com.webank.weid.protocol.base.CredentialWrapper;
import com.webank.weid.protocol.base.WeIdAuthentication;
import com.webank.weid.protocol.base.WeIdPrivateKey;
import com.webank.weid.protocol.request.ServiceArgs;
import com.webank.weid.protocol.response.CreateWeIdDataResult;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialPojoService;
import com.webank.weid.rpc.CredentialService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.CredentialServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;

public class paper_1 {

    public static void main(String[] args) {
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey("19091736990516714524288970013450212621475649300357507513418504949458995746142");
        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
        weIdAuthentication.setAuthenticationMethodId("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1#keys-75ad8301");

//        ResponseData<CreateWeIdDataResult> weIdDataResult = weIdService.createWeId();
//        System.out.println(weIdDataResult);
//         did:weid:666:0x0787cbfebae12f803b68131d6d8dc1d4c55746d7
//         11149960348126133500468592166232515585176863047877305644747533737045535151473349702146147206158203241887705804299267818256757982106459161765592181341663433
//         47633976217119584311206051679348807797923146408231548408101160775981450287754

        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setType("XX大学课程");
        serviceArgs.setServiceEndpoint("666");
        ResponseData<Boolean> response = weIdService.setService("did:weid:666:0x0787cbfebae12f803b68131d6d8dc1d4c55746d7", serviceArgs, new WeIdPrivateKey("47633976217119584311206051679348807797923146408231548408101160775981450287754"));
        System.out.println(response);
        serviceArgs.setType("XX大学课程");
        serviceArgs.setServiceEndpoint("666");
        response = weIdService.setService("did:weid:666:0x0787cbfebae12f803b68131d6d8dc1d4c55746d7", serviceArgs, new WeIdPrivateKey("47633976217119584311206051679348807797923146408231548408101160775981450287754"));
        System.out.println(response);
    }
}

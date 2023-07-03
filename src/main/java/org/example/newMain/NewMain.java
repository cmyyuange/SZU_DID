package org.example.newMain;

import com.webank.weid.protocol.base.*;
import com.webank.weid.protocol.request.CreateCredentialPojoArgs;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CredentialPojoService;
import com.webank.weid.rpc.PolicyService;
import com.webank.weid.rpc.WeIdService;
import com.webank.weid.service.impl.CredentialPojoServiceImpl;
import com.webank.weid.service.impl.PolicyServiceImpl;
import com.webank.weid.service.impl.WeIdServiceImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMain {

    public static void main(String[] args) throws IOException {
        CredentialPojoService credentialPojoService = new CredentialPojoServiceImpl();
        WeIdService weIdService = new WeIdServiceImpl();
        PolicyService policyService = new PolicyServiceImpl();

        WeIdAuthentication weIdAuthentication = new WeIdAuthentication();
        weIdAuthentication.setWeId("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1");
        WeIdPrivateKey weIdPrivateKey = new WeIdPrivateKey();
        weIdPrivateKey.setPrivateKey("19091736990516714524288970013450212621475649300357507513418504949458995746142");
        weIdAuthentication.setWeIdPrivateKey(weIdPrivateKey);
        weIdAuthentication.setAuthenticationMethodId("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1#keys-75ad8301");

//        policyService.registerClaimPolicy(1000,"{\"qwe\":1}",weIdAuthentication);
        List<Integer> claimList = new ArrayList<>();
        claimList.add(1000);
//        System.out.println("PresentationPolicy:" + policyService.registerPresentationPolicy(claimList,weIdAuthentication));
        System.out.println("PolicyCount:" + policyService.getPolicyCount().getResult());
        System.out.println("ClaimPolicy:" + policyService.getClaimPolicy(1000));
        System.out.println("ClaimPoliciesFromCpt:" + policyService.getClaimPoliciesFromCpt(1000));

        CreateCredentialPojoArgs<Map<String, Object>> createCredentialPojoArgs = new CreateCredentialPojoArgs<>();
        createCredentialPojoArgs.setCptId(1000);
        createCredentialPojoArgs.setIssuer("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1");
        createCredentialPojoArgs.setExpirationDate(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));
        createCredentialPojoArgs.setWeIdAuthentication(weIdAuthentication);
        Map<String, Object> claim = new HashMap<>();
        claim.put("qwe", "666666");
        createCredentialPojoArgs.setClaim(claim);
        long startTime = System.currentTimeMillis();
        ResponseData<CredentialPojo> response = credentialPojoService.createCredential(createCredentialPojoArgs);
        long endTime = System.currentTimeMillis();
        System.out.println(response.getResult().toJson());
        List<CredentialPojo> credentialList = new ArrayList<CredentialPojo>();
        credentialList.add(response.getResult());

        BufferedWriter writer = new BufferedWriter(new FileWriter("./NewMainCv.json"));
        writer.write(response.getResult().toJson());
        writer.close();

        ResponseData<Boolean> result = credentialPojoService.verify("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1",response.getResult());
        System.out.println(result);

        Challenge challenge = Challenge.create("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1","666666666666");

        WeIdAuthentication weIdAuthentication1 = new WeIdAuthentication();
        weIdAuthentication1.setWeId("did:weid:666:0xb4bc0879d2477dfd196d9fbcf1409a81dbd8088a");
        WeIdPrivateKey weIdPrivateKey1 = new WeIdPrivateKey();
        weIdPrivateKey1.setPrivateKey("20225565005916659118438541366087030954029403527773112506914830160216163854410");
        weIdAuthentication1.setWeIdPrivateKey(weIdPrivateKey1);
        weIdAuthentication1.setAuthenticationMethodId("did:weid:666:0xb4bc0879d2477dfd196d9fbcf1409a81dbd8088a#keys-ecb4c8e5");

        PresentationPolicyE presentationPolicyE = PresentationPolicyE.create("./policy.json");
        presentationPolicyE.setId(4);
        presentationPolicyE.setVersion(1);
        Map<Integer, ClaimPolicy> policyMap = new HashMap<>();
        policyMap.put(1000, policyService.getClaimPolicy(1000).getResult());
        presentationPolicyE.setPolicy(policyMap);
        presentationPolicyE.setOrgId("SZUDID");
        presentationPolicyE.setPolicyPublisherWeId("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1");

        ResponseData<PresentationE>  presentationE = credentialPojoService.createPresentation(credentialList,presentationPolicyE,challenge,weIdAuthentication);
        System.out.println(presentationE);
        BufferedWriter writer1 = new BufferedWriter(new FileWriter("./NewMainP.json"));
        writer1.write(presentationE.getResult().toJson());
        writer1.close();

        ResponseData<Boolean> verifyRes = credentialPojoService.verify("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1",presentationPolicyE,challenge, presentationE.getResult());
        System.out.println(verifyRes);
        System.out.println(weIdService.getWeIdDocumentJson("did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1"));

        // did:weid:666:0x383e962f7ab8ed4a6e7173758e63f32a415c9be1#keys-75ad8301
        // 19091736990516714524288970013450212621475649300357507513418504949458995746142
    }
}

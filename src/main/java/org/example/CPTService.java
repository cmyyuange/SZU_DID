package org.example;

import com.webank.weid.protocol.base.Cpt;
import com.webank.weid.protocol.response.ResponseData;
import com.webank.weid.rpc.CptService;

public class CPTService {

    CptService cptService;

    public CPTService(CptService cptService) {
        this.cptService = cptService;
    }

    public String registerCurriculumCpt() {
        return "";
    }

    public String queryCpt(String CptID) {
        ResponseData<Cpt> response = cptService.queryCpt(Integer.valueOf(CptID));
        return response.toString();
    }
}

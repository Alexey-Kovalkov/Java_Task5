package learn.task5.check;


import learn.task5.dto.Arrangements;
import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.entity.Agreement;
import learn.task5.repo.AgreementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(1)
public class InstCheckAgreeDoubles implements InstCheckNew, InstCheckTotal{
    @Autowired
    AgreementRepo agreementRepo;
    public CheckResult check(InstCreateJson instCreateJson) {
        CheckResult res = new CheckResult();
        // Проверить есть ли agreement.number == Request.Body.Arrangement[N].Number
        for (Arrangements ar : instCreateJson.getInstanceArrangement() ) {
            if (ar.getNumber() != null){
                Agreement agreementExists = agreementRepo.findFirstByNumber( ar.getNumber() );
            if (agreementExists != null) {
                res.setMsg("Параметр № Дополнительного соглашения (сделки) " + ar.getNumber() +
                        " уже существует для ЭП с ИД " + agreementExists.getProductId().getId());
                res.setStatus(400);
            }
                if (res.getStatus() != 200) {
                    return res;
                }
            }
        }
        return res;
    }
}

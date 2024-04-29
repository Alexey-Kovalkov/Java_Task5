package learn.task5.check;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.entity.TppProduct;
import learn.task5.entity.TppProductRegister;
import learn.task5.repo.TppProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AccCheckDoubles implements AccChecker{
    @Autowired
    private TppProductRepo tppProductRepo;
    public CheckResult check(AccCreateJson accCreateJson) {
        // проверяем tpp_product_register на дубли
        CheckResult res = new CheckResult();
        TppProduct tppProduct = tppProductRepo.findFirstById(accCreateJson.instanceId() );
        if (tppProduct == null) {
            res.setMsg("Отсутствует договор с id = " + accCreateJson.instanceId());
            res.setStatus(400);
            return res;
        }

        for (TppProductRegister rg : tppProduct.getRegisters()) {
            if (rg.getRegisterValue().equals(accCreateJson.registryTypeCode())) {
                res.setMsg("Параметр registryTypeCode тип регистра " + accCreateJson.registryTypeCode() +
                        " уже существует для ЭП с ИД " + accCreateJson.instanceId());
                res.setStatus(400);
                return res;
            }
        }
        return res;
    }
}

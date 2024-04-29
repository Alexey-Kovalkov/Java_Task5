package learn.task5.check;

import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.entity.TppProduct;
import learn.task5.repo.TppProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@Order(0)
public class InsCheckProdID implements InstCheckTotal{
    @Autowired
    TppProductRepo tppProductRepo;
    public CheckResult check(InstCreateJson instCreateJson) {
        CheckResult res = new CheckResult();
        TppProduct tppProduct = tppProductRepo.findFirstById(instCreateJson.getInstanceId());
        if (tppProduct == null) {
            res.setMsg("Экземпляр продукта с параметром instanceId " + instCreateJson.getInstanceId() + " не найден.");
            res.setStatus(400);
        }
        return res;
    }
}

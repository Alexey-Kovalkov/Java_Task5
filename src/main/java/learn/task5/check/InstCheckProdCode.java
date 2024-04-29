package learn.task5.check;

import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.repo.TppProductRegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Set;

@Component
@Order(2)
public class InstCheckProdCode implements InstCheckNew{
    @Autowired
    TppProductRegisterRepo tppProductRegisterRepo;
    public CheckResult check (InstCreateJson instCreateJson) {
        CheckResult res = new CheckResult();
        Set<BigInteger> rTypesSet = tppProductRegisterRepo.ProdRegTypes(instCreateJson.getProductCode());
        if (rTypesSet.isEmpty())  {
            res.setMsg("КодПродукта " + instCreateJson.getProductCode()  +
                    " не найдено в Каталоге продуктов tpp_ref_product_class.");
            res.setStatus(400);
        }

        return res;
    }
}

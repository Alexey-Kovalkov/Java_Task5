package learn.task5.check;

import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.entity.TppProduct;
import learn.task5.repo.TppProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(0)
public class InstCheckProdDouble implements InstCheckNew{
    @Autowired
    TppProductRepo tppProductRepo;
    public CheckResult check (InstCreateJson instCreateJson){
        CheckResult res = new CheckResult();
        // Проверить есть ли tpp_product.number == Request.Body.ContractNumber
        TppProduct pexample = new TppProduct();
        pexample.setContNumber(instCreateJson.getContractNumber());
        Example<TppProduct> example = Example.of(pexample);
        Optional<TppProduct> product = tppProductRepo.findOne(example);
        product.ifPresent(p ->
                {res.setMsg("Параметр ContractNumber № договора " + instCreateJson.getContractNumber() +
                        " уже существует для ЭП с ИД  " + p.getId());
                    res.setStatus(400);
                });
        return res;
    }
}

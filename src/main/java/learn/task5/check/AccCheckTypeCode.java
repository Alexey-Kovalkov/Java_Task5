package learn.task5.check;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.repo.TppProductRegisterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class AccCheckTypeCode implements AccChecker{
    @Autowired
    private TppProductRegisterRepo tppProductRegisterRepo;
    public CheckResult check(AccCreateJson accCreateJson) {
        // проверяем наличие registryTypeCode в tpp_ref_product_register_type
        CheckResult res = new CheckResult();
        Integer typeCodeCount = tppProductRegisterRepo.TypeCodeCount(accCreateJson.registryTypeCode());
        if (typeCodeCount == 0){
            res.setStatus(404);
            res.setMsg("Код Продукта " + accCreateJson.registryTypeCode() +
                    " не найдено в Каталоге продуктов <public.tpp_ref_product_register_type> для данного типа Регистра");
        }
        return res;
    }
}

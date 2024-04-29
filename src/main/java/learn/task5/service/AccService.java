
package learn.task5.service;

import learn.task5.check.AccChecker;
import learn.task5.dto.AccCreateJson;
import learn.task5.dto.AccDto;
import learn.task5.dto.data.AccountResult;
import learn.task5.dto.data.CheckResult;
import learn.task5.entity.Account;
import learn.task5.entity.TppProduct;
import learn.task5.entity.TppProductRegister;
import learn.task5.repo.AccRepo;
import learn.task5.repo.TppProductRegisterRepo;
import learn.task5.repo.TppProductRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Service
@Validated

public class AccService implements AccServ{
    @Autowired
    private final AccRepo accRepo;
    @Autowired
    private TppProductRepo tppProductRepo;
    @Autowired
    private TppProductRegisterRepo tppProductRegisterRepo;
    @Autowired
    public List<AccChecker> checks;
    @Getter
    private Integer status = 200;
    @Getter
    private String message = "";

    private AccountResult returnBad(){
        AccountResult ar = new AccountResult();
        ar.setData("accountId", "null");
        return ar;
    }

    public AccountResult addAccount(AccCreateJson accCreateJson) {
        TppProduct tppProduct;
        boolean bFind;
        CheckResult checkResult;

        for (AccChecker chk : checks) {
            checkResult = chk.check(accCreateJson);
            if (checkResult.getStatus() != 200){
                this.status = checkResult.getStatus();
                this.message = checkResult.getMsg();
                return returnBad();
            }
        }
        // считаем, что всё проверили, в т.ч. instanceId на пустоту и Product на существование
        tppProduct = tppProductRepo.findFirstById(accCreateJson.instanceId());

        // Найти счёт из пула и создать новый ProductRegister
        Set<BigInteger> accIdSet = accRepo.MyAccountId(accCreateJson.branchCode(), accCreateJson.currencyCode(),
                accCreateJson.mdmCode(), accCreateJson.priorityCode(), accCreateJson.registryTypeCode());
        TppProductRegister tppProductRegister = null;
        bFind = false;
        for (BigInteger accId : accIdSet) {
            bFind = true;
            tppProductRegister = new TppProductRegister(tppProduct, accCreateJson.registryTypeCode(), accId, accCreateJson.currencyCode(), "OPEN", "");
            break;
        }
        if (!bFind) {
            this.status = 400;
            this.message = "Ошибка при получении инфо из пула счетов для типа регистра " + accCreateJson.registryTypeCode();
            return returnBad();
        }

        // Сохранить
        AccountResult accountResult = new AccountResult();
        try {
            tppProductRegisterRepo.save(tppProductRegister);
            BigInteger register_id = tppProductRegisterRepo.Prod_register_id();
            accountResult.setData("accountId", register_id.toString());
        } catch (Exception ex) {
            this.message = ex.getMessage();
            this.status = 500;
            return returnBad();
        }
        return accountResult;
    }

}

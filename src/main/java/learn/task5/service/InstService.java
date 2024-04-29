package learn.task5.service;

import learn.task5.check.InstCheckBefore;
import learn.task5.check.InstCheckNew;
import learn.task5.check.InstCheckTotal;
import learn.task5.dto.Arrangements;
import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import learn.task5.dto.data.InstData;
import learn.task5.dto.data.InstResult;
import learn.task5.entity.Agreement;
import learn.task5.entity.TppProduct;
import learn.task5.entity.TppProductRegister;
import learn.task5.repo.AgreementRepo;
import learn.task5.repo.TppProductRegisterRepo;
import learn.task5.repo.TppProductRepo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Validated
public class InstService implements InstServ {
    @Getter
    private Integer status = 200;
    @Getter
    private String message = "";
    @Autowired
    public List<InstCheckBefore> checksBefore;
    @Autowired
    public List<InstCheckNew> checksNew;
    @Autowired
    public List<InstCheckTotal> checksTotal;
    @Autowired
    private TppProductRepo tppProductRepo;
    @Autowired
    private TppProductRegisterRepo tppProductRegisterRepo;
    @Autowired
    private AgreementRepo agreementRepo;

    private InstResult returnResult(InstData instData){
        InstResult ir = new InstResult();
        ir.setMessage(instData.getMessage());
        if (instData.getInstanceId() == null) {
            ir.setData("instanceId", "null");
        } else {
            ir.setData("instanceId", instData.getInstanceId().toString());
        }
        if (instData.getRegisterId() == null){
            ir.setData("registerId", "null");
        } else {
            ir.setData("registerId", instData.getRegisterId().toString());
        }
        if (instData.getSupplementaryAgreementId() == null){
            ir.setData("supplementaryAgreementId", "null");
        }  else {
            ir.setData("supplementaryAgreementId", instData.getSupplementaryAgreementId().toString());
        }
        return ir;
    }

    public BigInteger getClientIdForMdm(String mdmCode) {
        BigInteger result;
        if (mdmCode.isEmpty())
            result = BigInteger.valueOf( -1L);
        else {
            Random random = new Random();
            long idClient = random.nextInt();
            result = BigInteger.valueOf( idClient);
        }
        return result;
    }

    public InstResult addInstance(InstCreateJson instCreateJson){
        TppProduct tppProduct = new TppProduct();
        BigInteger productCodeId;
        BigInteger productId;
        boolean bFind = false;
        TppProductRegister tppProductRegister;
        Agreement agreement;

        InstData instData = new InstData();
        CheckResult checkResult;
        // 3 группы проверок: предварительные, при создании нового экземпляра, общие
        // предварительные
        for (InstCheckBefore chk : checksBefore) {
            checkResult = chk.check(instCreateJson);
            if (checkResult.getStatus() != 200){
                this.status = checkResult.getStatus();
                this.message = checkResult.getMsg();
                instData.setMessage(checkResult.getMsg());
                if (instCreateJson.getInstanceId() != null){
                    instData.setInstanceId(instCreateJson.getInstanceId().toString());
                }
                return returnResult(instData);
            }
        }
        // при создании нового экземпляра
        if (instCreateJson.getInstanceId() == null){
            for (InstCheckNew chk : checksNew) {
                checkResult = chk.check(instCreateJson);
                if (checkResult.getStatus() != 200){
                    this.status = checkResult.getStatus();
                    this.message = checkResult.getMsg();
                    instData.setMessage(this.message);
                    return returnResult(instData);
                }
            }
            // Добавить строку в таблицу tpp_product
            productCodeId = tppProductRepo.CodeProductId(instCreateJson.getProductCode());
            try {
                tppProduct.setProductCodeId(productCodeId);
                tppProduct.setClientId(getClientIdForMdm(instCreateJson.getMdmCode())); // заглушка
                tppProduct.setProductType(instCreateJson.getProductType());
                tppProduct.setContNumber(instCreateJson.getContractNumber());
                tppProduct.setPriority(instCreateJson.getPriority());
                tppProduct.setDateOfConclusion(instCreateJson.getContractDate());
                tppProduct.setPenaltyRate(instCreateJson.getInterestRatePenalty());
                tppProduct.setThresholdAmount(instCreateJson.getThresholdAmount());
                tppProduct.setInterestRateType(instCreateJson.getRateType()); //rateType.ordinal());
                tppProduct.setTaxRate(instCreateJson.getTaxPercentageRate());
                tppProductRepo.save(tppProduct);
            } catch (Exception ex) {
                this.message = "Ошибка при добавлении продукта: " + ex.getMessage();
                this.status = 500;
                instData.setMessage(this.message);
                return returnResult(instData);
            }
            // Получить id-шник нового продукта ...
            productId = tppProductRepo.Prod_id();
            instData.setInstanceId(productId.toString()); // сразу записать в ответ
            instCreateJson.setInstanceId(productId);
            // ... и сам продукт
            tppProduct = tppProductRepo.findFirstById(productId);

            // По типам регистра с ProductCode ...
            Set<BigInteger> rTypesSet = tppProductRegisterRepo.ProdRegTypes(instCreateJson.getProductCode());
            for (BigInteger rType : rTypesSet) {
                // ... отбираем счета из пула счетов ...
                Set<BigInteger> accIdSet = tppProductRegisterRepo.AccountsId(instCreateJson.getBranchCode(), instCreateJson.getIsoCurrencyCode(),
                                                        instCreateJson.getMdmCode(), rType);
                for(BigInteger accId : accIdSet){
                    bFind = true;
                    // ... и пишем в TppProductRegister текущего продукта
                    tppProductRegister = new TppProductRegister(tppProduct, instCreateJson.getRegisterType(), accId, instCreateJson.getIsoCurrencyCode(), "OPEN", "");
                    try {
                        tppProductRegisterRepo.save(tppProductRegister);
                    } catch (Exception ex) {
                        this.message = "Ошибка при записи регистра: " + ex.getMessage();
                        this.status = 500;
                        instData.setMessage(this.message);
                        return returnResult(instData);
                    }
                }
            }
            if (!bFind) {
                this.message = "Ошибка при получении инфо из пула счетов для кода продукта " + instCreateJson.getProductCode();
                this.status = 500;
                instData.setMessage(this.message);
                return returnResult(instData);
            }
        }

        // Общие действия
        // общие проверки
        for (InstCheckTotal chk : checksTotal) {
            checkResult = chk.check(instCreateJson);
            if (checkResult.getStatus() != 200){
                this.status = checkResult.getStatus();
                this.message = checkResult.getMsg();
                instData.setMessage(this.message);
                return returnResult(instData);
            }
        }

        // перечитать продукт
        tppProduct = tppProductRepo.findFirstById(instCreateJson.getInstanceId());
        // прочитать регистеры продукта
        tppProduct.fillRegisters(tppProductRegisterRepo);
        // проходим по доп. соглашениям - сохраняем
        for ( Arrangements ar : instCreateJson.getInstanceArrangement() ) {
            try {
                agreement = new Agreement(tppProduct,
                        ar.getGeneralAgreementId(), ar.getSupplementaryAgreementId(), ar.getArrangementType(), ar.getShedulerJobId(),
                        ar.getNumber(), ar.getOpeningDate(), ar.getClosingDate(), ar.getCancelDate(),
                        ar.getValidityDuration(), ar.getCancellationReason(), ar.getStatus(),
                        ar.getInterestCalculationDate(), ar.getInterestRate(), ar.getCoefficient(), ar.getCoefficientAction(),
                        ar.getMinimumInterestRate(), ar.getMinimumInterestRateCoefficient(), ar.getMinimumInterestRateCoefficientAction(),
                        ar.getMaximalInterestRate(), ar.getMaximalInterestRateCoefficient(), ar.getMaximalInterestRateCoefficientAction());
                agreementRepo.save(agreement);
                tppProduct.insertAgreement(agreementRepo.findFirstById(agreementRepo.Agr_id()));
            } catch (Exception ex) {
                this.message = "Ошибка при записи доп. соглашения: " + ex.getMessage();
                this.status = 500;
                instData.setMessage(this.message);
                return returnResult(instData);
            }
        }

        // запись ответа
        // записать id-шники агриментов и регистеров
        try {
            for (Agreement ag : tppProduct.getAgreements()) {
                instData.setAgrId(ag.getId().toString());
            }
            for (TppProductRegister re : tppProduct.getRegisters()){
                instData.setRegId(re.getId().toString());
            }
        }
        catch (Exception ex) {
            this.message = "Ошибка при записи ответа: " + ex.getMessage();
            this.status = 500;
            instData.setMessage(this.message);
            return returnResult(instData);
        }
        this.status = 200;
        this.message = "";
        instData.setMessage(this.message);
        return returnResult(instData);
    }
}

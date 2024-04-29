package learn.task5.dto;

import jakarta.validation.constraints.NotNull;
import learn.task5.check.Req;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

public record AccCreateJson(
//        @NotNull(message = "instanceId is null")
        @Req
        BigInteger instanceId,
        String registryTypeCode,
        String accountType,
        String currencyCode,
        String branchCode,
        String priorityCode,
        String mdmCode,
        String clientCode,
        String trainRegion,
        String counter,
        String salesCode
) {
}

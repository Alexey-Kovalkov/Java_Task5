package learn.task5.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import learn.task5.check.Req;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class InstCreateJson {
    private BigInteger instanceId;
    @Req
    private String productType;
    @Req
    private String productCode;
    @Req
    private String registerType;
    @Req
    private String mdmCode;
    @Req
    private String contractNumber;
    @Req
    private Date contractDate;
    @Req
    private Integer priority;
    private Double interestRatePenalty;
    private Double minimalBalance;
    private Double thresholdAmount;
    private String accountingDetails;
    private String rateType;
    private Double taxPercentageRate;
    private Double technicalOverdraftLimitAmount;
    private Long contractId;
    @JsonProperty("BranchCode")
    @JsonSetter("BranchCode")
    private String branchCode;
    @JsonProperty("IsoCurrencyCode")
    @JsonSetter("IsoCurrencyCode")
    private String isoCurrencyCode;
    private String urgencyCode;
    @JsonProperty("ReferenceCode")
    @JsonSetter("ReferenceCode")
    private Long referenceCode;
    private AdditionalPropsData additionalPropertiesVip;
    private ArrayList<Arrangements> instanceArrangement;
}

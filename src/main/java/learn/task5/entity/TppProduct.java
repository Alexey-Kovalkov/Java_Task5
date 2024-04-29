package learn.task5.entity;

import jakarta.persistence.*;
import learn.task5.repo.AgreementRepo;
import learn.task5.repo.TppProductRegisterRepo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tpp_product")
public class TppProduct {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "product_code_id")
    private BigInteger productCodeId;

    @Column(name = "client_id")
    private BigInteger clientId;

    @Column(name = "type")
    //@Enumerated( EnumType.STRING)
    //@Type( PostgreSQLEnumType.class)
    //private ProductType productType;
    private String productType;

    @Column(name = "number")
    private String contNumber;

    private Integer priority;

    @Column(name = "date_of_conclusion")
    private Date dateOfConclusion;

    @Column(name = "penalty_rate")
    private Double penaltyRate;

    @Column(name = "threshold_amount")
    private Double thresholdAmount;

    @Column(name = "requisite_type")
    private String requisiteType;

    @Column(name = "interest_rate_type")
    private String interestRateType;

    @Column(name = "tax_rate")
    private Double taxRate;

    //Обратная связь создается с помощью аннотации @OneToMany с параметром mappedBy,
    // значением которого должно быть имя поля в зависимой сущности
    @OneToMany( mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    //
    //если так задавать, то в подчиненной таблице сначала будут insert, а потом update поля product_id
    //@JoinColumn( name = "product_id", referencedColumnName = "id")
    //@Cascade( CascadeType.ALL)
    private List<TppProductRegister> registers = new ArrayList<>();

    public void insertRegister(TppProductRegister register) {
        registers.add(register);
        register.setProductId( this);
    }
    public void deleteRegister(TppProductRegister register) {
        registers.remove(register);
        register.setProductId(null);
    }

    @OneToMany( mappedBy = "productId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agreement> agreements = new ArrayList<>();

    public void insertAgreement(Agreement agreement) {
        agreements.add(agreement);
        agreement.setProductId(this);
    }

public void fillRegisters(TppProductRegisterRepo tppProductRegisterRepo){
    TppProductRegister prexample = new TppProductRegister();
    prexample.setProductId(this);
    Example<TppProductRegister> example = Example.of(prexample);
    List<TppProductRegister> productRegisters = tppProductRegisterRepo.findAll(example);
    for (TppProductRegister pr : productRegisters){
        insertRegister(pr);
    }
}
//    public void deleteAgreement(Agreement agreement) {
//        agreements.remove(agreement);
//        agreement.setProductId(null);
//    }
}

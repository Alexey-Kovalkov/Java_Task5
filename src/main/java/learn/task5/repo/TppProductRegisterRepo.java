package learn.task5.repo;

import learn.task5.entity.TppProductRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface TppProductRegisterRepo extends JpaRepository<TppProductRegister, Long> {
    String select_prod_register_id = "select currval('tpp_product_register_id_seq')";
    @Query(value = select_prod_register_id, nativeQuery = true)
    BigInteger Prod_register_id();

    String selectTypeCode = "select count(*) from tpp_ref_product_register_type where value = ?1";
    @Query(value = selectTypeCode, nativeQuery = true)
    Integer TypeCodeCount(String typeCode);

    String selectProdTypesByCode =  "select t.internal_id " +
                                    "from tpp_ref_product_register_type t " +
                                    "where t.account_type = 'Клиентский' " +
                                    "and t.product_class_code = ?1 " +
                                    "and (t.register_type_start_date is null or t.register_type_start_date <= current_date) " +
                                    "and (t.register_type_end_date is null or t.register_type_end_date >= current_date)";
    @Query(value = selectProdTypesByCode, nativeQuery = true)
    Set<BigInteger> ProdRegTypes(String prodClassCode);

    String selectProdAccounts = "select t3.id id_acc " +
                                "from tpp_ref_product_register_type t1 " +
                                "left join account_pool t2 on t1.value = t2.registry_type_code " +
                                                            "and t2.branch_code = ?1 " +
                                                            "and t2.currency_code = ?2 " +
                                                            "and t2.mdm_code = ?3 " +
                                                            "and t2.priority_code = '00' " +
                                "left join account t3 on t3.account_pool_id = t2.id and t3.bussy = false " +
                                "where t1.internal_id = ?4 ";
    @Query(value = selectProdAccounts, nativeQuery = true)
    Set<BigInteger> AccountsId(String branchCode, String currCode, String mdmCode, BigInteger regTypeID);
}


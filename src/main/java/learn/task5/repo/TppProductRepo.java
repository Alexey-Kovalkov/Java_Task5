package learn.task5.repo;

import learn.task5.entity.TppProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Set;

@Repository
public interface TppProductRepo extends JpaRepository<TppProduct, Long> {
    TppProduct findFirstById(BigInteger id);
    TppProduct findFirstByContNumber(String number);
    String selectIDByCode = "SELECT internal_id FROM tpp_ref_product_class t1 WHERE t1.value = ?1";
    String select_prod_id = "select currval('tpp_product_id_seq')";
    @Query(value = selectIDByCode, nativeQuery = true)
    BigInteger CodeProductId( String productCode);

    @Query(value = select_prod_id, nativeQuery = true)
    BigInteger Prod_id();

}

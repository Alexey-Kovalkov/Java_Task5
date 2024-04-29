package learn.task5.repo;

import learn.task5.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface AgreementRepo extends JpaRepository<Agreement, Long> {
    Agreement findFirstById(BigInteger id);
    Agreement findFirstByNumber(String number);
    String select_agr_id = "select currval('agreement_id_seq')";
    @Query(value = select_agr_id, nativeQuery = true)
    BigInteger Agr_id();
}

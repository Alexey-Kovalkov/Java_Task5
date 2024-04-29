package learn.task5.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Random;

@Service
public class ClientMdm {
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
}

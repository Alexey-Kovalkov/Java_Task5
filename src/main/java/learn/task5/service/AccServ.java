package learn.task5.service;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.AccDto;
import learn.task5.dto.data.AccountResult;
import learn.task5.entity.Account;
import java.util.Optional;

public interface AccServ {
    Integer getStatus();
    String getMessage();
    AccountResult addAccount(AccCreateJson accCreateJson);
    
}

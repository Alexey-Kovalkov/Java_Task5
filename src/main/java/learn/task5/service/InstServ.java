package learn.task5.service;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.AccountResult;
import learn.task5.dto.data.InstResult;

public interface InstServ {
    Integer getStatus();
    String getMessage();
    InstResult addInstance(InstCreateJson instCreateJson);
}

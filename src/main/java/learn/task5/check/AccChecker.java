package learn.task5.check;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.data.CheckResult;

public interface AccChecker {
    CheckResult check (AccCreateJson accCreateJson);
}

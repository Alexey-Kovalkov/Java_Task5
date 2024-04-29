package learn.task5.check;

import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;

public interface InstCheckBefore {
    CheckResult check (InstCreateJson instCreateJson);
}

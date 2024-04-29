package learn.task5.check;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.CheckResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Order(0)
public class InstCheckRequired implements InstCheckBefore{
    public CheckResult check(InstCreateJson instCreateJson) {
        CheckResult res = new CheckResult();
        // проверяем на null поля с аннотацией @Req
        Class cls = instCreateJson.getClass();
        for(Field f : cls.getDeclaredFields())
        {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Req.class) && (f.get(instCreateJson) == null)){
                    res.setMsg("Обязательный параметр " + f.getName() + " не заполнен.");
                    res.setStatus(400);
                }
            } catch(Exception ex) {
                res.setMsg(ex.getMessage());
                res.setStatus(400);
            }
        }
        return res;
    }
}

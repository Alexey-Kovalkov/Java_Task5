package learn.task5.check;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.data.CheckResult;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Order(0)
public class AccCheckRequired implements AccChecker{
    public CheckResult check(AccCreateJson accCreateJson) {
        CheckResult res = new CheckResult();
        // проверяем на null поля с аннотацией @Req
        Class cls = accCreateJson.getClass();
        for(Field f : cls.getDeclaredFields())
        {
            try {
                f.setAccessible(true);
                if (f.isAnnotationPresent(Req.class) && (f.get(accCreateJson) == null)){
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

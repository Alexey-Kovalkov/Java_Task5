package learn.task5.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;



@Getter
@Setter
public class InstResult {
    private String message;
    private HashMap<String, String> data = new HashMap<>();

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String key, String val) {
        this.data.put(key, val);
    }
}

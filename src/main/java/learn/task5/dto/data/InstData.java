package learn.task5.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class InstData {
    private String message;
    private String instanceId;
    private ArrayList<String> registerId;
    private ArrayList<String> supplementaryAgreementId;

    public void InstData() {
        this.registerId = new ArrayList<>();
        this.supplementaryAgreementId = new ArrayList<>();
    }
    public void setAgrId(String id) {
        if (this.supplementaryAgreementId == null) {
            this.supplementaryAgreementId = new ArrayList<>();
        }
        this.supplementaryAgreementId.add(id);
    }
    public void setRegId(String id) {
        if (this.registerId == null) {
            this.registerId = new ArrayList<>();
        }
        this.registerId.add(id);
    }

}

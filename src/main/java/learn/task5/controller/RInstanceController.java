package learn.task5.controller;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.InstCreateJson;
import learn.task5.dto.data.AccountResult;
import learn.task5.dto.data.InstResult;
import learn.task5.service.AccServ;
import learn.task5.service.InstServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RInstanceController {
    @Autowired
    InstServ instServ;
    @PostMapping(value ="corporate-settlement-instance/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstResult> createAcc(@RequestBody InstCreateJson instCreateJson){
        InstResult instResult = instServ.addInstance(instCreateJson);
        Integer st = instServ.getStatus();
        if (st != 200){
            instResult.setMessage(instServ.getMessage());
            return ResponseEntity.status(st).body(instResult);
        } else {
            return ResponseEntity.ok(instResult);
            // return new ResponseEntity<>(accCreateJson, HttpStatus.OK);
        }
    }
}

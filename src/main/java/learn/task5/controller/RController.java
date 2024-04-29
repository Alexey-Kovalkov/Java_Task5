package learn.task5.controller;

import learn.task5.dto.AccCreateJson;
import learn.task5.dto.AccDto;
import learn.task5.dto.data.AccountResult;
import learn.task5.service.AccServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
class RController{
    @Autowired
    AccServ accServ;
    @PostMapping(value ="corporate-settlement-account/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountResult> createAcc(@RequestBody AccCreateJson accCreateJson){
        AccountResult accountResult = accServ.addAccount(accCreateJson);
        Integer st = accServ.getStatus();
        if (st != 200){
            accountResult.setMessage(accServ.getMessage());
            return ResponseEntity.status(st).body(accountResult);
        } else {
            return ResponseEntity.ok(accountResult);
            // return new ResponseEntity<>(accCreateJson, HttpStatus.OK);
        }
    }
}
package learn.task5;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.task5.dto.AccCreateJson;
import learn.task5.dto.Arrangements;
import learn.task5.dto.InstCreateJson;
import learn.task5.service.ClientMdm;
import learn.task5.service.InstService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest( classes = { Task5Application.class })
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = "/process-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class Task5ApplicationTests {
    public static String getRequestBodyFromFile(String fileLocation) throws IOException {
        File file = ResourceUtils.getFile( String.format("classpath:%s", fileLocation));
        return new String(Files.readAllBytes(file.toPath()));
    }
    @Mock
    ClientMdm clientMdm;

    @InjectMocks
    InstService instService;

    @Autowired
    MockMvc mvc;

    @Test
    @Order(1)
    void testIt() throws Exception {
        // Создание объекта ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        InstCreateJson instCreateJson;
        AccCreateJson accCreateJson;

        final String jsonString = getRequestBodyFromFile("instance-create-ok.json");

        Mockito.when(clientMdm.getClientIdForMdm(anyString())).thenReturn( new BigInteger( "1") );

        instCreateJson = mapper.readValue( jsonString, InstCreateJson.class);

        //последовательности <restart with> в process-before.sql
        //проверяем, что при создании возвращаются эти же id
        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(instCreateJson)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.instanceId").value(1))
                .andExpect(jsonPath("$.data.registerId").value("[5, 6, 7]"))
                .andExpect(jsonPath("$.data.supplementaryAgreementId").value("[10]"));

        //второй договор с тем же номером
        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(instCreateJson)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("ContractNumber")));

        instCreateJson = mapper.readValue(jsonString, InstCreateJson.class);

        instCreateJson.setContractNumber( "newNumb");

        //второй договор с другим номером, но таким же номером ДС
        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(instCreateJson)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("№ Дополнительного соглашения")));


        instCreateJson = mapper.readValue( jsonString, InstCreateJson.class);

        instCreateJson.setContractNumber( "newNumb");
        for (Arrangements ar : instCreateJson.getInstanceArrangement())
            ar.setNumber("newDs");
        instCreateJson.setProductCode( "008.07.009");

        // отсутствуют связные записи в Каталоге Типа регистра
        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString( instCreateJson)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("КодПродукта")));

        String jsonRegTypeNull = getRequestBodyFromFile("instance-reg-type-null.json");

        //registerType - обязательное поле
        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-instance/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( jsonRegTypeNull))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("registerType")));

        accCreateJson = new AccCreateJson(new BigInteger( "1"),"02.001.005_45343_CoDowFF", "Клиентский",
                                            "500", "0021", "00", "13",
                                                "", "", "", "");

        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(accCreateJson)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value( 8));

        //проверка на задвоение
        mvc.perform(MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(accCreateJson)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("registryTypeCode")));

        //нет договора
        accCreateJson = new AccCreateJson(new BigInteger( "2"),"02.001.005_45343_CoDowFF", "Клиентский",
                "500", "0021", "00", "13",
                "", "", "", "");

        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString( accCreateJson)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Отсутствует договор")));

        //нет регистра
        accCreateJson = new AccCreateJson(new BigInteger( "1"),"02.001.005_45343_CoDowFF_", "Клиентский",
                "500", "0021", "00", "13",
                "", "", "", "");

        mvc.perform( MockMvcRequestBuilders.post("/corporate-settlement-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content( mapper.writeValueAsString(accCreateJson)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("public.tpp_ref_product_register_type")));
    }

}

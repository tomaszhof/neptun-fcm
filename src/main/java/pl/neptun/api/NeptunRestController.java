package pl.neptun.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.importer.QuestionAnswersImporter;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/api")
public class NeptunRestController {
    Logger logger = LoggerFactory.getLogger(QuestionAnswersImporter.class);

    @GetMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sayHello() {
        HashMap<String, Object> map = new HashMap<>();
        //Get data from service layer into entityList.
        List<Answer> entityList = NeptunDAO.findAll(Answer.class);
        String jsonString = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(entityList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        /*
        for(Answer ans : entityList){
            logger.debug(ans.getText());
            map.put(ans.getCode(), ans.getText());
        }
        */
        return jsonString;
    }
}

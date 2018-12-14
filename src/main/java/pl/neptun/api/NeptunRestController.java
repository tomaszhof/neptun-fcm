package pl.neptun.api;


import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.importer.QuestionAnswersImporter;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/api")
public class NeptunRestController {
    Logger logger = LoggerFactory.getLogger(QuestionAnswersImporter.class);

    @GetMapping(path = "/hello")
    public ResponseEntity<Object> sayHello()
    {
        //Get data from service layer into entityList.
        List<Long> entityList = NeptunDAO.findAllIds(Answer.class);

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (Long n : entityList) {
            JSONObject entity = new JSONObject();

            try {
                //entity.put(n.getCode(), n.getText());
                entity.put("ee", "oo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            entities.add(entity);
        }
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }
}

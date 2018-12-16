package pl.neptun.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.neptun.importer.QuestionAnswersImporter;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;
import pl.neptun.model.Question;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Transactional
@RestController
@RequestMapping("/api")
public class NeptunRestController {
    Logger logger = LoggerFactory.getLogger(QuestionAnswersImporter.class);

    @GetMapping(path = "/getallanswers", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getAllAnswers() {
        HashMap<String, Object> map = new HashMap<>();
        //Get data from service layer into entityList.
        List<Answer> entityList = NeptunDAO.findAll(Answer.class);

        for(Answer ans : entityList){
            logger.debug(ans.getText());
            map.put(ans.getCode(), ans.getText());
        }

        return map;
    }

    @GetMapping(path = "/getallquestions", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, String> getAllQuestions() {
        HashMap<String, String> map = new HashMap<>();
        //Get data from service layer into entityList.
        List<Question> entityList = NeptunDAO.findAll(Question.class);

        for(Question que : entityList){
            String text = que.getText();
            String code = que.getCode();

            logger.debug("Text: " + que.getText());
            logger.debug("Code: " + que.getCode());
            //logger.debug("Text: " + que.getAnswersCodes());
            logger.debug("ID: " + que.getId());
            logger.debug("\n\n\n");

            if(text != null || code != null)
                map.put(que.getCode(), que.getText());
            else
                logger.debug("\nMamy nulla\n");
        }

        return map;
    }

    @RequestMapping(value = "/question/{code}", method = GET)
    @ResponseBody
    public HashMap<String, String> getQuestionByCode(@PathVariable("code") String code) {
        HashMap<String, String> map = new HashMap<>();
        code = code.toUpperCase(); // na wypadek podania małego 'q'
        Question question = NeptunDAO.findByCode(Question.class, code);
        map.put(question.getCode(), question.getText());
        return map;
    }

}

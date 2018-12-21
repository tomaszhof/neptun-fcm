package pl.neptun.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, String> getAllAnswers() {
        HashMap<String, String> map = new HashMap<>();
        //Get data from service layer into entityList.
        List<Answer> entityList = NeptunDAO.findAll(Answer.class);

        for(Answer ans : entityList){
            String code = ans.getCode();
            String text = ans.getText();

            if(code == null)
                code = "";
            if(text == null)
                code = "";
            map.put(code, text);
        }

        return map;
    }

    //zwraca JSON numerPytania:Odpowiedz
    @RequestMapping(path = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, String> getAllQuestions() {
        HashMap<String, String> map = new HashMap<>();
        //Get data from service layer into entityList.
        List<Question> entityList = NeptunDAO.findAll(Question.class);

        for(Question que : entityList){
            String text = que.getText();
            String code = que.getCode();
            String ansCodes = que.getAnswersCodes();

            if(text == null)
                text = "";
            if(code == null)
                code = "";
            if(ansCodes == null)
                ansCodes = "";
            if(code.equals("Q0") || code.equals(" Q0"))
                continue;

            map.put(code, text);
        }
        return map;
    }

    //zwraca "pytanie:możliwe kody odpowiedzi"
    @RequestMapping(value = "/question/{code}", produces = MediaType.APPLICATION_JSON_VALUE, method = GET)
    @ResponseBody
    public HashMap<String, String> getQuestionByCode(@PathVariable("code") String code) {
        HashMap<String, String> map = new HashMap<>();
        code = code.toUpperCase(); // na wypadek podania małego 'q'
        Question question = NeptunDAO.findByCode(Question.class, code);
        map.put(question.getCode(), question.getAnswersCodes());
        return map;
    }

    //zwraca numerOdpowiedzi:Odpowiedz
    @RequestMapping(value = "/answer/{code}", produces = MediaType.APPLICATION_JSON_VALUE, method = GET)
    @ResponseBody
    public HashMap<String, String> getAnswerByCode(@PathVariable("code") String code) {
        HashMap<String, String> map = new HashMap<>();
        code = code.toUpperCase(); // na wypadek podania małego 'a'

        String[] codes = code.split(","); //rodziela zapytania
        logger.debug("\n\nCodeGET : " + code);

        //dodaje do mapy odpowiedzi
        for(String cd : codes)
        {
            logger.debug("\nCode:" + cd + "\n");
            Answer answer = NeptunDAO.findByCode(Answer.class, cd);
            map.put(answer.getCode(), answer.getText());
        }
        return map;
    }

}

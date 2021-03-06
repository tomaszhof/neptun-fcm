package pl.neptun.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import pl.neptun.jpa.AnswersRepository;
import pl.neptun.jpa.QuestionsRepository;
import pl.neptun.model.Answer;
import pl.neptun.model.QAs;
import pl.neptun.model.Question;

import javax.transaction.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Transactional
@RestController
@RequestMapping("/api")
public class NeptunRestController {

	@Autowired
	private QuestionsRepository questionsRepository;
	
	@Autowired
	private AnswersRepository answersRepository;
	
	Logger logger = LoggerFactory.getLogger(NeptunRestController.class);

	@RequestMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HashMap<String, String> getAllAnswers() {
		HashMap<String, String> map = new HashMap<>();
		// Get data from service layer into entityList.
		List<Answer> entityList = answersRepository.findAll();//NeptunDAO.findAll(Answer.class);

		for (Answer ans : entityList) {
			String code = ans.getCode();
			String text = ans.getText();

			if (code == null)
				code = "";
			if (text == null)
				code = "";
			map.put(code, text);
		}

		return map;
	}

	// zwraca JSON numerPytania:Odpowiedz
	@RequestMapping(path = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HashMap<String, String> getAllQuestions() {
		HashMap<String, String> map = new HashMap<>();
		// Get data from service layer into entityList.
		List<Question> entityList = questionsRepository.findAll(); //NeptunDAO.findAll(Question.class);


		for (Question que : entityList) {
			String text = que.getText();
			String code = que.getCode();
			String ansCodes = que.getAnswersCodes();

			if (text == null)
				text = "";
			if (code == null)
				code = "";
			if (ansCodes == null)
				ansCodes = "";

			map.put(code, text);
		}
		return map;
	}

	// zwraca "pytanie:możliwe kody odpowiedzi"
	@RequestMapping(value = "/question/{code}", produces = MediaType.APPLICATION_JSON_VALUE, method = GET)
	@ResponseBody
	@Transactional(Transactional.TxType.REQUIRED)
	public HashMap<String, String> getQuestionByCode(@PathVariable("code") String code) {
		HashMap<String, String> map = new HashMap<>();
		code = code.toUpperCase(); // na wypadek podania małego 'q'
		Question question = questionsRepository.findByCode(code);
		map.put(question.getCode(), question.getAnswersCodes());
		return map;
	}

	// zwraca numerOdpowiedzi:Odpowiedz
	@RequestMapping(value = "/answer/{code}", produces = MediaType.APPLICATION_JSON_VALUE, method = GET)
	@ResponseBody
	public HashMap<String, String> getAnswerByCode(@PathVariable("code") String code) {
		HashMap<String, String> map = new HashMap<>();
		code = code.toUpperCase(); // na wypadek podania małego 'a'

		String[] codes = code.split(","); // rodziela zapytania
		logger.debug("\n\nCodeGET : " + code);

		// dodaje do mapy odpowiedzi
		for (String cd : codes) {
			logger.debug("\nCode:" + cd + "\n");
			Answer answer = answersRepository.findByCode(cd); //NeptunDAO.findByCode(Answer.class, cd);
			map.put(answer.getCode(), answer.getText());
		}
		return map;
	}


	@GetMapping(value = "/code")
	@ResponseBody
	public QAs getAllCodes(){
		QAs qAs = new QAs();
		qAs.setAnswers(answersRepository.findAll());
		qAs.setQuestions(questionsRepository.findAll());



		return qAs;
	}

	
	@SuppressWarnings("resource")
	@RequestMapping(value = "/rules", method = RequestMethod.GET, produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String getFile() {
		String filePath = "data/";
		String fileName = "32_RULE_WAY_OF_COMPUTER_PROGRAM.csv";
		String fileContent = "NO CONTENT";
		Resource resource = new ClassPathResource(filePath+ fileName);
		try {
				InputStream in = resource.getInputStream();
				fileContent = new BufferedReader(new InputStreamReader(in, "UTF-8")).lines().collect(Collectors.joining("\n"));
			} catch (IOException e) {
					fileContent = "RULES FILE NOT FOUND!";
					logger.error("Reading rules file error!");
				
			}
		
		return fileContent;
	   
	}


}

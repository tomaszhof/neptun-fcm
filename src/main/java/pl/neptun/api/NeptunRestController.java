package pl.neptun.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Answer;
import pl.neptun.model.Question;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	
	Logger logger = LoggerFactory.getLogger(NeptunRestController.class);

	@RequestMapping(path = "/answers", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HashMap<String, String> getAllAnswers() {
		HashMap<String, String> map = new HashMap<>();
		// Get data from service layer into entityList.
		List<Answer> entityList = NeptunDAO.findAll(Answer.class);

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
		List<Question> entityList = NeptunDAO.findAll(Question.class);

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
	public HashMap<String, String> getQuestionByCode(@PathVariable("code") String code) {
		HashMap<String, String> map = new HashMap<>();
		code = code.toUpperCase(); // na wypadek podania małego 'q'
		Question question = NeptunDAO.findByCode(Question.class, code);
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
			Answer answer = NeptunDAO.findByCode(Answer.class, cd);
			map.put(answer.getCode(), answer.getText());
		}
		return map;
	}

	@RequestMapping(value = "/rules", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	String getRules() {
		return "data/32_RULE_WAY_OF_COMPUTER_PROGRAM.csv";
	}
	
	@SuppressWarnings("resource")
	@RequestMapping(value = "api/rules", method = RequestMethod.GET)
	@ResponseBody
	public void getFile(HttpServletResponse response) {
		logger.info("BUKA !!! BUKA !!! PROCESSING REQUEST  rules file...");
		String filePath = "data/";
		String fileName = "32_RULE_WAY_OF_COMPUTER_PROGRAM.csv";
		String fileContent = "BUKA";
		Resource resource = new ClassPathResource(filePath+ fileName);
		try {
			logger.info("Read rules file...");
				InputStream in = resource.getInputStream();
				fileContent
				= new BufferedReader(new InputStreamReader(in, "UTF-8")).lines().collect(Collectors.joining("\n"));;
				FileCopyUtils.copy(fileContent.getBytes(), response.getOutputStream());
				response.getOutputStream().println("PUSTO TUTAJ!");
				logger.info("done.");
			} catch (IOException e) {
				try {
					response.getOutputStream().println("PUSTO TUTAJ!");
					logger.error("Reading rules file error!");
				} catch (IOException e1) {
					logger.error("Reading rules file - problem in error handling method.");
				}
			}
		
//		if (!resource.exists())
//			return new FileSystemResource("data/PUSTO.csv");
		
		response.setContentType("text/plain; charset=utf-8");
	    response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
	    response.setHeader("Content-Length", String.valueOf(fileContent.length()));
		
	   
	}

	@RequestMapping(value = "api/rulestest", method = RequestMethod.GET)
	@ResponseBody
	public void export(HttpServletResponse response ) 
		    throws IOException {
		    String myString = "Tutaj jakaś buka";
		    response.setContentType("text/plain");
		    response.setHeader("Content-Disposition","attachment;filename=myFile.txt");
		    ServletOutputStream out = response.getOutputStream();
		    out.println(myString);
		    out.flush();
		    out.close();
		}
	
	@RequestMapping(value = "api/rulestest2", method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public void export2(HttpServletResponse response ) 
		    throws IOException {
		    String myString = "{ Tutaj jakaś buka}";
		    response.setContentType("text/plain");
		    response.setHeader("Content-Disposition","attachment;filename=myFile.txt");
		    ServletOutputStream out = response.getOutputStream();
		    out.println(myString);
		    out.flush();
		    out.close();
		}

}

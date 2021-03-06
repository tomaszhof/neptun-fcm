package pl.neptun.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import pl.neptun.jpa.QuestionsRepository;
import pl.neptun.model.Question;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class QuestionAnswersImporter extends FileImporter {

	Logger logger = LoggerFactory.getLogger(QuestionAnswersImporter.class);

	@Autowired
	private QuestionsRepository questionsRepository;
	
	public QuestionAnswersImporter() {
		super();
	}


	@Override
	@javax.transaction.Transactional(Transactional.TxType.REQUIRED)
	public void processData() {
		Resource resource = new ClassPathResource(path + fileName);
		logger.debug("loading questions-answers...");
		try {
			logger.debug("loaded data - questions-answers");
			InputStream resourceInputStream = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream, StandardCharsets.UTF_8));

			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				String questionCode = null;
				String answersCodes = null;


				String[] data = strLine.split(":");
				if (data.length > 1) {
					questionCode = data[1];

				}
				if (data.length > 2) {
					answersCodes = data[2];

					//if ((questionCode != null) && (answersCodes != null)) {
						logger.debug("Question: " + questionCode + " Answers: " + answersCodes);
						Question q = questionsRepository.findByCode(questionCode);//NeptunDAO.findByCode(Question.class, questionCode);
						//logger.debug("test: " + q);
						q.setAnswersCodes(answersCodes);
						logger.debug("before persist.");
						//em.merge(q);
						questionsRepository.save(q);
						logger.debug("after persist.");
					//}
					logger.debug(strLine);
				}

			}

			// Close the input stream
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("Jakis blad!!!" + e.getMessage());
			e.printStackTrace();
		}
	}
}

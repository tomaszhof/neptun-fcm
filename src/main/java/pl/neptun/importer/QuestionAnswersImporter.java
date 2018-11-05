package pl.neptun.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.neptun.jpa.NeptunDAO;
import pl.neptun.model.Question;

public class QuestionAnswersImporter extends FileImporter {

	Logger logger = LoggerFactory.getLogger(QuestionsImporter.class);

	public QuestionAnswersImporter(String fileName) {
		super(fileName);
	}

	@Override
	public void processData() {
		Resource resource = new ClassPathResource(path + fileName);
		logger.debug("loading questions-answers...");
		try {
			logger.debug("loaded data - questions-answers");
			InputStream resourceInputStream = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream, "UTF-8"));

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
					if ((questionCode != null) && (answersCodes != null)) {
						logger.debug("Question: " + questionCode + " Answers: " + answersCodes);
						Question q = NeptunDAO.findByCode(Question.class, questionCode);
						q.setAnswersCodes(answersCodes);
						logger.debug("before persist.");
						em.merge(q);
						logger.debug("after persist.");
					}
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

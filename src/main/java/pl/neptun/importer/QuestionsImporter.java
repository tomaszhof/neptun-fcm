package pl.neptun.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.neptun.model.Question;

public class QuestionsImporter extends FileImporter {
	Logger logger = LoggerFactory.getLogger(QuestionsImporter.class);

	public QuestionsImporter(String fileName) {
		super(fileName);
	}

	@Override
	public void processData() {
		Resource resource = new ClassPathResource("data/1_MODEL_Q_QUESTIONS.csv");
		logger.debug("loading questions...");
		try {
			logger.debug("loaded data - questions");
			InputStream resourceInputStream = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream, "UTF-8"));

			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				String questionCode = null;
				String questionText = null;

				String[] data = strLine.split(";");
				if (data.length > 0) {
					questionCode = data[0];
				}
				if (data.length > 1) {
					questionText = data[1];
					questionText = questionText.replaceAll("_", " ");
				}
				if ((questionCode != null) && (questionText != null)) {
					Question q = new Question();
					q.setCode(questionCode);
					q.setText(questionText);
					logger.debug("before persist.");
					em.merge(q);
					logger.debug("after persist.");
				}
				logger.debug(strLine);
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

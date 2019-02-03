package pl.neptun.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import pl.neptun.jpa.AnswersRepository;
import pl.neptun.model.Answer;

@Component
public class AnswersImporter extends FileImporter {
	Logger logger = LoggerFactory.getLogger(AnswersImporter.class);
	
	@Autowired
	private AnswersRepository answersRepository;
	
	public AnswersImporter() {
		super();
	}

	@Override
	public void processData() {
		Resource resource = new ClassPathResource(path+fileName);
		logger.debug("loading answers...");
		try {
			logger.debug("loaded data - answers");
			InputStream resourceInputStream = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream, "UTF-8"));

			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				String answerCode = null;
				String answerText = null;

				String[] data = strLine.split(";");
				if (data.length > 0) {
					answerCode = data[0];
				}
				if (data.length > 1) {
					answerText = data[1];
					answerText = answerText.replaceAll("_", " ");
				}
				if ((answerCode != null) && (answerText != null)) {
					Answer a = new Answer();
					a.setCode(answerCode);
					a.setText(answerText);
					logger.debug("before persist.");
					//em.merge(a);
					answersRepository.save(a);
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

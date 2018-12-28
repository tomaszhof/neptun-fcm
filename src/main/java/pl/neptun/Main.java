/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.neptun;

import org.jscience.physics.amount.Amount;
import org.jscience.physics.model.RelativisticModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.neptun.importer.AnswersImporter;
import pl.neptun.importer.QuestionAnswersImporter;
import pl.neptun.importer.QuestionsImporter;
import pl.neptun.model.Question;
import pl.neptun.model.User;

import javax.measure.quantity.Mass;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.measure.unit.SI.KILOGRAM;

@Controller
@SpringBootApplication
public class Main {

	/*
	https://stackoverflow.com/questions/44839753/returning-json-object-as-response-in-spring-boot?rq=1

	REFACTOR
	wystaw JSON'a
	/api/test

	class NeptunRestController /api
		/questions
		/answers
			ew. jedno po kodzie
		/Q&A

		http response content type JSON -> wszystkie po bożemu że to json
	 */

	@Value("${spring.datasource.url}")
	private String dbUrl;
//
//  @Autowired
//  private DataSource dataSource;

	private EntityManagerFactory emf;

	private void initializeHibernate() {
		System.out.println("Set hibernate connection...");
		emf = Persistence.createEntityManagerFactory("UnitNeptunFCMTest");// , configOverrides);
		System.out.println("success.");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@RequestMapping("/")
	String index() {
		return "index";
	}

	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		return "db";
	}

	@RequestMapping("/test")
	String test(Map<String, Object> model) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			initializeHibernate();
			output.add("Initialized Hibernate connection.");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();
			output.add("Transaction began.");
			User testUser = new User();
			testUser.setFirstName("Jan");
			testUser.setLastName("Kowalski");
			output.add("before persist.");
			em.persist(testUser);
			output.add("after persist.");
			em.getTransaction().commit();

			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<User> criteria = builder.createQuery(User.class);
			Root<User> personRoot = criteria.from(User.class);
			criteria.select(personRoot);
			criteria.where(builder.like(personRoot.get("firstName"), "Kowalski"));
			List<User> people = em.createQuery(criteria).getResultList();

			for (User u : people) {
				output.add("User from db: " + u.getFirstName() + " " + u.getLastName() + ";");
			}
			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", "Jakis blad!!!" + e.getMessage());
			return "error";
		}
	}

	@RequestMapping("/hello")
	String hello(Map<String, Object> model) {
		RelativisticModel.select();
		Amount<Mass> m = Amount.valueOf("12 GeV").to(KILOGRAM);
		model.put("science", "E=mc^2: 12 GeV = " + m.toString());
		return "hello";
	}

	@RequestMapping("/questions")
	String load(Map<String, Object> model) {
		Resource resource = new ClassPathResource("data/1_MODEL_Q_QUESTIONS.csv");
		ArrayList<String> output = new ArrayList<String>();
		output.add("loading questions...");
		try {
			output.add("loaded data - questions");
			InputStream resourceInputStream = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceInputStream, "UTF-8"));

			String strLine;
			initializeHibernate();
			output.add("Initialized Hibernate connection.");
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

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
					output.add("before persist.");
					em.persist(q);
					output.add("after persist.");
				}
				output.add(strLine);
			}

			// Close the input stream
			br.close();
			em.getTransaction().commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			model.put("message", "Jakis blad!!!" + e.getMessage());
			e.printStackTrace();
		}
		model.put("records", output);
		return "db";
	}

	@RequestMapping("/importer")
	String importData(Map<String, Object> model) {
		QuestionsImporter qi = new QuestionsImporter("1_MODEL_Q_QUESTIONS.csv");
		AnswersImporter ai = new AnswersImporter("2_MODEL_A_ANSWERS.csv");
		QuestionAnswersImporter qai = new QuestionAnswersImporter("3_MODEL_QA_QUESTIONS_AND_ANSWERS_TOGETHER.csv");
		ArrayList<String> output = new ArrayList<String>();
		output.add("loading questions...");
		try {
			qi.doImport();
			output.add("loaded data - questions");
			ai.doImport();
			output.add("loaded data - answers");
			qai.doImport();
			output.add("loaded data - questions-answers");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			model.put("message", "Jakis blad!!!" + e.getMessage());
			e.printStackTrace();
			output.add("Jakis blad!!!" + e.getMessage());
		}
		model.put("records", output);
		return "db";
	}

}

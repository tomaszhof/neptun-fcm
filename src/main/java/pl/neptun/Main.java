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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import pl.neptun.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.measure.unit.SI.KILOGRAM;
import javax.measure.quantity.Mass;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jscience.physics.model.RelativisticModel;
import org.jscience.physics.amount.Amount;


@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;
//
//  @Autowired
//  private DataSource dataSource;
  
  private EntityManagerFactory emf;

  private void initializeHibernate() {
	  Map<String, String> env = System.getenv();
	  Map<String, Object> configOverrides = new HashMap<String, Object>();
	  //configOverrides.put("hibernate.connection.url", dbUrl);
	  //System.out.println("Set hibernate connection url: " + dbUrl);
//	  for (String envName : env.keySet()) {
//	      if (envName.contains("JDBC_DATABASE_URL")) {
//	          configOverrides.put("hibernate.connection.url", "BUKA");//env.get(envName));    
//	      }
//	      // You can put more code in here to populate configOverrides...
//	  }
	  emf = Persistence.createEntityManagerFactory("UnitNeptunFCMTest");//, configOverrides);
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
//    try (Connection connection = dataSource.getConnection()) {
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
//      ArrayList<String> output = new ArrayList<String>();
//      while (rs.next()) {
//        output.add("Read from DB as TH: " + rs.getTimestamp("tick"));
//      }
//
//      model.put("records", output);
      return "db";
//    } catch (Exception e) {
//      model.put("message", e.getMessage());
//      return "error";
//    }
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
    	criteria.where( builder.like(personRoot.get("firstName"), "Kowalski" ) );
    	List<User> people = em.createQuery( criteria ).getResultList();
    	
    	
    	for (User u : people) {
    		output.add("User from db: " + u.getFirstName() + " " + u.getLastName()+";");
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


//  @Bean
//  public DataSource dataSource() throws SQLException {
//    if (dbUrl == null || dbUrl.isEmpty()) {
//      return new HikariDataSource();
//    } else {
//      HikariConfig config = new HikariConfig();
//      config.setJdbcUrl(dbUrl);
//      return new HikariDataSource(config);
//    }
//  }

}

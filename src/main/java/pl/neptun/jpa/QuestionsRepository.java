package pl.neptun.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.neptun.model.Question;

public interface QuestionsRepository  extends JpaRepository<Question, Long>{
	public Question findByCode(String code);
}

package pl.neptun.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.neptun.model.Answer;

public interface AnswersRepository  extends JpaRepository<Answer, Long>{
	public Answer findByCode(String code);
}

package pl.neptun.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="questions")
public class Question {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String code;
	
	@Lob
	private String text;
	
	private String answersCodes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAnswersCodes() {
		return answersCodes;
	}

	public void setAnswersCodes(String answersCodes) {
		this.answersCodes = answersCodes;
	}

}

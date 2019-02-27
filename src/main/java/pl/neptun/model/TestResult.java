package pl.neptun.model;

import javax.persistence.*;
import java.util.ArrayList;

//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tests_results")
public class TestResult {
	
	public TestResult() {
	}
	
//	@JsonCreator
//	TestResult(@JsonProperty("id") final Long id, @JsonProperty("beforeAnswers") final String beforeAnswers,
//			@JsonProperty("afterAnswers") final String afterAnswers, @JsonProperty("shortestPath") final Long shortestPath) {
//		super();
//		this.id = id;
//		this.beforeAnswers = beforeAnswers;
//		this.afterAnswers = afterAnswers;
//		this.shortestPath = shortestPath;
//	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(columnDefinition="text", length=10485760)
	private String beforeAnswers; //Phase 1

	@Column(columnDefinition="text", length=10485760)
	private String afterAnswers; //Phase 2

	private Long shortestPath;

	private Long realPath;

	private Long deviation;

	private Long maxDeviation;

	private Long integralU;


//	@ManyToOne
//	@JoinColumn(name = "user_id", updatable=false, insertable=false)
//	@NotNull
//	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBeforeAnswers() {
		return beforeAnswers;
	}

	public void setBeforeAnswers(String beforeAnswers) {
		this.beforeAnswers = beforeAnswers;
	}

	public String getAfterAnswers() {
		return afterAnswers;
	}

	public void setAfterAnswers(String afterAnswers) {
		this.afterAnswers = afterAnswers;
	}

	public Long getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(Long shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Long getRealPath() {
		return realPath;
	}

	public void setRealPath(Long realPath) {
		this.realPath = realPath;
	}

	public Long getDeviation() {
		return deviation;
	}

	public void setDeviation(Long deviation) {
		this.deviation = deviation;
	}

	public Long getMaxDeviation() {
		return maxDeviation;
	}

	public void setMaxDeviation(Long maxDeviation) {
		this.maxDeviation = maxDeviation;
	}

	public Long getIntegralU() {
		return integralU;
	}

	public void setIntegralU(Long integralU) {
		this.integralU = integralU;
	}


//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

}

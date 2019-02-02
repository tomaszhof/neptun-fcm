package pl.neptun.service;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.neptun.model.TestResult;

public interface TestResultsRepository extends JpaRepository<TestResult, Long> {
	
//	public TestResult findByUser();
}

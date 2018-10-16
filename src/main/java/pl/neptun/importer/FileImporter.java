package pl.neptun.importer;

import javax.persistence.EntityManager;

import pl.neptun.jpa.NeptunDAO;

public abstract class FileImporter {
	
	protected String path;
	protected String fileName;
	protected EntityManager em = NeptunDAO.em();
	
	public FileImporter(String fileName) {
		this.path = "data/";
		this.fileName = fileName;
	}
	
	abstract public void processData();
	
	public void doImport() {
		em.getTransaction().begin();
		processData();
		em.getTransaction().commit();
	};
	
}
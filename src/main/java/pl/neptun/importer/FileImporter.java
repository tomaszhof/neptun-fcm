package pl.neptun.importer;



public abstract class FileImporter {
	
	protected String path;
	protected String fileName;
	//protected EntityManager em = NeptunJPA.em();
	
	public FileImporter() {
		this.path = "data/";
		this.fileName = null;
	}
	
	abstract public void processData();
	
	public void doImport(String fileName) {
		this.fileName = fileName;
		//NeptunJPA.importBegin(NeptunJPA.TRANSACTION_CARDINALITY);
		////em.getTransaction().begin();
		processData();
		////em.getTransaction().commit();
		//NeptunJPA.importEnd(NeptunJPA.TRANSACTION_CARDINALITY);
	}
	
}

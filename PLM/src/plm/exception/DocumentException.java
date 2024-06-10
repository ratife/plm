package plm.exception;

import plm.model.Document;

public class DocumentException extends EntityException {

	private static final long serialVersionUID = 1L;

	public DocumentException(String msg) {
		super(" Document error : " + msg);
	}
	
	public DocumentException(String msg, Document doc) {
		super(" Document error : " + msg +  doc.toString());
	}
}

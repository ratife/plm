package plm.exception;

import plm.model.Part;

public class PartException extends EntityException {
	
	private static final long serialVersionUID = 1L;

	public PartException(String msg) {
		super(" Part Error : " + msg);
	}
	
	public PartException(String msg, Part part) {
		super(" Part error : " + msg  + part.toString());
	}
}

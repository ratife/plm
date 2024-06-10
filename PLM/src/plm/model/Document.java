package plm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Document extends EntityPML{

	@Column
	private String documentAttribute1;
	
	@Column
	private String documentAttribute2;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "part_reference", referencedColumnName = "reference"),
        @JoinColumn(name = "part_version", referencedColumnName = "version"),
        @JoinColumn(name = "part_iteration", referencedColumnName = "iteration")
    })	
	protected Part part;
	
	public Document(String reference, String version, int iteration) {
		super(reference, version, iteration);
	}

	public boolean isReserved() 
	{
		return reserved;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public String getDocumentAttribute1() {
		return documentAttribute1;
	}

	public String getDocumentAttribute2() {
		return documentAttribute2;
	}


	public void setDocumentAttribute1(String documentAttribute1) {
		this.documentAttribute1 = documentAttribute1;
	}

	public void setDocumentAttribute2(String documentAttribute2) {
		this.documentAttribute2 = documentAttribute2;
	}
	
	
	
	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

		
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		stringBuilder.append(",documentAttribute1:"+documentAttribute1);
		stringBuilder.append(",documentAttribute1:"+documentAttribute1);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}

	
		
}
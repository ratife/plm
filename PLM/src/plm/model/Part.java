package plm.model;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Part extends EntityPML{
	
	@Column
	private String partAttribute1;
	
	@Column
	private String partAttribute2;
	
	@OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	protected Set<Document> document; 
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "parent_reference", referencedColumnName = "reference"),
        @JoinColumn(name = "parent_version", referencedColumnName = "version"),
        @JoinColumn(name = "parent_iteration", referencedColumnName = "iteration")
    })	
	protected Part parent;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
	protected Set<Part> children; 

	public Part(String reference, String version, int iteration) {
		super(reference, version, iteration);
	}

	public String getReference() {
		return id.getReference();
	}

	public String getVersion() {
		return id.getVersion();
	}

	public int getIteration() {
		return id.getIteration() ;
	}

	public boolean isReserved() {
		return this.reserved;
	}

	public String getReservedBy() {
		return reservedBy;
	}


	public String getPartAttribute1() {
		return partAttribute1;
	}

	public String getPartAttribute2() {
		return partAttribute2;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	public void setPartAttribute1(String partAttribute1) {
		this.partAttribute1 = partAttribute1;
	}

	public void setPartAttribute2(String partAttribute2) {
		this.partAttribute2 = partAttribute2;
	}

	public Set<Document> getDocument() {
		return document;
	}

	public void setDocument(Set<Document> document) {
		this.document = document;
	}

	public Part getParent() {
		return parent;
	}

	public void setParent(Part parent) {
		this.parent = parent;
	}

	public Set<Part> getChildren() {
		return children;
	}

	public void setChildren(Set<Part> children) {
		this.children = children;
	}
	
	@Override
	public void changeState(String state) {
		super.changeState(state);
		getDocument().forEach(document->{
			document.setLifeCycleState(state);
		});
	}

	@Override
	public void reserve(String userId) {
		super.reserve(userId);
		/*
		if(children!=null)
			children.forEach(child->{
				child.reserve(userId);
	    	});
		*/
    	if(document!=null)
    		document.forEach(child->{
    			child.reserve(userId);
	    	});
	}
	
	@Override
	public void free() {
		super.free();
		/*
		if(children!=null)
			children.forEach(child->{
	    		child.free();
	    	});
		*/
    	if(document!=null)
    		document.forEach(child->{
	    		child.free();
	    	});
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		stringBuilder.append(",partAttribute1:"+partAttribute1);
		stringBuilder.append(",partAttribute2:"+partAttribute2);
		stringBuilder.append(",child:[");
		if(children!=null) {
			stringBuilder.append(children.stream().map(Part::toString).collect(Collectors.joining(",")));
		}
		stringBuilder.append("]");
		stringBuilder.append(",doc:[");
    	if(document!=null) {
    		stringBuilder.append(document.stream().map(Document::toString).collect(Collectors.joining(",")));
    	}
    	stringBuilder.append("]");
    	stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	
	
}

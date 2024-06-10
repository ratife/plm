package plm.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntityPML {
	
	@EmbeddedId
	protected EntityPK id;
	
	@Column
	protected boolean reserved;
	
	@Column
	protected String  reservedBy;
	
	@Column
	private String lifeCycleState;
	
	@ManyToOne
	private VersionSchema versionSchema;
	
	@ManyToOne
	private LifeCycleTemplate lifeCycleTemplate;
	
	
	public EntityPML() {	
	}
	
	public EntityPML(String reference, String version, int iteration) {
		this.id = new EntityPK(reference,version,iteration);	
	}
	
	public EntityPML(EntityPK id) {	
		this.id = id;
	}
	
	public EntityPK getId() {
		return id;
	}

	public void setId(EntityPK id) {
		this.id = id;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public String getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(String reservedBy) {
		this.reservedBy = reservedBy;
	}

	public String getLifeCycleState() {
		return lifeCycleState;
	}

	public void setLifeCycleState(String lifeCycleState) {
		this.lifeCycleState = lifeCycleState;
	}
	
	public LifeCycleTemplate getLifeCycleTemplate() {
		return lifeCycleTemplate;
	}

	public void setLifeCycleTemplate(LifeCycleTemplate lifeCycleTemplate) {
		this.lifeCycleTemplate = lifeCycleTemplate;
	}

	public VersionSchema getVersionSchema() {
		return versionSchema;
	}

	public void setVersionSchema(VersionSchema versionSchema) {
		this.versionSchema = versionSchema;
	}
	
	public void reserve(String userId) {
		reservedBy = userId;
		reserved = true;
	}
	
	public void free() {
		reservedBy = null;
		reserved = false;
	}
	
	public void changeState(String state) {
		setLifeCycleState(state);
	}
	
	public boolean isCorrectNextState(String state) {
		return getLifeCycleTemplate().getNextSate(state).equals(this.getLifeCycleState());
	}

	@Override
	public String toString() {
		return "{id:"+id+",reserved:"+reserved+",reservedBy:"+reservedBy+",lifeCycleState:"+lifeCycleState;
	}
}
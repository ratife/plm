package plm.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class EntityPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String reference;
	
	private String version;
	
	private int iteration;
	
	public EntityPK() {
	}
	
	public EntityPK(String reference, String version, int iteration) {
		this.reference = reference;
		this.version = version;
		this.iteration = iteration;
	}
	
	
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getIteration() {
		return iteration;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(iteration, reference, version);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityPK other = (EntityPK) obj;
		return iteration == other.iteration && Objects.equals(reference, other.reference)
				&& Objects.equals(version, other.version);
	}

	@Override
	public String toString() {
		return "{reference:"+reference+",version:"+version+",iteration:"+iteration+"}";
	}
	
	
}
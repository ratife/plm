package plm.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LifeCycleTemplate {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lifeCycleTemplate;

    @Column(nullable = false)
    private String separator;

    
	public String getInitialState() {
		String[] states = lifeCycleTemplate.split(separator);
		return states[0];
	}
	public boolean isKnown(String lifeCycleState) {
		String[] states = lifeCycleTemplate.split(separator);
		return Arrays.asList(states).contains(lifeCycleState);
	}
	public boolean isFinal(String lifeCycleState) {
		String[] states = lifeCycleTemplate.split(separator);
		return states[states.length-1].equals(lifeCycleState);
	}
	public String getNextSate(String state) {
		String[] states = lifeCycleTemplate.split(separator);
		int index = Arrays.asList(states).indexOf(state);
		return states[index+1];
	}
}

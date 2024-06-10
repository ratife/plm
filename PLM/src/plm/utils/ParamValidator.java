package plm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ParamValidator {
	
	@Autowired
	private MessageSource messageSource;
	
	public void validateServiceParameters(String reference, String version,String userId, int iteration) {
        if (reference == null || reference.trim().isEmpty()) {
            throw new IllegalArgumentException("Reference " + Utils.message(messageSource,"field.not_empty"));
        }
        if (version == null || version.trim().isEmpty()) {
        	throw new IllegalArgumentException("Version " + Utils.message(messageSource,"field.not_empty"));
        }
        if (userId == null || userId.trim().isEmpty()) {
        	throw new IllegalArgumentException("userId " + Utils.message(messageSource,"field.not_empty"));
        }
        if (iteration <= 0) {
        	throw new IllegalArgumentException("iteration " + Utils.message(messageSource,"field.positif"));
        }
    }
}

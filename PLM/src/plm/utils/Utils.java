package plm.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class Utils {
	public static String message(MessageSource messageSource,String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
}

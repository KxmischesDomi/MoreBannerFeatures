package de.kxmischesdomi.morebannerfeatures.core.config;

import de.kxmischesdomi.morebannerfeatures.core.config.options.BooleanOption;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MBFOptions {

	public static final BooleanOption BAR;
	public static final BooleanOption ERRORS;

	static {
		BAR = new BooleanOption("bar", false);
		ERRORS = new BooleanOption("errors", true);
	}

}

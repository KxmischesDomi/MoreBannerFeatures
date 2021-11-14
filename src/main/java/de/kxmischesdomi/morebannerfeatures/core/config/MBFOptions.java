package de.kxmischesdomi.morebannerfeatures.core.config;

import de.kxmischesdomi.morebannerfeatures.core.config.options.BooleanOption;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class MBFOptions {

	public static final BooleanOption BAR;
	public static final BooleanOption ERRORS;
	public static final BooleanOption PIG_BANNERS;
	public static final BooleanOption BOAT_BANNERS;
	public static final BooleanOption STRIDER_BANNERS;
	public static final BooleanOption HANING_BANNERS;

	static {
		BAR = new BooleanOption("bar", false);
		ERRORS = new BooleanOption("errors", true);
		PIG_BANNERS = new BooleanOption("pig_banners", true).display(false);
		BOAT_BANNERS = new BooleanOption("boat_banners", true).display(false);
		STRIDER_BANNERS = new BooleanOption("strider_banners", true).display(false);
		HANING_BANNERS = new BooleanOption("hanging_banners", true).display(false);
	}

}

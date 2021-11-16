package de.kxmischesdomi.morebannerfeatures.core.config;

import de.kxmischesdomi.morebannerfeatures.core.config.options.BooleanOption;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class MBFOptions {

	public static final BooleanOption BAR;
	public static final BooleanOption FOX_CORRECTION;
	public static final BooleanOption BANNER_GLINT;
	public static final BooleanOption HORSE_SLOT;
	public static final BooleanOption ERRORS;
	public static final BooleanOption PIG_BANNERS;
	public static final BooleanOption BOAT_BANNERS;
	public static final BooleanOption STRIDER_BANNERS;
	public static final BooleanOption HANGING_BANNERS;
	public static final BooleanOption VILLAGER_BANNERS;

	static {
		BAR = new BooleanOption("bar", false);
		FOX_CORRECTION = new BooleanOption("fox_correction", true);
		BANNER_GLINT = new BooleanOption("banner_glint", false);
		HORSE_SLOT = new BooleanOption("horse_slot", true);
		ERRORS = new BooleanOption("errors", true);

		PIG_BANNERS = new BooleanOption("pig_banners", true).display(false);
		BOAT_BANNERS = new BooleanOption("boat_banners", true).display(false);
		STRIDER_BANNERS = new BooleanOption("strider_banners", true).display(false);
		HANGING_BANNERS = new BooleanOption("hanging_banners", true).display(false);
		VILLAGER_BANNERS = new BooleanOption("villager_banners", true).display(false);
	}

}

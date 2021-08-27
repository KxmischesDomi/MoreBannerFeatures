package de.kxmischesdomi.morebannerfeatures;

import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import net.fabricmc.api.ModInitializer;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "morebannerfeatures";
	public static final boolean developmentBuild = true;

	@Override
	public void onInitialize() {

		if (developmentBuild) {
			DevelopmentUtils.initDevelopmentTools();
		}

	}

	public static boolean isTrinketsInstalled() {
		return false;
	}


}
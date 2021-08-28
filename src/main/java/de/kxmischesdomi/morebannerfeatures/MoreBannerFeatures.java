package de.kxmischesdomi.morebannerfeatures;

import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import dev.emi.trinkets.TrinketsMain;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "morebannerfeatures";
	public static final boolean developmentBuild = true;

	public static boolean trinketsInstalled;

	public static final Identifier BANNER_BACKGROUND = new Identifier(MoreBannerFeatures.MOD_ID, "textures/gui/background.png");

	@Override
	public void onInitialize() {

		if (developmentBuild) {
			DevelopmentUtils.initDevelopmentTools();
		}

		try {
			String modId = TrinketsMain.MOD_ID;
			trinketsInstalled = true;
		} catch (Exception exception) {
			 trinketsInstalled = false;
		}

	}

	public static boolean isTrinketsInstalled() {
		return trinketsInstalled;
	}


}
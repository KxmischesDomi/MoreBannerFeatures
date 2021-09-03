package de.kxmischesdomi.morebannerfeatures;

import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.util.Identifier;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "morebannerfeatures";
	public static final boolean developmentBuild = false;

	public static Boolean trinketsInstalled = null;

	@Override
	public void onInitialize() {

		if (developmentBuild) {
			DevelopmentUtils.initDevelopmentTools();
		}

	}

	public static boolean isTrinketsInstalled() {
		if (trinketsInstalled == null) {
			trinketsInstalled = FabricLoader.INSTANCE.getAllMods().stream().anyMatch(modContainer -> modContainer.getMetadata().getId().equalsIgnoreCase("trinkets"));
		}
		return trinketsInstalled;
	}


}
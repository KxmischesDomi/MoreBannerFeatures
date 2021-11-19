package de.kxmischesdomi.morebannerfeatures;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFConfigManager;
import de.kxmischesdomi.morebannerfeatures.utils.DevelopmentUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.FabricLoader;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "morebannerfeatures";
	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
	public static final boolean developmentBuild = true;

	public static Boolean trinketsInstalled = null;

	@Override
	public void onInitialize() {
		MBFConfigManager.load();

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
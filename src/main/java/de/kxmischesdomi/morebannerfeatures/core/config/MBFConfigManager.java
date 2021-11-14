package de.kxmischesdomi.morebannerfeatures.core.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import de.kxmischesdomi.morebannerfeatures.core.config.options.IOption;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.Option;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class MBFConfigManager {

	private static File configFile;

	private static List<IOption> cachedOptions;

	public static void loadConfigFile() {
		if (configFile == null) {
			configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), MoreBannerFeatures.MOD_ID + ".json");
		}
	}

	public static void load() {
		loadConfigFile();

		try {
			if (!configFile.exists()) {
				save();
			}
			if (configFile.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(configFile));
				JsonObject json = new JsonParser().parse(br).getAsJsonObject();

				for (IOption option : getAllOptions()) {
					option.read(json);
				}

			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	public static void save() {
		loadConfigFile();

		JsonObject json = new JsonObject();

		try {
			for (IOption option : getAllOptions()) {
				option.write(json);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		String jsonString = MoreBannerFeatures.GSON.toJson(json);

		try (FileWriter fileWriter = new FileWriter(configFile)) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save Mod Menu configuration file");
			e.printStackTrace();
		}

	}

	public static List<IOption> getAllOptions() {

		if (cachedOptions != null && !cachedOptions.isEmpty()) {
			return cachedOptions;
		}

		List<IOption> options = new ArrayList<>();
		for (Field field : MBFOptions.class.getDeclaredFields()) {
			try {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
					if (IOption.class.isAssignableFrom(field.getType())) {
						options.add(((IOption) field.get(null)));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		cachedOptions = options;
		return options;
	}

	public static Option[] getAllAsOptions() {
		return getAllOptions().stream().map(IOption::toOption).toArray(Option[]::new);
	}

}

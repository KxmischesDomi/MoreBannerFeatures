package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.TranslatableText;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BooleanOption implements IOption {

	private final String key;

	private boolean value;
	private boolean defaultValue;

	public BooleanOption(String key) {
		this.key = key;
		this.value = false;
	}

	public BooleanOption(String key, boolean defaultValue) {
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void write(JsonObject config) {
		config.addProperty(key, value);
	}

	@Override
	public void read(JsonObject config) {
		JsonPrimitive member = config.getAsJsonPrimitive(key);
		value = member == null ? defaultValue : member.getAsBoolean();
	}

	@Override
	public Option toOption() {
		return CyclingOption.create("mbf.options." + key, new TranslatableText("mbf.tooltip." + key), ignored -> value, (gameOptions, option, newValue) -> {
			this.value = newValue;
		});
	}

	public boolean getBooleanValue() {
		return value;
	}

}

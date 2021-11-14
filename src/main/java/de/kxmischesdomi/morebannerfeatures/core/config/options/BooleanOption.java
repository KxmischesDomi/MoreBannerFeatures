package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class BooleanOption implements IOption {

	private final String key;

	private boolean value;
	private boolean defaultValue;
	private boolean tooltip = true;
	private boolean display = true;

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
		CyclingOption<Boolean> cyclingOption = CyclingOption.create("mbf.options." + key, ignored -> value, (gameOptions, option, newValue) -> {
			this.value = newValue;
		});
		if (tooltip) {
			cyclingOption.tooltip(client -> {
				List<OrderedText> list = client.textRenderer.wrapLines(new TranslatableText("mbf.tooltip." + key), 200);
				return (value) -> {
					return list;
				};
			});
		}
		return cyclingOption;
	}

	@Override
	public boolean shouldDisplay() {
		return display;
	}

	public boolean getBooleanValue() {
		return value;
	}

	public BooleanOption display(boolean display) {
		this.display = display;
		return this;
	}

	public BooleanOption tooltip(boolean tooltip) {
		this.tooltip = tooltip;
		return this;
	}

}

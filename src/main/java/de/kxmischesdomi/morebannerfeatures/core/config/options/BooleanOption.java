package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class BooleanOption implements IOption {

	private final String key;

	private boolean value;
	private boolean defaultValue;
	private Component tooltip;
	private boolean display = true;

	public BooleanOption(String key) {
		this.key = key;
		this.value = false;
		this.tooltip = new TranslatableComponent("mbf.tooltip." + key);
	}

	public BooleanOption(String key, boolean defaultValue) {
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.tooltip = new TranslatableComponent("mbf.tooltip." + key);
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
	public Object toOption() {
		net.minecraft.client.CycleOption<Boolean> cyclingOption = net.minecraft.client.CycleOption.createOnOff("mbf.options." + key, ignored -> value, (gameOptions, option, newValue) -> {
			this.value = newValue;
		});
		if (tooltip != null) {
			cyclingOption.setTooltip(client -> (value) -> client.font.split(tooltip, 200));
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

	public BooleanOption tooltip(Component tooltip) {
		this.tooltip = tooltip;
		return this;
	}

}

package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

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
		this.tooltip = Component.translatable("mbf.tooltip." + key);
	}

	public BooleanOption(String key, boolean defaultValue) {
		this.key = key;
		this.value = defaultValue;
		this.defaultValue = defaultValue;
		this.tooltip = Component.translatable("mbf.tooltip." + key);
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
		if (tooltip != null) {
			return OptionInstance.createBoolean("mfb.options." + key, (value) -> Tooltip.create(tooltip), value, newValue -> {
				this.value = newValue;
			});
		} else {
			return OptionInstance.createBoolean("mbf.options." + key, value, newValue -> {
				this.value = newValue;
			});
		}
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

package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;
import net.minecraft.client.option.Option;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface IOption {
	String getKey();
	void write(JsonObject config);
	void read(JsonObject config);
	Option toOption();
}

package de.kxmischesdomi.morebannerfeatures.core.config.options;

import com.google.gson.JsonObject;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public interface IOption {
	String getKey();
	void write(JsonObject config);
	void read(JsonObject config);
	boolean shouldDisplay();

	/**
	 * @return the option as a object to prevent the server from initializing a client class. Will result in an exception if the value is not an Option.
	 */
	Object toOption();
}

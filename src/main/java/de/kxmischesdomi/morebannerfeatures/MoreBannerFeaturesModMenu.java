package de.kxmischesdomi.morebannerfeatures;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.kxmischesdomi.morebannerfeatures.renderer.screen.MBFOptionsScreen;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class MoreBannerFeaturesModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return MBFOptionsScreen::new;
	}

}

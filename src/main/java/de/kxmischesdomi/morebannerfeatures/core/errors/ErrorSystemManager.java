package de.kxmischesdomi.morebannerfeatures.core.errors;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class ErrorSystemManager {

	private static long lastTimeReported = -1;

	public static void reportException() {
		if (canBeReportedAgain()) {
			lastTimeReported = System.currentTimeMillis();
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null) {
				MutableComponent text = MutableComponent.create(new TranslatableContents("mbf.message.error"));
				Style style = text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/7BSqZa9r3P"));
				text.setStyle(style);
				player.displayClientMessage(text, false);
			}
		}
	}

	public static boolean canBeReportedAgain() {
		return MBFOptions.ERRORS.getBooleanValue() && (lastTimeReported == -1 || System.currentTimeMillis() - lastTimeReported >= 60000);
	}

}

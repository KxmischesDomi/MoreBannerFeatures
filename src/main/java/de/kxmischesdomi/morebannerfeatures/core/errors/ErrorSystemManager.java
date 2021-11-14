package de.kxmischesdomi.morebannerfeatures.core.errors;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.1.0
 */
public class ErrorSystemManager {

	private static long lastTimeReported = -1;

	public static void reportException() {
		if (canBeReportedAgain()) {
			lastTimeReported = System.currentTimeMillis();
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				TranslatableText text = new TranslatableText("mbf.message.error");
				Style style = text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/7BSqZa9r3P"));
				text.setStyle(style);
				player.sendMessage(text, false);
			}
		}
	}

	public static boolean canBeReportedAgain() {
		return MBFOptions.ERRORS.getBooleanValue() && (lastTimeReported == -1 || System.currentTimeMillis() - lastTimeReported >= 60000);
	}

}

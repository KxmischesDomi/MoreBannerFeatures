package de.kxmischesdomi.morebannerfeatures.renderer.screen;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFConfigManager;
import de.kxmischesdomi.morebannerfeatures.core.config.options.IOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MBFOptionsScreen extends GameOptionsScreen {

	private final Screen previous;
	private ButtonListWidget buttonList;

	@SuppressWarnings("resource")
	public MBFOptionsScreen(Screen previous) {
		super(previous, MinecraftClient.getInstance().options, new TranslatableText("mbf.options"));
		this.previous = previous;
	}

	protected void init() {
		this.buttonList = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
		this.buttonList.addAll(getAllToDisplay());
		this.addSelectableChild(this.buttonList);
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
			MBFConfigManager.save();
			this.client.setScreen(this.previous);
		}));
	}

	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.buttonList.render(matrices, mouseX, mouseY, delta);
		drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
		super.render(matrices, mouseX, mouseY, delta);
		List<OrderedText> tooltip = getHoveredButtonTooltip(this.buttonList, mouseX, mouseY);
		if (tooltip != null) {
			this.renderOrderedTooltip(matrices, tooltip, mouseX, mouseY);
		}

	}

	public void removed() {
		MBFConfigManager.save();
	}

	public static Option[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(IOption::toOption).map(optionHolder -> ((Option) optionHolder.getOption())).toArray(Option[]::new);
	}

}

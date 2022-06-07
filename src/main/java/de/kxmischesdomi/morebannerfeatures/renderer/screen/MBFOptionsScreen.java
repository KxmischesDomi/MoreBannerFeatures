package de.kxmischesdomi.morebannerfeatures.renderer.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import de.kxmischesdomi.morebannerfeatures.core.config.MBFConfigManager;
import de.kxmischesdomi.morebannerfeatures.core.config.options.IOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MBFOptionsScreen extends OptionsSubScreen {

	private final Screen previous;
	private OptionsList buttonList;

	@SuppressWarnings("resource")
	public MBFOptionsScreen(Screen previous) {
		super(previous, Minecraft.getInstance().options, MutableComponent.create(new TranslatableContents("mbf.options")));
		this.previous = previous;
	}

	protected void init() {
		this.buttonList = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.buttonList.addSmall(getAllToDisplay());
		this.addWidget(this.buttonList);
		this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE, (button) -> {
			MBFConfigManager.save();
			this.minecraft.setScreen(this.previous);
		}));
	}

	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		this.buttonList.render(matrices, mouseX, mouseY, delta);
		drawCenteredString(matrices, this.font, this.title, this.width / 2, 5, 0xffffff);
		super.render(matrices, mouseX, mouseY, delta);
		List<FormattedCharSequence> tooltip = tooltipAt(this.buttonList, mouseX, mouseY);
		if (tooltip != null) {
			this.renderTooltip(matrices, tooltip, mouseX, mouseY);
		}

	}

	public void removed() {
		MBFConfigManager.save();
	}

	public static OptionInstance<?>[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(iOption -> ((OptionInstance<?>) iOption.toOption())).toArray(OptionInstance[]::new);
	}

}

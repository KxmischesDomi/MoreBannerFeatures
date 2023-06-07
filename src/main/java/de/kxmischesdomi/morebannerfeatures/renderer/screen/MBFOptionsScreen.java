package de.kxmischesdomi.morebannerfeatures.renderer.screen;

import de.kxmischesdomi.morebannerfeatures.core.config.MBFConfigManager;
import de.kxmischesdomi.morebannerfeatures.core.config.options.IOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class MBFOptionsScreen extends OptionsSubScreen {

	private final Screen previous;
	private OptionsList buttonList;

	@SuppressWarnings("resource")
	public MBFOptionsScreen(Screen previous) {
		super(previous, Minecraft.getInstance().options, Component.translatable("mbf.options"));
		this.previous = previous;
	}

	protected void init() {
		this.buttonList = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		this.buttonList.addSmall(getAllToDisplay());
		this.addWidget(this.buttonList);
		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
			MBFConfigManager.save();
			this.minecraft.setScreen(this.previous);
		}).bounds(this.width / 2 - 100, this.height - 27, 200, 20).createNarration(supplier -> MutableComponent.create(CommonComponents.GUI_DONE.getContents())).build());
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(guiGraphics);
		this.buttonList.render(guiGraphics, mouseX, mouseY, delta);
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 0xffffff);
		super.render(guiGraphics, mouseX, mouseY, delta);
		basicListRender(guiGraphics, buttonList, mouseX, mouseY, delta);
	}

	public void removed() {
		MBFConfigManager.save();
	}

	public static OptionInstance<?>[] getAllToDisplay() {
		return MBFConfigManager.getAllOptions().stream().filter(IOption::shouldDisplay).map(iOption -> ((OptionInstance<?>) iOption.toOption())).toArray(OptionInstance[]::new);
	}

}

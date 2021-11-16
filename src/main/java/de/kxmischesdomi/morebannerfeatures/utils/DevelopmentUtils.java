package de.kxmischesdomi.morebannerfeatures.utils;

import de.kxmischesdomi.morebannerfeatures.MoreBannerFeatures;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class DevelopmentUtils {

	// DEVELOPMENT TOOLS
	public static Vec3 offset = Vec3.ZERO;
	public static Vec3 scale = new Vec3(1, 1, 1);
	public static boolean applyEntityOffsets = true;
	static long lastMillis = 0;

	public static void initDevelopmentTools() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> executeDevRenderTools(player, hand) ? InteractionResult.CONSUME : InteractionResult.PASS);
		UseItemCallback.EVENT.register((player, world1, hand) -> {
			ItemStack stackInHand = player.getItemInHand(hand);
			return executeDevRenderTools(player, hand) ? InteractionResultHolder.sidedSuccess(stackInHand, false) : InteractionResultHolder.pass(stackInHand);
		});
	}

	private static boolean executeDevRenderTools(Player player, InteractionHand hand) {
		if (!MoreBannerFeatures.developmentBuild) {
			return false;
		}

		ItemStack stackInHand = player.getItemInHand(hand);

		boolean actionItem = false;
		boolean scaleItem = false;

		if (lastMillis == 0 || System.currentTimeMillis() > lastMillis + 50) {
			lastMillis = System.currentTimeMillis();
			boolean add = hand != InteractionHand.OFF_HAND;
			actionItem = true;

			if (stackInHand.is(Items.LIME_DYE)) {
				// Y
				modify(add, 0, getOffset(player), 0);
			} else if (stackInHand.is(Items.RED_DYE)) {
				// X
				modify(add, getOffset(player), 0, 0);
			} else if (stackInHand.is(Items.BLUE_DYE)) {
				// Z
				modify(add, 0, 0, getOffset(player));
			} else if (stackInHand.is(Items.NETHER_STAR)) {
				offset = Vec3.ZERO;
				scale = new Vec3(1, 1, 1);
			} else if (stackInHand.is(Items.LEAD)) {
				applyEntityOffsets = !applyEntityOffsets;
				player.displayClientMessage(new TextComponent("§7Entity Offsets: " + (applyEntityOffsets ? "§a" : "§c") + applyEntityOffsets), true);
				return true;
			} else if (stackInHand.is(Items.ORANGE_DYE)) {
				modifyScale(add, getOffset(player), 0, 0);
				scaleItem = true;
			} else if (stackInHand.is(Items.GREEN_DYE)) {
				modifyScale(add, 0, getOffset(player), 0);
				scaleItem = true;
			} else if (stackInHand.is(Items.LIGHT_BLUE_DYE)) {
				modifyScale(add, 0, 0, getOffset(player));
				scaleItem = true;
			} else if (!stackInHand.is(Items.NAME_TAG)) {
				actionItem = false;
			}

		}

		if (actionItem) {
			Vec3 offset = DevelopmentUtils.offset;
			if (scaleItem) {
				offset = DevelopmentUtils.scale;
			}
			player.displayClientMessage(new TextComponent("§7Offset: §c" + round(offset.x()) + " §8| §a" + round(offset.y()) + " §8| §9" + round(offset.z())), true);
			return true;
		}

		return false;
	}

	private static void modify(boolean add, float x, float y, float z) {
		if (add) offset = offset.add(x, y, z);
		else offset = offset.subtract(x, y, z);
	}
	private static void modifyScale(boolean add, float x, float y, float z) {
		if (add) scale = scale.add(x, y, z);
		else scale = scale.subtract(x, y, z);
	}

	private static double round(double value) {
		return round(value, 2);
	}

	private static float getOffset(Player playerEntity) {
		if (playerEntity.isShiftKeyDown()) return 0.01f;
		return 0.1f;
	}

	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

}

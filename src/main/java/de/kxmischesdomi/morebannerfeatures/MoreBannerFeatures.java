package de.kxmischesdomi.morebannerfeatures;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;

public class MoreBannerFeatures implements ModInitializer {

	public static final String MOD_ID = "morebannerfeatures";
	public static final boolean developmentBuild = true;

	@Override
	public void onInitialize() {

	}

	// DEVELOPMENT TOOLS
	public static Vec3d offset = Vec3d.ZERO;
	public static Vec3d scale = new Vec3d(1, 1, 1);
	public static boolean applyEntityOffsets = true;
	static long lastMillis = 0;

	static {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> executeDevRenderTools(player, hand) ? ActionResult.CONSUME : ActionResult.PASS);
		UseItemCallback.EVENT.register((player, world1, hand) -> {
			ItemStack stackInHand = player.getStackInHand(hand);
			return executeDevRenderTools(player, hand) ? TypedActionResult.success(stackInHand, false) : TypedActionResult.pass(stackInHand);
		});
	}

	private static boolean executeDevRenderTools(PlayerEntity player, Hand hand) {
		if (!developmentBuild) {
			return false;
		}

		ItemStack stackInHand = player.getStackInHand(hand);

		boolean actionItem = false;
		boolean scaleItem = false;

		if (lastMillis == 0 || System.currentTimeMillis() > lastMillis + 50) {
			lastMillis = System.currentTimeMillis();
			boolean add = hand != Hand.OFF_HAND;
			actionItem = true;

			if (stackInHand.isOf(Items.LIME_DYE)) {
				// Y
				modify(add, 0, getOffset(player), 0);
			} else if (stackInHand.isOf(Items.RED_DYE)) {
				// X
				modify(add, getOffset(player), 0, 0);
			} else if (stackInHand.isOf(Items.BLUE_DYE)) {
				// Z
				modify(add, 0, 0, getOffset(player));
			} else if (stackInHand.isOf(Items.NETHER_STAR)) {
				offset = Vec3d.ZERO;
				scale = new Vec3d(1, 1, 1);
			} else if (stackInHand.isOf(Items.LEAD)) {
				applyEntityOffsets = !applyEntityOffsets;
				player.sendMessage(new LiteralText("§7Entity Offsets: " + (applyEntityOffsets ? "§a" : "§c") + applyEntityOffsets), true);
				return true;
			} else if (stackInHand.isOf(Items.ORANGE_DYE)) {
				modifyScale(add, getOffset(player), 0, 0);
				scaleItem = true;
			} else if (stackInHand.isOf(Items.GREEN_DYE)) {
				modifyScale(add, 0, getOffset(player), 0);
				scaleItem = true;
			} else if (stackInHand.isOf(Items.LIGHT_BLUE_DYE)) {
				modifyScale(add, 0, 0, getOffset(player));
				scaleItem = true;
			} else if (!stackInHand.isOf(Items.NAME_TAG)) {
				actionItem = false;
			}

		}

		if (actionItem) {
			Vec3d offset = MoreBannerFeatures.offset;
			if (scaleItem) {
				offset = MoreBannerFeatures.scale;
			}
			player.sendMessage(new LiteralText("§7Offset: §c" + round(offset.getX()) + " §8| §a" + round(offset.getY()) + " §8| §9" + round(offset.getZ())), true);
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

	private static float getOffset(PlayerEntity playerEntity) {
		if (playerEntity.isSneaking()) return 0.01f;
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
package de.kxmischesdomi.morebannerfeatures.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * Used for future features!
 *
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class BannerUtils {

	public static List<DyeColor> BANNER_COLORS;
	public static List<Item> BANNER_ITEMS;

	public static ItemStack getRandomBannerItemStack(Random random) {
		ItemStack itemStack = new ItemStack(BannerUtils.getRandomBannerItem(random));
		BannerUtils.putPatternsOnStack(itemStack, BannerUtils.generateRandomVillageBannerPatterns(random));
		return itemStack;
	}

	public static Item getRandomBannerItem(Random random) {
		return BANNER_ITEMS.get(random.nextInt(BANNER_ITEMS.size()));
	}

	public static List<Pair<BannerPattern, DyeColor>> generateRandomVillageBannerPatterns(Random random) {
		List<Pair<BannerPattern, DyeColor>> patterns = Lists.newArrayList();

		List<BannerPattern> allowedToUse = Lists.newArrayList(BannerPattern.values());

		for (int i = 0; i < 4; i++) {
			BannerPattern pattern = allowedToUse.get(random.nextInt(allowedToUse.size()));
			patterns.add(new Pair<>(pattern, BANNER_COLORS.get(random.nextInt(BANNER_COLORS.size()))));

			allowedToUse = Lists.newArrayList(BannerPattern.values());
			allowedToUse.remove(pattern);
		}

		return patterns;
	}

	public static void putPatternsOnStack(ItemStack itemStack, List<Pair<BannerPattern, DyeColor>> patterns) {
		NbtCompound nbtCompound = itemStack.getOrCreateSubNbt("BlockEntityTag");

		NbtList nbtList2;
		if (nbtCompound.contains("Patterns", 9)) {
			nbtList2 = nbtCompound.getList("Patterns", 10);
		} else {
			nbtList2 = new NbtList();
			nbtCompound.put("Patterns", nbtList2);
		}

		for (Pair<BannerPattern, DyeColor> pattern : patterns) {
			NbtCompound nbtCompound2 = new NbtCompound();
			nbtCompound2.putString("Pattern", pattern.getFirst().getId());
			nbtCompound2.putInt("Color", pattern.getSecond().getId());
			nbtList2.add(nbtCompound2);
		}

	}

	static {
		BANNER_ITEMS = Lists.newArrayList();
		for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntries()) {
			if (entry.getValue() instanceof BannerItem) {
				BANNER_ITEMS.add(entry.getValue());
			}
		}

		DyeColor[] BANNED_BANNER_COLOR = { DyeColor.PINK, DyeColor.MAGENTA, DyeColor.PURPLE };
		BANNER_COLORS = Lists.newArrayList(DyeColor.values());
		BANNER_COLORS.removeAll(Arrays.asList(BANNED_BANNER_COLOR));

	}

}

package net.teamfruit.emcgadgets;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModConfig {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	private static final ForgeConfigSpec.ConfigValue<List<? extends String>> KEY_ITEM_NAMES;

	public static Set<Item> keyItems = new HashSet<>();

	static {
		BUILDER.comment("EMCGadgets Configuration");

		KEY_ITEM_NAMES = BUILDER
				.comment("Items required to use EMC with Building Gadgets",
						"The player must have one of these items in their inventory")
				.defineList("keyItems",
						Arrays.asList("projecte:transmutation_tablet"),
						obj -> obj instanceof String);

		SPEC = BUILDER.build();
	}

	public static void register() {
		ModLoadingContext.get().registerConfig(Type.COMMON, SPEC);
	}

	public static void loadKeyItems() {
		keyItems.clear();
		for (String name : KEY_ITEM_NAMES.get()) {
			ResourceLocation loc = ResourceLocation.tryParse(name);
			if (loc != null) {
				Item item = ForgeRegistries.ITEMS.getValue(loc);
				if (item != null) {
					keyItems.add(item);
				}
			}
		}
		EMCGadgets.LOGGER.info("Loaded {} key items: {}", keyItems.size(), keyItems);
	}
}

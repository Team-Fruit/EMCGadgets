package net.teamfruit.emcgadgets;

import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class EMCInventoryManipulation {

	@CoreInvoke
	public static int useEmc(ItemStack target, EntityPlayer player, int amountRequired) {
		if (hasTablet(player.inventory.mainInventory)) {
			IKnowledgeProvider provider = PersonalEMC.get(player);

			if (provider.hasKnowledge(target)) {
				long itemEmc = EMCHelper.getEmcValue(target);
				long placeableCount = provider.getEmc() / itemEmc;
				if (placeableCount > 0) {
					long useEmc = amountRequired * itemEmc;
					provider.setEmc(provider.getEmc() - useEmc);
					return longToInt(placeableCount);
				}
			}
		}
		return 0;
	}

	@CoreInvoke
	public static long countEmc(ItemStack itemStack, EntityPlayer player) {
		if (hasTablet(player.inventory.mainInventory)) {
			IKnowledgeProvider provider = PersonalEMC.get(player);

			if (provider.hasKnowledge(itemStack)) {
				long itemEmc = EMCHelper.getEmcValue(itemStack);
				if (itemEmc <= 0L) return 0L;
				long placeableCount = provider.getEmc() / itemEmc;
				if (placeableCount > 0)
					return placeableCount;
			}
		}
		return 0L;
	}

	private static boolean hasTablet(NonNullList<ItemStack> inventory) {
		return inventory.stream()
				.anyMatch(s -> ModConfig.keyItems.contains(s.getItem()));
	}

	private static int longToInt(long count) {
		try {
			return Math.toIntExact(count);
		} catch (ArithmeticException var3) {
			return Integer.MAX_VALUE;
		}
	}

}
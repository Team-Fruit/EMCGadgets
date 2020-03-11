package net.teamfruit.emcgadgets;

import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.gameObjs.items.TransmutationTablet;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EMCInventoryManipulation {

	@CoreInvoke
	public static int useEmc(ItemStack target, EntityPlayer player, int amountRequired) {
		boolean hasTable = player.inventory.mainInventory.stream()
				.anyMatch(s -> s.getItem() instanceof TransmutationTablet);
		if (hasTable) {
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
		boolean hasTable = player.inventory.mainInventory.stream()
				.anyMatch(s -> s.getItem() instanceof TransmutationTablet);
		if (hasTable) {
			IKnowledgeProvider provider = PersonalEMC.get(player);

			if (provider.hasKnowledge(itemStack)) {
				long itemEmc = EMCHelper.getEmcValue(itemStack);
				long placeableCount = provider.getEmc() / itemEmc;
				if (placeableCount > 0)
					return placeableCount;
			}
		}
		return 0L;
	}

	public static int longToInt(long count) {
		try {
			return Math.toIntExact(count);
		} catch (ArithmeticException var3) {
			return Integer.MAX_VALUE;
		}
	}

}
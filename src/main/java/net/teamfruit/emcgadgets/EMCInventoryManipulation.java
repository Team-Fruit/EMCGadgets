package net.teamfruit.emcgadgets;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.math.BigInteger;

public class EMCInventoryManipulation {

	public static int useEmc(ItemStack target, PlayerEntity player, int amountRequired) {
		return player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).map(provider -> {
			if (!hasTablet(player.inventory.items)) {
				EMCGadgets.LOGGER.debug("useEmc: No tablet found");
				return 0;
			}
			if (!provider.hasKnowledge(target)) {
				EMCGadgets.LOGGER.debug("useEmc: No knowledge of {}", target);
				return 0;
			}
			long itemEmc = ProjectEAPI.getEMCProxy().getValue(ItemInfo.fromStack(target));
			if (itemEmc <= 0) {
				EMCGadgets.LOGGER.debug("useEmc: Item {} has no EMC value", target);
				return 0;
			}
			BigInteger playerEmcBefore = provider.getEmc();
			BigInteger itemEmcBig = BigInteger.valueOf(itemEmc);
			long placeableCount = playerEmcBefore.divide(itemEmcBig).longValueExact();
			if (placeableCount <= 0) {
				EMCGadgets.LOGGER.debug("useEmc: Not enough EMC. Have {}, need {}", playerEmcBefore, itemEmc);
				return 0;
			}
			// 実際に提供する数量 (要求数と購入可能数の小さい方)
			int actualAmount = (int) Math.min(amountRequired, placeableCount);
			BigInteger emcToUse = itemEmcBig.multiply(BigInteger.valueOf(actualAmount));
			BigInteger playerEmcAfter = playerEmcBefore.subtract(emcToUse);
			provider.setEmc(playerEmcAfter);

			// サーバーサイドの場合、クライアントに同期
			if (player instanceof net.minecraft.entity.player.ServerPlayerEntity) {
				provider.syncEmc((net.minecraft.entity.player.ServerPlayerEntity) player);
			}

			EMCGadgets.LOGGER.debug("useEmc: Consumed {} EMC for {} x {}. EMC: {} -> {}",
				emcToUse, actualAmount, target.getItem(), playerEmcBefore, playerEmcAfter);
			return actualAmount;
		}).orElse(0);
	}

	public static long countEmc(ItemStack itemStack, PlayerEntity player) {
		return player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).map(provider -> {
			if (!hasTablet(player.inventory.items)) {
				return 0L;
			}
			if (!provider.hasKnowledge(itemStack)) {
				return 0L;
			}
			long itemEmc = ProjectEAPI.getEMCProxy().getValue(ItemInfo.fromStack(itemStack));
			if (itemEmc <= 0) {
				return 0L;
			}
			BigInteger playerEmc = provider.getEmc();
			BigInteger itemEmcBig = BigInteger.valueOf(itemEmc);
			try {
				return playerEmc.divide(itemEmcBig).longValueExact();
			} catch (ArithmeticException e) {
				return Long.MAX_VALUE;
			}
		}).orElse(0L);
	}

	private static boolean hasTablet(NonNullList<ItemStack> inventory) {
		return inventory.stream()
				.anyMatch(s -> ModConfig.keyItems.contains(s.getItem()));
	}

	private static int longToInt(long count) {
		try {
			return Math.toIntExact(count);
		} catch (ArithmeticException e) {
			return Integer.MAX_VALUE;
		}
	}
}

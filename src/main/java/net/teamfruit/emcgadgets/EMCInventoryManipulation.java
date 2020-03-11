package net.teamfruit.emcgadgets;

import com.direwolf20.buildinggadgets.common.tools.InventoryManipulation;
import com.direwolf20.buildinggadgets.common.tools.MathTool;
import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.gameObjs.items.TransmutationTablet;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EMCInventoryManipulation {

    public static int extractFromEmc(EntityPlayer player, ItemStack target, int amountRequired) {
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
                    return MathTool.longToInt(placeableCount);
                }
            }
        }
        return 0;
    }

    public static int countInEmc(ItemStack itemStack, EntityPlayer player) {
        boolean hasTable = player.inventory.mainInventory.stream()
                .anyMatch(s -> s.getItem() instanceof TransmutationTablet);
        if (hasTable) {
            IKnowledgeProvider provider = PersonalEMC.get(player);

            if (provider.hasKnowledge(itemStack)) {
                long itemEmc = EMCHelper.getEmcValue(itemStack);
                long placeableCount = provider.getEmc() / itemEmc;
                if (placeableCount > 0)
                    return MathTool.longToInt(placeableCount);
            }
        }
        return 0;
    }
}
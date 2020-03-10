package net.teamfruit.emcgadgets;

import com.direwolf20.buildinggadgets.common.tools.InventoryManipulation;
import moze_intel.projecte.gameObjs.items.TransmutationTablet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EMCInventoryManipulation {

    public static boolean useItem(ItemStack target, EntityPlayer player, int amountRequired, World world) {

        if (player.inventory.getItemStack().getItem() instanceof TransmutationTablet) {

        }
        return true;
    }

    public static int countItem(ItemStack itemStack, EntityPlayer player, InventoryManipulation.IRemoteInventoryProvider remoteInventory) {
        if (player.inventory.getItemStack().getItem() instanceof TransmutationTablet) {

        }
        return Integer.MAX_VALUE;
    }
}
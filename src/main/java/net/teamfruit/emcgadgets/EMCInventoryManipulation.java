package net.teamfruit.emcgadgets;

import com.direwolf20.buildinggadgets.common.tools.InventoryManipulation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EMCInventoryManipulation {

    public static boolean useItem(ItemStack target, EntityPlayer player, int amountRequired, World world) {
        return true;
    }

    public static int countItem(ItemStack itemStack, EntityPlayer player, InventoryManipulation.IRemoteInventoryProvider remoteInventory) {
        return Integer.MAX_VALUE;
    }

    public static boolean usePaste(EntityPlayer player, int count) {
        return true;
    }

    public static int countPaste(EntityPlayer player) {
        return Integer.MAX_VALUE;
    }
}

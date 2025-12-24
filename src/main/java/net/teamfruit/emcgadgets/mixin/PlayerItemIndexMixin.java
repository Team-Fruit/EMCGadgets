package net.teamfruit.emcgadgets.mixin;

import com.direwolf20.buildinggadgets.common.tainted.inventory.MatchResult;
import com.direwolf20.buildinggadgets.common.tainted.inventory.PlayerItemIndex;
import com.direwolf20.buildinggadgets.common.tainted.inventory.materials.MaterialList;
import com.direwolf20.buildinggadgets.common.tainted.inventory.materials.objects.IUniqueObject;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Multiset;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.teamfruit.emcgadgets.EMCGadgets;
import net.teamfruit.emcgadgets.EMCInventoryManipulation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = PlayerItemIndex.class, remap = false)
public abstract class PlayerItemIndexMixin {

    @Shadow
    @Final
    private PlayerEntity player;

    @Shadow
    public abstract void reIndex();

    // applyMatchで使うために、tryMatchで検出したEMC補填が必要なアイテムを保存
    @Unique
    private Map<IUniqueObject<?>, Integer> emcgadgets$emcNeededItems = new HashMap<>();

    @Inject(
        method = "tryMatch",
        at = @At("RETURN"),
        cancellable = true
    )
    private void emcgadgets$tryMatchReturn(MaterialList list, CallbackInfoReturnable<MatchResult> cir) {
        MatchResult result = cir.getReturnValue();

        // 既に成功している場合は何もしない
        if (result.isSuccess()) {
            emcgadgets$emcNeededItems.clear();
            return;
        }

        ImmutableMultiset<IUniqueObject<?>> chosenOption = result.getChosenOption();
        ImmutableMultiset<IUniqueObject<?>> foundItems = result.getFoundItems();

        Map<IUniqueObject<?>, Integer> emcNeeded = new HashMap<>();
        boolean canProvideAll = true;

        for (Multiset.Entry<IUniqueObject<?>> entry : chosenOption.entrySet()) {
            IUniqueObject<?> obj = entry.getElement();
            int needed = entry.getCount();
            int found = foundItems.count(obj);
            int missing = needed - found;

            if (missing > 0) {
                ItemStack targetStack = obj.createStack();
                if (!targetStack.isEmpty()) {
                    long emcAvailable = EMCInventoryManipulation.countEmc(targetStack, player);

                    if (emcAvailable >= missing) {
                        emcNeeded.put(obj, missing);
                    } else {
                        canProvideAll = false;
                        break;
                    }
                } else {
                    canProvideAll = false;
                    break;
                }
            }
        }

        if (canProvideAll && !emcNeeded.isEmpty()) {
            emcgadgets$emcNeededItems = emcNeeded;

            // 成功結果を偽装 (実際のアイテムは追加しない)
            ImmutableMultiset.Builder<IUniqueObject<?>> newFoundBuilder = ImmutableMultiset.builder();
            newFoundBuilder.addAll(foundItems);
            for (Map.Entry<IUniqueObject<?>, Integer> e : emcNeeded.entrySet()) {
                newFoundBuilder.addCopies(e.getKey(), e.getValue());
            }

            MatchResult newResult = MatchResult.success(list, newFoundBuilder.build(), chosenOption);
            cir.setReturnValue(newResult);

            EMCGadgets.LOGGER.debug("tryMatch: Faked success, EMC will provide {} item types", emcNeeded.size());
        }
    }

    @Inject(
        method = "applyMatch",
        at = @At("HEAD")
    )
    private void emcgadgets$applyMatchHead(MatchResult result, CallbackInfoReturnable<Boolean> cir) {
        // サーバーサイドでのみ実行
        if (player.level.isClientSide()) {
            return;
        }

        if (!result.isSuccess()) {
            return;
        }

        // tryMatchで検出したEMC補填が必要なアイテムを実際に追加
        if (!emcgadgets$emcNeededItems.isEmpty()) {
            boolean addedItems = false;

            for (Map.Entry<IUniqueObject<?>, Integer> entry : emcgadgets$emcNeededItems.entrySet()) {
                IUniqueObject<?> obj = entry.getKey();
                int count = entry.getValue();
                ItemStack targetStack = obj.createStack();

                if (!targetStack.isEmpty()) {
                    int used = EMCInventoryManipulation.useEmc(targetStack, player, count);

                    if (used > 0) {
                        ItemStack itemsToAdd = targetStack.copy();
                        itemsToAdd.setCount(used);
                        player.inventory.add(itemsToAdd);
                        addedItems = true;
                        EMCGadgets.LOGGER.debug("applyMatch: Added {} x {} via EMC",
                            targetStack.getItem(), used);
                    }
                }
            }

            if (addedItems) {
                reIndex();
                EMCGadgets.LOGGER.debug("applyMatch: Re-indexed after adding EMC items");
            }

            emcgadgets$emcNeededItems.clear();
        }
    }
}

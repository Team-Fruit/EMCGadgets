package net.teamfruit.emcgadgets;

import com.google.common.base.Predicates;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_NAME)
public class ModConfig {

    @Config.Name("Key Item")
    @Config.Comment({ "This item is required when using EMC Gadget" })
    public static String[] keyItemNames = {
            "projecte:item.pe_transmutation_tablet",
            "projectex:arcane_tablet",
    };

    @Config.Ignore
    public static Set<Item> keyItems;

    public static void loadKeyItems() {
        keyItems = Arrays.stream(keyItemNames)
                .map(e -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(e)))
                .filter(Predicates.notNull())
                .collect(Collectors.toSet());
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class Handler {

        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.load(Reference.MOD_ID, Config.Type.INSTANCE);
                loadKeyItems();
            }
        }
    }
}

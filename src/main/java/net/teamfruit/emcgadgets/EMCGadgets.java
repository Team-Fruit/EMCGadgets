package net.teamfruit.emcgadgets;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EMCGadgets.MOD_ID)
public class EMCGadgets {

    public static final String MOD_ID = "emcgadgets";
    public static final Logger LOGGER = LogManager.getLogger();

    public EMCGadgets() {
        ModConfig.register();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("EMCGadgets initializing...");
        ModConfig.loadKeyItems();
    }
}

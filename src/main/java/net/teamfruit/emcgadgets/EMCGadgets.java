package net.teamfruit.emcgadgets;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(
		modid = Reference.MOD_ID,
		name = Reference.MOD_NAME,
		version = Reference.VERSION,
		dependencies = Reference.DEPENDENCIES,
		acceptableRemoteVersions = "*",
		guiFactory = "net.teamfruit.emcgadgets.gui.config.ConfigGuiFactory"
)
public class EMCGadgets {

	@Mod.Instance(Reference.MOD_ID)
	public static EMCGadgets INSTANCE;

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side) {
		return true;
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ModConfig.loadKeyItems();
	}

}

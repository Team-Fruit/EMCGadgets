package net.teamfruit.emcgadgets;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(
		modid = Reference.MOD_ID,
		name = Reference.MOD_NAME,
		version = Reference.VERSION,
		dependencies = Reference.DEPENDENCIES,
		acceptableRemoteVersions = "*"
)
public class EMCGadgets {

	@Mod.Instance(Reference.MOD_ID)
	public static EMCGadgets INSTANCE;

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side) {
		return true;
	}

}

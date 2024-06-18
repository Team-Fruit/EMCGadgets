package net.teamfruit.emcgadgets.compat;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

import javax.annotation.Nonnull;

public class CompatFMLDeobfuscatingRemapper {
	@Nonnull
	public static String mapDesc(@Nonnull final String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
	}

	@Nonnull
	public static String mapMethodDesc(@Nonnull final String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
	}

	@Nonnull
	public static String mapFieldName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(owner, name, desc);
	}

	@Nonnull
	public static String mapMethodName(@Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc) {
		return FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc);
	}

	@Nonnull
	public static String unmap(@Nonnull final String typeName) {
		return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
	}

	public static boolean useMcpNames() {
		final Boolean deobfuscated = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		return deobfuscated != null && deobfuscated;
	}
}

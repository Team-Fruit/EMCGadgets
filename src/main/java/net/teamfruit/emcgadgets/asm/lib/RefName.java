package net.teamfruit.emcgadgets.asm.lib;

import net.teamfruit.emcgadgets.compat.CompatFMLDeobfuscatingRemapper;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class RefName {
	@Nonnull
	private final String mcpName;
	@Nonnull
	private final String srgName;

	protected RefName(final @Nonnull String mcpName, final @Nonnull String srgName) {
		this.mcpName = mcpName;
		this.srgName = srgName;
	}

	@Nonnull
	public String name() {
		return CompatFMLDeobfuscatingRemapper.useMcpNames() ? Validate.notEmpty(this.mcpName) : Validate.notEmpty(this.srgName);
	}

	@Nonnull
	public String mcpName() {
		return this.mcpName;
	}

	@Nonnull
	public String srgName() {
		return this.srgName;
	}

	@Override
	@Nonnull
	public String toString() {
		return String.format("[%s,%s]", this.mcpName, this.srgName);
	}

	@Nonnull
	public static RefName deobName(final @Nonnull String mcpName, final @Nonnull String srgName) {
		return new RefName(mcpName, srgName);
	}

	@Nonnull
	public static RefName name(final @Nonnull String name) {
		return new RefName(name, name);
	}
}

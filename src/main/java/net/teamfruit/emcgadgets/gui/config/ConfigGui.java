package net.teamfruit.emcgadgets.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.teamfruit.emcgadgets.Reference;

import javax.annotation.Nullable;

public class ConfigGui extends GuiConfig {
	public ConfigGui(final @Nullable GuiScreen parent) {
		super(parent, Reference.MOD_ID, Reference.MOD_NAME);
	}
}
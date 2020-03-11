package net.teamfruit.emcgadgets.compat;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Set;

public abstract class CompatTransformer implements IClassTransformer {
	private static final Logger LOGGER = LogManager.getLogger();

	public abstract ClassNode read(@Nonnull byte[] bytes);

	public abstract byte[] write(@Nonnull ClassNode node);

	public abstract ClassNode transform(final ClassNode input, final CompatTransformerVotingContext context);

	public abstract Set<String> targetNames();

	public static class CompatTransformerVotingContext {
	}

	@Override
	public byte[] transform(final String name, final String transformedName, byte[] bytes) {
		if (bytes == null || name == null || transformedName == null)
			return bytes;

		if (targetNames().contains(transformedName))
			try {
				ClassNode node = read(bytes);

				node = transform(node, new CompatTransformerVotingContext());

				bytes = write(node);
			} catch (final Exception e) {
				LOGGER.fatal("Could not transform: ", e);
			}

		return bytes;
	}
}

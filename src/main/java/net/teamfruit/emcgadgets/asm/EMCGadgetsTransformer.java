package net.teamfruit.emcgadgets.asm;

import net.teamfruit.emcgadgets.Log;
import net.teamfruit.emcgadgets.asm.lib.ClassName;
import net.teamfruit.emcgadgets.asm.lib.INodeTransformer;
import net.teamfruit.emcgadgets.asm.lib.INodeTreeTransformer;
import net.teamfruit.emcgadgets.asm.lib.VisitorHelper;
import net.teamfruit.emcgadgets.compat.CompatTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EMCGadgetsTransformer extends CompatTransformer {
	@Override
	public ClassNode read(@Nonnull final byte[] bytes) {
		return VisitorHelper.read(bytes, ClassReader.SKIP_FRAMES);
	}

	@Override
	public byte[] write(@Nonnull final ClassNode node) {
		return VisitorHelper.write(node, ClassWriter.COMPUTE_FRAMES);
	}

	private final INodeTreeTransformer transformers[] = {
			new InventoryManipulationTransform(),
	};

	private final Set<String> transformerNames = Stream.of(this.transformers).map(INodeTransformer::getClassName).map(ClassName::getName).collect(Collectors.toSet());

	@Override
	public ClassNode transform(final ClassNode input, final CompatTransformerVotingContext context) {
		try {
			for (final INodeTreeTransformer transformer : this.transformers)
				if (transformer.getMatcher().test(input))
					return VisitorHelper.transform(input, transformer, Log.log);
		} catch (final Exception e) {
			throw new RuntimeException("Could not transform: ", e);
		}

		return input;
	}

	@Override
	public Set<String> targetNames() {
		return this.transformerNames;
	}
}
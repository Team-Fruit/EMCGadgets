package net.teamfruit.emcgadgets.asm;

import net.teamfruit.emcgadgets.asm.lib.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class InventoryManipulationTransform implements INodeTreeTransformer {
	@Override
	public ClassName getClassName() {
		return ClassName.of("com.direwolf20.buildinggadgets.common.tools.InventoryManipulation");
	}

	@Override
	public ClassNode apply(final ClassNode node) {
		final ASMValidate validator = ASMValidate.create(getSimpleName());
		validator.test("useItem");
		validator.test("countItem");

		{
			final MethodMatcher matcher = new MethodMatcher(getClassName(), DescHelper.toDescMethod(boolean.class, ClassName.of("net.minecraft.item.ItemStack"), ClassName.of("net.minecraft.entity.player.EntityPlayer"), int.class, ClassName.of("net.minecraft.world.World")), ASMDeobfNames.InventoryManipulationUseItem);
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					VisitorHelper.stream(method.instructions).filter(e -> {
						return e instanceof JumpInsnNode && e.getOpcode() == Opcodes.IF_ICMPGE;
					}).reduce((first, second) -> second).map(e -> e.getPrevious().getPrevious()).ifPresent(marker -> {
						final InsnList insertion = new InsnList();

						/*
						L5
						 LINENUMBER 133 L5
						FRAME CHOP 1
						 ILOAD 4
						 ALOAD 0
						 ALOAD 1
						 ILOAD 4
						 INVOKESTATIC net/teamfruit/emcgadgets/EMCInventoryManipulation.useEmc (Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;I)I
						 ISUB
						 ISTORE 4
						L10
						 LINENUMBER 136 L10
						 ILOAD 4
						 ILOAD 2
						 IF_ICMPGE L11
						*/

						// L3
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 4));
						insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
						insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
						insertion.add(new VarInsnNode(Opcodes.ILOAD, 4));
						insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emcgadgets.EMCInventoryManipulation").getBytecodeName(), ASMDeobfNames.EMCInventoryManipulationUseEmc.name(), DescHelper.toDescMethod(int.class, ClassName.of("net.minecraft.item.ItemStack"), ClassName.of("net.minecraft.entity.player.EntityPlayer"), int.class), false));
						insertion.add(new InsnNode(Opcodes.ISUB));
						insertion.add(new VarInsnNode(Opcodes.ISTORE, 4));

						method.instructions.insertBefore(marker, insertion);
						validator.check("useItem");
					});
				}
			});
		}

		{
			final MethodMatcher matcher = new MethodMatcher(getClassName(), DescHelper.toDescMethod(int.class, ClassName.of("net.minecraft.item.ItemStack"), ClassName.of("net.minecraft.entity.player.EntityPlayer"), ClassName.of("com.direwolf20.buildinggadgets.common.tools.InventoryManipulation$IRemoteInventoryProvider")), ASMDeobfNames.InventoryManipulationCountItem);
			node.methods.stream().filter(matcher).forEach(method -> {
				{
					VisitorHelper.stream(method.instructions).filter(e -> {
						return e instanceof VarInsnNode && e.getOpcode() == Opcodes.LSTORE && ((VarInsnNode) e).var == 3;
					}).findFirst().ifPresent(marker -> {
						final InsnList insertion = new InsnList();

						/*
						 LSTORE 3
						L3
						 LINENUMBER 219 L3
						 LLOAD 3
						 ALOAD 0
						 ALOAD 1
						 INVOKESTATIC net/teamfruit/emcgadgets/EMCInventoryManipulation.countInEmc (Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;)J
						 LADD
						 LSTORE 3
						*/

						// L3
						insertion.add(new VarInsnNode(Opcodes.LLOAD, 3));
						insertion.add(new VarInsnNode(Opcodes.ALOAD, 0));
						insertion.add(new VarInsnNode(Opcodes.ALOAD, 1));
						insertion.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ClassName.of("net.teamfruit.emcgadgets.EMCInventoryManipulation").getBytecodeName(), ASMDeobfNames.EMCInventoryManipulationCountEmc.name(), DescHelper.toDescMethod(long.class, ClassName.of("net.minecraft.item.ItemStack"), ClassName.of("net.minecraft.entity.player.EntityPlayer")), false));
						insertion.add(new InsnNode(Opcodes.LADD));
						insertion.add(new VarInsnNode(Opcodes.LSTORE, 3));

						method.instructions.insert(marker, insertion);
						validator.check("countItem");
					});
				}
			});
		}

		validator.validate();
		return node;
	}
}
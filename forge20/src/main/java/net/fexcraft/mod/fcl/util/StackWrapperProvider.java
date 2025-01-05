package net.fexcraft.mod.fcl.util;

import net.fexcraft.mod.uni.impl.SWI;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class StackWrapperProvider implements ICapabilityProvider {

	public static final Capability<StackWrapper> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	public static Class<? extends StackWrapper> IMPL = SWI.class;
	private LazyOptional<StackWrapper> optional;
	private StackWrapper wrapper;

	public StackWrapperProvider(ItemStack stack){
		wrapper = wrap(stack);
		optional = LazyOptional.of(() -> wrapper);
	}

	public static StackWrapper wrap(ItemStack stack){
		try{
			return IMPL.getConstructor(ItemStack.class).newInstance(stack);
		}
		catch(Exception e){
			e.printStackTrace();
			return new SWI(stack);
		}
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction){
		return capability == CAPABILITY ? optional.cast() : LazyOptional.empty();
	}

}

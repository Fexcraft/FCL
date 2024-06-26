package net.fexcraft.lib.mc.capabilities;

import net.fexcraft.lib.mc.capabilities.paint.Paintable;
import net.fexcraft.lib.mc.capabilities.sign.SignCapability;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLCapabilities {

	@CapabilityInject(SignCapability.class)
	public static final Capability<SignCapability> SIGN_CAPABILITY = null;
	
	@CapabilityInject(Paintable.class)
	public static final Capability<Paintable> PAINTABLE = null;

	@CapabilityInject(UniEntity.class)
	public static final Capability<UniEntity> PLAYER = null;
	
}
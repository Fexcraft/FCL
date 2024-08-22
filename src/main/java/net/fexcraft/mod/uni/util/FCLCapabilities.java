package net.fexcraft.mod.uni.util;

import net.fexcraft.lib.mc.capabilities.sign.SignCapability;
import net.fexcraft.mod.uni.UniChunk;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCLCapabilities {

	@CapabilityInject(SignCapability.class)
	public static final Capability<SignCapability> SIGN_CAPABILITY = null;

	@CapabilityInject(UniEntity.class)
	public static final Capability<UniEntity> PLAYER = null;

	@CapabilityInject(UniChunk.class)
	public static final Capability<UniChunk> CHUNK = null;
	
}
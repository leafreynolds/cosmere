/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class RosharCapability implements IRoshar
{
	//Injection
	public static final Capability<IRoshar> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	Level m_level;

	CompoundTag m_nbt = null;

	public RosharCapability(Level level)
	{
		m_level = level;
	}

	@Nonnull
	public static LazyOptional<IRoshar> get(Level level)
	{
		return level != null ? level.getCapability(RosharCapability.CAPABILITY, null)
		                     : LazyOptional.empty();
	}


	@Override
	public CompoundTag serializeNBT()
	{
		if (m_nbt == null)
		{
			m_nbt = new CompoundTag();
		}

		return m_nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		m_nbt = nbt;
	}
}

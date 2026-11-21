/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities.world;

import leaf.cosmere.surgebinding.common.registries.SurgebindingAttachments;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RosharCapability implements IRoshar
{
	Level m_level;

	CompoundTag m_nbt = null;

	public RosharCapability(Level level)
	{
		m_level = level;
	}

	@Nonnull
	public static Optional<IRoshar> get(Level level)
	{
		if (level == null || !isRoshar(level))
		{
			return Optional.empty();
		}
		return Optional.of(level.getData(SurgebindingAttachments.ROSHAR.get()));
	}

	public static boolean isRoshar(Level level)
	{
		return level != null && level.dimension().location().getPath().contains("roshar");
	}


	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		if (m_nbt == null)
		{
			m_nbt = new CompoundTag();
		}

		return m_nbt;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		m_nbt = nbt;
	}
}

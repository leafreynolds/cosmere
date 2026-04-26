package leaf.cosmere.surgebinding.common.capabilities.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class RosharCapability implements IRoshar
{
	private CompoundTag m_nbt = null;

	public RosharCapability()
	{
	}

	public static Optional<IRoshar> get(Level level)
	{
		if (level == null || !level.dimension().location().toString().contains("roshar"))
		{
			return Optional.empty();
		}
		return Optional.of(level.getData(SurgebindingAttachments.ROSHAR.get()));
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

/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class HemalurgyWorldCapability implements IHemalurgyWorldCap
{
	private final Level level;
	private CompoundTag m_nbt = null;
	private final List<CustomSpawner> customSpawners;

	public HemalurgyWorldCapability(Level level)
	{
		this.level = level;
		customSpawners = ImmutableList.of(new KolossPatrolSpawner());
	}

	@Nonnull
	public static Optional<IHemalurgyWorldCap> get(Level level)
	{
		if (level == null)
		{
			return Optional.empty();
		}
		return Optional.of(level.getData(HemalurgyAttachments.HEMALURGY_WORLD.get()));
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

	@Override
	public void tick()
	{
		if (level instanceof ServerLevel serverLevel)
		{
			final MinecraftServer server = serverLevel.getServer();
			for (CustomSpawner customSpawner : customSpawners)
			{
				customSpawner.tick(
						serverLevel,
						server.isSpawningMonsters(),
						server.isSpawningAnimals());
			}
		}
	}
}

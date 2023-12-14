/*
 * File updated ~ 30 - 7 - 2023 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.List;

public class HemalurgyWorldCapability implements IHemalurgyWorldCap
{
	//Injection
	public static final Capability<IHemalurgyWorldCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	private final Level level;
	private CompoundTag m_nbt = null;
	private final List<CustomSpawner> customSpawners;

	public HemalurgyWorldCapability(Level level)
	{
		this.level = level;
		customSpawners = ImmutableList.of(new KolossPatrolSpawner());

	}

	@Nonnull
	public static LazyOptional<IHemalurgyWorldCap> get(Level level)
	{
		return level != null ? level.getCapability(HemalurgyWorldCapability.CAPABILITY, null)
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

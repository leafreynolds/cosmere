/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.capabilities.world;

import leaf.cosmere.common.registration.impl.EntityTypeRegistryObject;
import leaf.cosmere.hemalurgy.common.entity.Koloss;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.PatrolSpawner;

/**
 * Koloss spawn patrol version of  {@link PatrolSpawner}.
 */
public class KolossPatrolSpawner implements CustomSpawner
{
	private int nextTick;

	public int tick(ServerLevel pLevel, boolean pSpawnEnemies, boolean pSpawnFriendlies)
	{
		if (!pSpawnEnemies)
		{
			return 0;
		}
		else if (!pLevel.getGameRules().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING))
		{
			return 0;
		}
		else
		{
			RandomSource randomsource = pLevel.random;
			--this.nextTick;
			if (this.nextTick > 0)
			{
				return 0;
			}
			else
			{
				this.nextTick += 12000 + randomsource.nextInt(1200);
				long i = pLevel.getDayTime() / 24000L;
				if (i >= 5L && pLevel.isDay())
				{
					if (randomsource.nextInt(5) != 0)
					{
						return 0;
					}
					else
					{
						int j = pLevel.players().size();
						if (j < 1)
						{
							return 0;
						}
						else
						{
							Player player = pLevel.players().get(randomsource.nextInt(j));
							if (player.isSpectator())
							{
								return 0;
							}
							else if (pLevel.isCloseToVillage(player.blockPosition(), 2))
							{
								return 0;
							}
							else
							{
								int k = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
								int l = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
								BlockPos.MutableBlockPos blockpos$mutableblockpos = player.blockPosition().mutable().move(k, 0, l);
								int i1 = 10;
								if (!pLevel.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10))
								{
									return 0;
								}
								else
								{
									Holder<Biome> holder = pLevel.getBiome(blockpos$mutableblockpos);
									if (holder.is(BiomeTags.WITHOUT_PATROL_SPAWNS))
									{
										return 0;
									}
									else
									{
										int j1 = 0;
										int k1 = (int) Math.ceil((double) pLevel.getCurrentDifficultyAt(blockpos$mutableblockpos).getEffectiveDifficulty()) + 1;

										for (int l1 = 0; l1 < k1; ++l1)
										{
											++j1;
											blockpos$mutableblockpos.setY(pLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockpos$mutableblockpos).getY());
											if (l1 == 0)
											{
												if (!this.spawnPatrolMember(pLevel, blockpos$mutableblockpos, randomsource, true))
												{
													break;
												}
											}
											else
											{
												this.spawnPatrolMember(pLevel, blockpos$mutableblockpos, randomsource, false);
											}

											blockpos$mutableblockpos.setX(blockpos$mutableblockpos.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
											blockpos$mutableblockpos.setZ(blockpos$mutableblockpos.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
										}

										return j1;
									}
								}
							}
						}
					}
				}
				else
				{
					return 0;
				}
			}
		}
	}

	private boolean spawnPatrolMember(ServerLevel pLevel, BlockPos pPos, RandomSource pRandom, boolean pLeader)
	{
		BlockState blockstate = pLevel.getBlockState(pPos);
		if (!NaturalSpawner.isValidEmptySpawnBlock(pLevel, pPos, blockstate, blockstate.getFluidState(), HemalurgyEntityTypes.KOLOSS_LARGE.getEntityType()))
		{
			return false;
		}
		else if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, pLevel, MobSpawnType.PATROL, pPos, pRandom))
		{
			return false;
		}
		else
		{
			final EntityTypeRegistryObject<Koloss> kolossType =
					pLeader
					? HemalurgyEntityTypes.KOLOSS_LARGE
					: pLevel.random.nextBoolean()
					  ? HemalurgyEntityTypes.KOLOSS_MEDIUM
					  : HemalurgyEntityTypes.KOLOSS_SMALL;

			PatrollingMonster patrollingmonster = kolossType.getEntityType().create(pLevel);
			if (patrollingmonster != null)
			{
				if (pLeader)
				{
					patrollingmonster.setPatrolLeader(true);
					patrollingmonster.findPatrolTarget();
				}

				patrollingmonster.setPos((double) pPos.getX(), (double) pPos.getY(), (double) pPos.getZ());
				patrollingmonster.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), MobSpawnType.PATROL, (SpawnGroupData) null, (CompoundTag) null);
				pLevel.addFreshEntityWithPassengers(patrollingmonster);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
}

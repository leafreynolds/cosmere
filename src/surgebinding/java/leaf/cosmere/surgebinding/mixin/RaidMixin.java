/*
 * File updated ~ 15 - 1 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.mixin;

import leaf.cosmere.surgebinding.common.mixinAccess.RaidMixinAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.UUID;

@Mixin(Raid.class)
public abstract class RaidMixin implements RaidMixinAccess
{
	@Shadow
	@Final
	private Set<UUID> heroesOfTheVillage;

	@Override
	public boolean _cosmere$isHero(Player player)
	{
		return heroesOfTheVillage.contains(player.getUUID());
	}
}

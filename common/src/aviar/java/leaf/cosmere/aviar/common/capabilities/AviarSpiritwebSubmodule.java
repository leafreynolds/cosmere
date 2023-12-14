/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.aviar.common.entity.AviarBird;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class AviarSpiritwebSubmodule implements ISpiritwebSubmodule
{
	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		if (spiritweb.getLiving() instanceof Player player)
		{
			final int tickOffset = Manifestations.ManifestationTypes.AVIAR.getID();
			if ((player.tickCount + tickOffset) % 20 != 0)
			{
				return;
			}

			//tick any aviar bonds from being on shoulder
			final CompoundTag shoulderEntityLeft = player.getShoulderEntityLeft();
			if (!shoulderEntityLeft.isEmpty())
			{
				tickAviar(player, shoulderEntityLeft);
			}

			final CompoundTag shoulderEntityRight = player.getShoulderEntityRight();
			if (!shoulderEntityRight.isEmpty())
			{
				tickAviar(player, shoulderEntityRight);
			}

		}
	}

	private void tickAviar(Player player, CompoundTag compoundtag)
	{
		EntityType.byString(compoundtag.getString("id"))
				.filter((entityType) -> entityType == AviarEntityTypes.AVIAR_ENTITY.get())
				.ifPresent((entityType) ->
				{
					final int variant = compoundtag.getInt("Variant");
					AviarBird.tickBond(player, variant, compoundtag.getUUID("UUID").toString());
				});
	}
}

/*
 * File updated ~ 5 - 6 - 2024 ~ Leaf
 */

package leaf.cosmere.aviar.common.items;

import leaf.cosmere.aviar.common.entity.AviarBird;
import leaf.cosmere.aviar.common.registries.AviarEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class PatjisFruitItem extends Item
{
	public PatjisFruitItem(Supplier<Item.Properties> property)
	{
		super(property.get());
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand hand)
	{
		//if parrot or aviar
		if (hand == InteractionHand.MAIN_HAND && (target instanceof Parrot parrot))
		{
			if (!(parrot instanceof AviarBird))
			{
				if (!player.level().isClientSide)
				{
					//convert parrot to aviar
					AviarBird birb = AviarEntityTypes.AVIAR_ENTITY.get().create(target.level());

					if (birb == null)
					{
						return InteractionResult.FAIL;
					}

					//make it the same kind of parrot
					birb.restoreFrom(parrot);

					birb.moveTo(birb.getX(), birb.getY(), birb.getZ(), birb.getYRot(), birb.getXRot());
					birb.setDeltaMovement(parrot.getDeltaMovement());

					parrot.remove(Entity.RemovalReason.DISCARDED);
					birb.level().addFreshEntity(birb);

					//todo replace sound
					birb.playSound(SoundEvents.PARROT_IMITATE_ENDER_DRAGON, 1.0F, 1.0F);

					for (int i = 0; i < 3; ++i)
					{
						double d0 = birb.getRandom().nextGaussian() * 0.02D;
						double d1 = birb.getRandom().nextGaussian() * 0.02D;
						double d2 = birb.getRandom().nextGaussian() * 0.02D;
						birb.level().addParticle(ParticleTypes.HEART, birb.getRandomX(1.0D), birb.getRandomY() + 0.5D, birb.getRandomZ(1.0D), d0, d1, d2);
					}
					itemStack.shrink(1);
				}
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}

		}

		return InteractionResult.PASS;
	}
}

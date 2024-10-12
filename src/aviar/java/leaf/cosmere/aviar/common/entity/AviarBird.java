/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.common.entity;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.aviar.common.config.AviarConfigs;
import leaf.cosmere.aviar.common.items.PatjisFruitItem;
import leaf.cosmere.aviar.common.registries.AviarAttributes;
import leaf.cosmere.aviar.common.registries.AviarEffects;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AviarBird extends Parrot
{
	public AviarBird(EntityType<? extends Parrot> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}


	@Override
	public boolean isFood(ItemStack pStack)
	{
		return pStack.getItem() instanceof PatjisFruitItem;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent)
	{
		AviarBird otherParent = (AviarBird) pOtherParent;
		Parrot baby = EntityType.PARROT.create(pLevel);
		if (baby != null)
		{
			baby.setVariant(this.random.nextBoolean() ? this.getVariant() : otherParent.getVariant());
		}
		return baby;
	}

	private void setOffspringAttributes(AviarBird parentOne, AgeableMob parentTwo)
	{
		//todo - if breeding stats down the line, this is where they would be set
	}

	@Override
	public void tick()
	{
		super.tick();

		//only once per tick, offloaded to be on different ticks to other mods in suite
		final int tickOffset = Manifestations.ManifestationTypes.AVIAR.getID();
		if ((this.tickCount + tickOffset) % 20 != 0)
		{
			return;
		}

		final LivingEntity owner = this.getOwner();
		final double range = AviarConfigs.SERVER.AVIAR_BONUS_RANGE.get();

		if (owner == null || this.distanceTo(owner) > range)
		{
			return;
		}

		tickBond(owner, this.getVariant(), this.getStringUUID());
	}

	public static void tickBond(LivingEntity livingEntity, Variant variant, String aviarUUID)
	{
		//if owner exists and has spiritweb
		SpiritwebCapability.get(livingEntity).ifPresent(data ->
		{
			final Attribute attribute = switch (variant)
			{
				//todo decide on finalized attributes changed
				// note: I'm unsure if these are the same as the IDs in the previous version // Gerbagel
				default -> AttributesRegistry.COGNITIVE_CONCEALMENT.get();
				case BLUE -> AttributesRegistry.COSMERE_FORTUNE.get();
				case GREEN -> AttributesRegistry.XP_RATE_ATTRIBUTE.get();
				case YELLOW_BLUE -> AttributesRegistry.DETERMINATION.get();
				case GRAY -> AviarAttributes.HOSTILE_LIFE_SENSE.get();
			};

			//todo config
			double strength = switch (variant)
			{
				default -> 10;//cognitive concealment
				case BLUE -> 1;//cosmere fortune
				case GREEN -> 1;//xp gain
				case YELLOW_BLUE -> 5;//determination
				case GRAY -> 2;//hostile life sense
			};

			final CosmereEffectInstance effectInstance = CosmereEffectInstance.getOrCreateEffect(AviarEffects.AVIAR_BOND_EFFECT.get(), data, aviarUUID, 1);
			effectInstance.setDynamicAttribute(attribute, strength, AttributeModifier.Operation.ADDITION);

			data.addEffect(effectInstance);
		});
	}
}

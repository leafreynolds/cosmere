/*
 * File updated ~ 20 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api.cosmereEffect;

import com.google.common.collect.Maps;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/*
 *   An instance knows what effect it is associated with
 *   An instance has a UUID, so that an entity can benefit from the same effect multiple times (unlike mob effects)
 *   An instance has a strength, so that the attribute increase can reflect the source it came from
 *   An instance has a timer, such that it can end
 *   An instance will clean any attribute and effect changes when the timer is finished
 */
public class CosmereEffectInstance implements ICosmereEffectProvider
{

	private CosmereEffect effect;
	//uuid is based on the uuid of the source entity's uuid that the effect is coming from, + the resource location of the power
	// ie: effectSource.getStringUUID() + effect.getRegistryName()
	private UUID uuid;
	private double strength;

	/**
	 * Time remaining on this cosmere effect in ticks
	 * There are 20 ticks in a second (generally)
	 */
	private int duration;

	private final Map<Attribute, AttributeModifierInfo> dynamicAttributeModifiers = Maps.newHashMap();

	public CosmereEffectInstance()
	{
	}

	public CosmereEffectInstance(CosmereEffect effect, UUID uuid, double strength, int duration)
	{
		this.effect = effect;
		this.uuid = uuid;
		this.strength = strength;
		this.duration = duration;
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public CosmereEffect getEffect()
	{
		return effect;
	}

	public int getDuration()
	{
		return this.duration;
	}

	public double getStrength()
	{
		return this.strength;
	}

	public Tag save(CompoundTag compoundTag)
	{
		compoundTag.putString("effect_id", getEffect().getRegistryName().toString());
		compoundTag.putUUID("uuid", uuid);
		compoundTag.putDouble("strength", strength);
		compoundTag.putInt("duration", duration);

		if (!this.dynamicAttributeModifiers.isEmpty())
		{
			ListTag listtag = new ListTag();

			for (AttributeModifierInfo info : this.dynamicAttributeModifiers.values())
			{
				listtag.add(info.save(new CompoundTag()));
			}

			compoundTag.put("dynamic_attributes", listtag);
		}

		return compoundTag;
	}

	@Nullable
	public static CosmereEffectInstance load(CompoundTag compoundtag)
	{
		final ResourceLocation effectID = new ResourceLocation(compoundtag.getString("effect_id"));

		if (CosmereAPI.cosmereEffectRegistry().containsKey(effectID))
		{
			final CosmereEffectInstance effectInstance = new CosmereEffectInstance();

			effectInstance.effect = CosmereAPI.cosmereEffectRegistry().getValue(effectID);
			effectInstance.uuid = compoundtag.getUUID("uuid");
			effectInstance.strength = compoundtag.getDouble("strength");
			effectInstance.duration = compoundtag.getInt("duration");

			if (compoundtag.contains("dynamic_attributes"))
			{
				ListTag listtag = (ListTag) compoundtag.get("dynamic_attributes");

				for (int i = 0; i < listtag.size(); ++i)
				{
					CompoundTag attributeTag = listtag.getCompound(i);
					AttributeModifierInfo ami = AttributeModifierInfo.load(attributeTag);

					if (ami != null)
					{
						effectInstance.dynamicAttributeModifiers.put(ami.getAttribute(), ami);
					}
				}
			}

			return effectInstance;
		}

		return null;
	}

	public boolean tick(ISpiritweb data)
	{
		if (this.duration > 0)
		{
			if (this.effect.isActiveTick(data))
			{
				this.effect.applyEffectTick(data, this.strength);
			}

			this.decreaseDuration();
		}

		return this.duration > 0;
	}

	private void decreaseDuration()
	{
		--this.duration;
	}

	public void applyAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap)
	{
		for (Map.Entry<Attribute, AttributeModifierInfo> entry : this.getEffect().getAttributeModifiers().entrySet())
		{
			AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
			if (attributeinstance != null)
			{
				AttributeModifierInfo attributeModifierInfo = entry.getValue();

				final UUID effectInstanceUUID = getUUID();
				attributeinstance.removeModifier(effectInstanceUUID);
				attributeinstance.addPermanentModifier(
						new AttributeModifier(
								effectInstanceUUID,
								String.format("%s - %s: %s", this.getRegistryName(), getStrength(), effectInstanceUUID.toString()),
								this.getAttributeModifierValue(getStrength(), attributeModifierInfo),
								attributeModifierInfo.getOperation()
						)
				);
			}
		}

		for (Map.Entry<Attribute, AttributeModifierInfo> entry : dynamicAttributeModifiers.entrySet())
		{
			AttributeInstance attributeinstance = pAttributeMap.getInstance(entry.getKey());
			if (attributeinstance != null)
			{
				AttributeModifierInfo attributeModifierInfo = entry.getValue();

				final UUID effectInstanceUUID = getUUID();
				attributeinstance.removeModifier(effectInstanceUUID);
				attributeinstance.addPermanentModifier(
						new AttributeModifier(
								effectInstanceUUID,
								String.format("%s - %s: %s", this.getRegistryName(), getStrength(), effectInstanceUUID.toString()),
								this.getAttributeModifierValue(getStrength(), attributeModifierInfo),
								attributeModifierInfo.getOperation()
						)
				);
			}
		}
	}

	public void removeAttributeModifiers(AttributeMap attributeMap)
	{
		//remove attribute modifiers from effect
		for (Map.Entry<Attribute, AttributeModifierInfo> entry : this.getEffect().getAttributeModifiers().entrySet())
		{
			AttributeInstance attributeinstance = attributeMap.getInstance(entry.getKey());
			if (attributeinstance != null)
			{
				attributeinstance.removeModifier(uuid);
			}
		}

		//remove dynamic attribute changes specific to this instance;
		for (Map.Entry<Attribute, AttributeModifierInfo> entry : dynamicAttributeModifiers.entrySet())
		{
			AttributeInstance attributeinstance = attributeMap.getInstance(entry.getKey());
			if (attributeinstance != null)
			{
				attributeinstance.removeModifier(uuid);
			}
		}
	}

	public double getAttributeModifierValue(double strength, AttributeModifierInfo modifier)
	{
		return modifier.getAmount() * strength;
	}

	//reuse memory where possible
	public void setDynamicAttribute(Attribute attribute, double strength, AttributeModifier.Operation operation)
	{
		AttributeModifierInfo ami;
		if (dynamicAttributeModifiers.containsKey(attribute))
		{
			ami = dynamicAttributeModifiers.get(attribute);
			ami.update(strength, operation);
		}
		else
		{
			ami = new AttributeModifierInfo(attribute, strength, operation);
		}

		dynamicAttributeModifiers.put(attribute, ami);
	}

	public void removeDynamicAttribute(Attribute attribute)
	{
		dynamicAttributeModifiers.remove(attribute);
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	private void setStrength(double strength)
	{
		this.strength = strength;
	}

	@NotNull
	public static CosmereEffectInstance getOrCreateEffect(CosmereEffect cosmereEffect, ISpiritweb data, LivingEntity sourceEntity, double strength)
	{
		return getOrCreateEffect(cosmereEffect, data, sourceEntity.getStringUUID(), strength);
	}

	public static CosmereEffectInstance getOrCreateEffect(CosmereEffect cosmereEffect, ISpiritweb data, String uuid, double strength)
	{
		final CosmereEffect effect = cosmereEffect;
		CosmereEffectInstance effectInstance = data.getEffect(EffectsHelper.getEffectUUID(effect, uuid));

		//if no effect with this UUID was found, we know we need to make one
		if (effectInstance == null)
		{
			effectInstance = EffectsHelper.getNewEffect(effect, data.getLiving(), strength);
		}
		else
		{
			//remove the current instance of this effect's attributes from the data
			data.removeEffect(effectInstance.getUUID());
			//reset the duration
			effectInstance.setDuration(93);
			effectInstance.setStrength(strength);
		}
		return effectInstance;
	}

	public Map<Attribute, AttributeModifierInfo> getDynamicModifiers()
	{
		return dynamicAttributeModifiers;
	}
}

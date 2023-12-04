/*
 * File updated ~ 8 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.api.cosmereEffect;

import com.google.common.collect.Maps;
import leaf.cosmere.api.providers.ICosmereEffectProvider;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

/*
 *   How we want effects to work:
 *		- An effect can have a unique source on an instance basis (player, allomantic grenade etc), rather than only allowing one instance of an effect at a time. This would let us stack effects, ie multiple people using copper clouds to prevent seekers finding a group.
 *		- An effect may have a ticking effect (like how regeneration works?)
 *		- An effect needs a timer, so we can set for how long it lasts for (it can be renewed as often as needed)
 *		- An effect should be able to have any number of attribute changes, and should clean up after itself when finished.
 *		- We don't want it to show up in jade tooltips
 *		- An effect should have a strength associated with it, so it knows how much to scale up the attribute changes, or be available for use with the ticking effect
 *
 *	Because of that, we can't use the vanilla mob effects at all
 *   Even specially separating it away from the list on living entities. Those effects will never allow a situation like above where I want
 *   players to be able to use multiple copper clouds together. Effects only let attributes benefit once per type of effect
 *
 */
public abstract class CosmereEffect implements ICosmereEffectProvider
{

	//each effect cannot have multiple modifications to the same attribute, but could theoretically modify all attributes
	private final Map<Attribute, AttributeModifierInfo> attributeModifiers = Maps.newHashMap();

	protected CosmereEffect()
	{
	}

	public Map<Attribute, AttributeModifierInfo> getAttributeModifiers()
	{
		return this.attributeModifiers;
	}

	public CosmereEffect addAttributeModifier(Attribute attribute, double amount, AttributeModifier.Operation operation)
	{
		AttributeModifierInfo attributeModifier = new AttributeModifierInfo(attribute, amount, operation);
		this.attributeModifiers.put(attribute, attributeModifier);
		return this;
	}

	protected int getTickToCheck(ISpiritweb data)
	{
		return data.getLiving().tickCount + this.getTickOffset();
	}

	protected int getTickOffset()
	{
		//offset the tick check
		//ie entity tick count + this offset = tick to check against active tick
		return 0;
	}

	protected int getActiveTick()
	{
		//every 20 ticks, or 1 second
		return 20;
	}

	protected boolean isActiveTick(ISpiritweb data)
	{
		return getTickToCheck(data) % getActiveTick() == 0;
	}


	public void applyEffectTick(ISpiritweb data, double strength)
	{
	}

	@Override
	public CosmereEffect getEffect()
	{
		return this;
	}

	@Override
	public String getTranslationKey()
	{
		ResourceLocation regName = getRegistryName();
		return "effect." + regName.getNamespace() + "." + regName.getPath();
	}
}

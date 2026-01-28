package leaf.cosmere.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;

public interface IGrantsBaseAttributes
{
	ArrayList<Attribute> determineBaseAttributes(ItemStack itemStack);

	void grantBaseAttributes(LivingEntity livingEntity, ArrayList<Attribute> attributes, int strength);
}

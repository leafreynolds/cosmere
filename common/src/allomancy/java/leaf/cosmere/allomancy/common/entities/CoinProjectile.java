/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.common.entities;

import leaf.cosmere.allomancy.common.registries.AllomancyEntityTypes;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoinProjectile extends AbstractArrow implements ItemSupplier
{
	private ItemStack projectileStack = ItemsRegistry.METAL_NUGGETS.get(Metals.MetalType.COPPER).get().getDefaultInstance();

	public CoinProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack)
	{
		super(AllomancyEntityTypes.COIN_PROJECTILE.get(), livingEntity, level);
		this.projectileStack = itemStack.copy();
	}

	public CoinProjectile(EntityType<CoinProjectile> coinProjectileEntityType, Level level)
	{
		super(coinProjectileEntityType, level);
	}

	@Override
	protected ItemStack getPickupItem()
	{
		return projectileStack;
	}

	@Override
	public ItemStack getItem()
	{
		return projectileStack;
	}
}

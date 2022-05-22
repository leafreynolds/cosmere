/*
 * File created ~ 21 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.entities;

import leaf.cosmere.registry.EntityRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CoinProjectile extends AbstractArrow implements ItemSupplier
{
	private ItemStack projectileStack = ItemsRegistry.COPPER_CLIP.get().getDefaultInstance();

	public CoinProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack)
	{
		super(EntityRegistry.COIN_PROJECTILE.get(), livingEntity, level);
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

/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.entities;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registry.ItemsRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryBlocksRegistry;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEntityTypes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SandProjectile extends AbstractArrow implements ItemSupplier
{
    private ItemStack projectileStack = SandmasteryBlocksRegistry.TALDAIN_SAND_LAYER.getItemStack();

    public SandProjectile(Level level, LivingEntity livingEntity, ItemStack itemStack)
    {
        super(SandmasteryEntityTypes.SAND_PROJECTILE.get(), livingEntity, level);
        this.projectileStack = itemStack.copy();
    }

    public SandProjectile(EntityType<SandProjectile> sandProjectileEntityType, Level level)
    {
        super(sandProjectileEntityType, level);
    }

    @Override
    protected ItemStack getPickupItem()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getItem()
    {
        return projectileStack;
    }
}

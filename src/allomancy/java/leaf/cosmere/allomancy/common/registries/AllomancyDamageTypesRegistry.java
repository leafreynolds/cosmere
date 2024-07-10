package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.common.Cosmere;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class AllomancyDamageTypesRegistry
{
	public static final ResourceKey<DamageType> PEWTER_DELAYED_DAMAGE = createKey("pewter_delayed_damage");

	// special thanks to the aether mod team for inspiration
	private static ResourceKey<DamageType> createKey(String name)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Cosmere.MODID, name));
	}

	public static DamageSource damageSource(Level level, ResourceKey<DamageType> key)
	{
		return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
	}
}

package leaf.cosmere.registry;

import leaf.cosmere.entities.CoinProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, leaf.cosmere.Cosmere.MODID);

	/*public static final RegistryObject<EntityType<SprenFlameEntity>> SPREN_FIRE =
			ENTITIES.register(
					"spren_flame",
					() -> EntityType.Builder
							.of(SprenFlameEntity::new, MobCategory.CREATURE)
							.sized(.5f, .5f)
							.setShouldReceiveVelocityUpdates(false)
							.build("spren_flame"));*/

	public static final RegistryObject<EntityType<CoinProjectile>> COIN_PROJECTILE =
			ENTITIES.register(
					"coin_projectile",
					() -> EntityType.Builder
							.<CoinProjectile>of(CoinProjectile::new, MobCategory.MISC)
							.sized(0.5F, 0.5F)
							.clientTrackingRange(4)
							.updateInterval(20)
							.build("coin_projectile"));


	public static void PrepareEntityAttributes()
	{
		//DefaultAttributes.put(SPREN_FIRE.get(), SprenFlameEntity.prepareAttributes().build());
	}
}

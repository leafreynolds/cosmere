package leaf.cosmere.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static leaf.cosmere.Cosmere.MODID;


public class EntityRegistry
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

	/*public static final RegistryObject<EntityType<SprenFlameEntity>> SPREN_FIRE =
			ENTITIES.register(
					"spren_flame",
					() -> EntityType.Builder
							.of(SprenFlameEntity::new, MobCategory.CREATURE)
							.sized(.5f, .5f)
							.setShouldReceiveVelocityUpdates(false)
							.build("spren_flame"));*/


	public static void PrepareEntityAttributes()
	{
		//DefaultAttributes.put(SPREN_FIRE.get(), SprenFlameEntity.prepareAttributes().build());
	}
}

package leaf.cosmere.registry;

import leaf.cosmere.entities.spren.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;

import static leaf.cosmere.Cosmere.MODID;

public class EntityRegistry
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

	public static final RegistryObject<EntityType<SprenFlameEntity>> SPREN_FIRE =
			ENTITIES.register(
					"spren_flame",
					() -> EntityType.Builder
							.create(SprenFlameEntity::new, EntityClassification.CREATURE)
							.size(.5f, .5f)
							.setShouldReceiveVelocityUpdates(false)
							.build("spren_flame"));


	public static void PrepareEntityAttributes()
	{
		GlobalEntityTypeAttributes.put(SPREN_FIRE.get(), SprenFlameEntity.prepareAttributes().create());
	}
}

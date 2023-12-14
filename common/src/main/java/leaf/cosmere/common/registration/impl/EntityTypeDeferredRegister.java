package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;

public class EntityTypeDeferredRegister extends WrappedDeferredRegister<EntityType<?>>
{

	private Map<EntityTypeRegistryObject<? extends LivingEntity>, Supplier<Builder>> livingEntityAttributes = new HashMap<>();

	private final List<IEntityTypeProvider> allEntityTypes = new ArrayList<>();

	public EntityTypeDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.ENTITY_TYPES);
	}

	public <ENTITY extends LivingEntity> EntityTypeRegistryObject<ENTITY> register(String name, EntityType.Builder<ENTITY> builder, Supplier<Builder> attributes)
	{
		EntityTypeRegistryObject<ENTITY> entityTypeRO = register(name, builder);
		livingEntityAttributes.put(entityTypeRO, attributes);
		return entityTypeRO;
	}

	public <ENTITY extends Entity> EntityTypeRegistryObject<ENTITY> register(String name, EntityType.Builder<ENTITY> builder)
	{
		final EntityTypeRegistryObject<ENTITY> registryObject = register(name, () -> builder.build(name), EntityTypeRegistryObject::new);
		allEntityTypes.add(registryObject);
		return registryObject;
	}

	@Override
	public void register(IEventBus bus)
	{
		super.register(bus);
		bus.addListener(this::registerEntityAttributes);
	}

	private void registerEntityAttributes(EntityAttributeCreationEvent event)
	{
		if (livingEntityAttributes == null)
		{
			CosmereAPI.logger.error("GlobalEntityTypeAttributes have already been set. This should not happen.");
		}
		else
		{
			//Register our living entity attributes
			for (Map.Entry<EntityTypeRegistryObject<? extends LivingEntity>, Supplier<Builder>> entry : livingEntityAttributes.entrySet())
			{
				event.put(entry.getKey().get(), entry.getValue().get().build());
			}
			//And set the map to null to allow it to be garbage collected
			livingEntityAttributes = null;
		}
	}


	public List<IEntityTypeProvider> getAllEntityTypes()
	{
		return Collections.unmodifiableList(allEntityTypes);
	}
}
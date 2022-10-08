package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IMobEffectProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MobEffectDeferredRegister extends WrappedDeferredRegister<MobEffect>
{
	private final List<IMobEffectProvider> allMobEffects = new ArrayList<>();

	public MobEffectDeferredRegister(String modid)
	{
		super(modid, ForgeRegistries.MOB_EFFECTS);
	}

	public <MOB_EFFECTS extends MobEffect> MobEffectRegistryObject<MOB_EFFECTS> register(String name, Supplier<? extends MOB_EFFECTS> sup)
	{
		MobEffectRegistryObject<MOB_EFFECTS> registeredItem = register(name, sup, MobEffectRegistryObject::new);
		allMobEffects.add(registeredItem);
		return registeredItem;
	}


	public List<IMobEffectProvider> getAllMobEffects()
	{
		return Collections.unmodifiableList(allMobEffects);
	}
}

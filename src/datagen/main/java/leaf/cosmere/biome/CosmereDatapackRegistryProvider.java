/*
 * File updated ~ 11 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.biome;


import leaf.cosmere.BaseDatapackRegistryProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.CosmereDamageTypesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class CosmereDatapackRegistryProvider extends BaseDatapackRegistryProvider
{
	public CosmereDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(output, lookupProvider, BUILDER, Cosmere.MODID);
	}


	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()

			.add(Registries.DAMAGE_TYPE, context ->
			{
				for (CosmereDamageTypesRegistry.CosmereDamageType damageType : CosmereDamageTypesRegistry.DAMAGE_TYPES.values())
				{
					context.register(damageType.key(), new DamageType(damageType.getMsgId(), damageType.exhaustion()));
				}
			});
}
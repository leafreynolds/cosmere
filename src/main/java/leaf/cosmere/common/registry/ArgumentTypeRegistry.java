package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.commands.arguments.AllomancyArgumentType;
import leaf.cosmere.common.commands.arguments.FeruchemyArgumentType;
import leaf.cosmere.common.commands.arguments.ManifestationsArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ArgumentTypeRegistry
{
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_INFOS = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, Cosmere.MODID);

	public static final RegistryObject<ArgumentTypeInfo<?, ?>> MANIFESTATION_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("manifestation_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
							ManifestationsArgumentType.class,
							SingletonArgumentInfo.contextFree(ManifestationsArgumentType::createArgument)));

	public static final RegistryObject<ArgumentTypeInfo<?, ?>> ALLOMANCY_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("allomancy_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
									AllomancyArgumentType.class,
									SingletonArgumentInfo.contextFree(AllomancyArgumentType::createArgument)));

	public static final RegistryObject<ArgumentTypeInfo<?, ?>> FERUCHEMY_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("feruchemy_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
									FeruchemyArgumentType.class,
									SingletonArgumentInfo.contextFree(FeruchemyArgumentType::createArgument)));
}

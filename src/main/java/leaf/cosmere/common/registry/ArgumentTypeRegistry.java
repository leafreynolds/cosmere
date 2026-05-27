package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.commands.arguments.AllomancyArgumentType;
import leaf.cosmere.common.commands.arguments.FeruchemyArgumentType;
import leaf.cosmere.common.commands.arguments.ManifestationsArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ArgumentTypeRegistry
{
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPE_INFOS = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, Cosmere.MODID);

	public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> MANIFESTATION_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("manifestation_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
							ManifestationsArgumentType.class,
							SingletonArgumentInfo.contextFree(ManifestationsArgumentType::createArgument)));

	public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> ALLOMANCY_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("allomancy_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
									AllomancyArgumentType.class,
									SingletonArgumentInfo.contextFree(AllomancyArgumentType::createArgument)));

	public static final DeferredHolder<ArgumentTypeInfo<?, ?>, ArgumentTypeInfo<?, ?>> FERUCHEMY_ARGUMENT_TYPE = ARGUMENT_TYPE_INFOS.register("feruchemy_argument_type",
			() -> ArgumentTypeInfos.registerByClass(
									FeruchemyArgumentType.class,
									SingletonArgumentInfo.contextFree(FeruchemyArgumentType::createArgument)));
}

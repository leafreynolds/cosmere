/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to correctly add new commands, among other things!
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.cosmere.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.commands.arguments.ManifestationsArgumentType;
import leaf.cosmere.common.commands.subcommands.CosmereEffectCommand;
import leaf.cosmere.common.commands.subcommands.EyeCommand;
import leaf.cosmere.common.commands.subcommands.ManifestationCommand;
import leaf.cosmere.common.commands.subcommands.SummonCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;


public class CosmereCommand
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(EyeCommand.register(dispatcher))
				.then(ManifestationCommand.register(dispatcher))
				.then(SummonCommand.register(dispatcher))
				.then(CosmereEffectCommand.register(dispatcher))
		);
	}

	public static void registerCustomArgumentTypes()
	{
		ArgumentTypeInfos.registerByClass(
				ManifestationsArgumentType.class,
				SingletonArgumentInfo.contextFree(ManifestationsArgumentType::createArgument));
	}
}

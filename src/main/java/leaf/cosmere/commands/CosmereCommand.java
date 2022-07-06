/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to correctly add new commands, among other things!
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.cosmere.commands;

import com.mojang.brigadier.CommandDispatcher;
import leaf.cosmere.Cosmere;
import leaf.cosmere.commands.arguments.ManifestationsArgumentType;
import leaf.cosmere.commands.subcommands.EyeCommand;
import leaf.cosmere.commands.subcommands.ManifestationCommand;
import leaf.cosmere.commands.subcommands.SummonCommand;
import leaf.cosmere.commands.subcommands.TestCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;


public class CosmereCommand
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				//.then(TestCommand.register(dispatcher))
				.then(EyeCommand.register(dispatcher))
				.then(ManifestationCommand.register(dispatcher))
				.then(SummonCommand.register(dispatcher))
		);
	}
	public static void registerCustomArgumentTypes() {
		ArgumentTypes.register("cosmere:manifestations_argument", ManifestationsArgumentType.class, new EmptyArgumentSerializer<>(ManifestationsArgumentType::createArgument));
	}
}

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
import leaf.cosmere.common.commands.subcommands.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


public class CosmereCommand
{

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal(Cosmere.MODID)
				.then(EyeCommand.register(dispatcher))
				.then(ManifestationCommand.register(dispatcher))
				.then(SummonCommand.register(dispatcher))
				.then(CosmereEffectCommand.register(dispatcher))
				.then(ChooseMetalbornPowersCommand.register(dispatcher))
		);
	}
}

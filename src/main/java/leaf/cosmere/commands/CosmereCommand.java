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
import leaf.cosmere.commands.subcommands.TestCommand;
import leaf.cosmere.commands.subcommands.ManifestationCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;


public class CosmereCommand
{

    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(Commands.literal(Cosmere.MODID)
                .then(TestCommand.register(dispatcher))
                .then(ManifestationCommand.register(dispatcher))
        );
    }
}

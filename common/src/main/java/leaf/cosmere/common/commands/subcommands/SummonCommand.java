/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.commands.subcommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.commands.arguments.ManifestationsArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;

public class SummonCommand extends ModCommand
{
	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		return SINGLE_SUCCESS;
	}

	public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		return Commands.literal("summon_metalborn")
				.requires(context -> context.hasPermission(2))
				//.executes(SummonCommand::spawnEntity)
				.then(Commands.argument("manifestation", ManifestationsArgumentType.createArgument())
						.executes(SummonCommand::spawnEntity))
				; // end add
	}

	private static int spawnEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		Manifestation manifestation = null;

		try
		{
			manifestation = context.getArgument("manifestation", Manifestation.class);
		}
		catch (Exception ignored)
		{
		}

		//todo allow not setting a specific manifestation
/*		if (manifestation == null)
		{
			boolean isAllomancy = MathHelper.randomBool();
			int powerID = MathHelper.randomInt(0, 15);
			final Metals.MetalType metalType = Metals.MetalType.valueOf(powerID).get();
			manifestation = isAllomancy ? ManifestationRegistry.ALLOMANCY_POWERS.get(metalType).get()
			                            : FeruchemyManifestations.FERUCHEMY_POWERS.get(metalType).get();
		}*/
		ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
		Villager entity = new Villager(EntityType.VILLAGER, serverPlayer.level);
		entity.moveTo(serverPlayer.position());
		context.getSource().getLevel().addFreshEntity(entity);

		final Manifestation finalManifestation = manifestation;
		SpiritwebCapability.get(entity).ifPresent((spiritweb) ->
		{
			spiritweb.giveManifestation(finalManifestation, 10);
			spiritweb.syncToClients(null);
		});

		return SINGLE_SUCCESS;
	}
}
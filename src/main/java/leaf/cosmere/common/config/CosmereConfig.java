/*
 * File updated ~ 7 - 5 - 2022 ~ Leaf
 */

package leaf.cosmere.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CosmereConfig
{
	public static Common COMMON;
	public static ForgeConfigSpec COMMON_SPEC;
	public static Client CLIENT;
	public static ForgeConfigSpec CLIENT_SPEC;

	static
	{
		Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();

		Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specClientPair.getRight();
		CLIENT = specClientPair.getLeft();
	}


	public static class Client
	{
		public final ForgeConfigSpec.BooleanValue clientConfigTest;

		Client(ForgeConfigSpec.Builder builder)
		{
			builder.comment("Client Cosmere Settings").push("client");
			clientConfigTest = builder.comment("clientConfigTest").translation("config.cosmere.clientconfigtest").define("clientconfigtest", true);
			builder.pop();
		}

	}

	public static class Common
	{
		public final ForgeConfigSpec.IntValue commonConfigTest;

		Common(ForgeConfigSpec.Builder builder)
		{
			builder.comment("General Cosmere Settings").push("common");
			commonConfigTest = builder.comment("commonConfigTest.").translation("config.cosmere.commonconfigtest").defineInRange("commonconfigtest", 5, 0, Integer.MAX_VALUE);
			builder.pop();

		}
	}

}

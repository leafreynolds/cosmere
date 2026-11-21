// This file is deleted in the AI-generated branch, assuming we no longer need it either; keeping in case of future reference necessity
///*
// * File updated ~ 7 - 6 - 2023 ~ Leaf
// */
//
//package leaf.cosmere.common.config;
//
//import com.electronwill.nightconfig.core.file.CommentedFileConfig;
//import leaf.cosmere.common.Cosmere;
//import net.neoforged.fml.ModContainer;
//import net.neoforged.fml.config.ConfigFileTypeHandler;
//import net.neoforged.fml.config.ModConfig;
//import net.neoforged.fml.loading.FMLPaths;
//
//import java.nio.file.Path;
//import java.util.function.Function;
//
//
//public class CosmereModConfig extends ModConfig
//{
//
//	private static final CosmereConfigFileTypeHandler COSMERE_TOML = new CosmereConfigFileTypeHandler();
//
//	private final ICosmereConfig cosmereConfig;
//
//	public CosmereModConfig(ModContainer container, ICosmereConfig config)
//	{
//		//registers the passed in config and sets it up in the cosmere folder.
//		super(config.getConfigType(), config.getConfigSpec(), container, Cosmere.MODID + "/" + config.getFileName() + ".toml");
//		this.cosmereConfig = config;
//	}
//
//	@Override
//	public ConfigFileTypeHandler getHandler()
//	{
//		return COSMERE_TOML;
//	}
//
//	public void clearCache()
//	{
//		cosmereConfig.clearCache();
//	}
//
//	private static class CosmereConfigFileTypeHandler extends ConfigFileTypeHandler
//	{
//
//		private static Path getPath(Path configBasePath)
//		{
//			//TY Mekanism for showing how to keep configs in the normal config folder
//			if (configBasePath.endsWith("serverconfig"))
//			{
//				return FMLPaths.CONFIGDIR.get();
//			}
//			return configBasePath;
//		}
//
//		@Override
//		public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath)
//		{
//			return super.reader(getPath(configBasePath));
//		}
//
//		@Override
//		public void unload(Path configBasePath, ModConfig config)
//		{
//			super.unload(getPath(configBasePath), config);
//		}
//	}
//}

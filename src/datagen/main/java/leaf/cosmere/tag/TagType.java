/*
 * File updated ~ 1 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.tag;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

//Based off of Mekanism's implementation of TagType<TYPE>, obtained 1st June, 2023
public record TagType<TYPE>(String name, NonNullSupplier<Either<IForgeRegistry<TYPE>, Registry<TYPE>>> registry)
{

	public static final TagType<Item> ITEM = new TagType<>("Item", () -> Either.left(ForgeRegistries.ITEMS));
	public static final TagType<Block> BLOCK = new TagType<>("Block", () -> Either.left(ForgeRegistries.BLOCKS));
	public static final TagType<EntityType<?>> ENTITY_TYPE = new TagType<>("Entity Type", () -> Either.left(ForgeRegistries.ENTITY_TYPES));
	public static final TagType<Fluid> FLUID = new TagType<>("Fluid", () -> Either.left(ForgeRegistries.FLUIDS));
	public static final TagType<BlockEntityType<?>> BLOCK_ENTITY_TYPE = new TagType<>("Block Entity Type", () -> Either.left(ForgeRegistries.BLOCK_ENTITY_TYPES));
	public static final TagType<GameEvent> GAME_EVENT = new TagType<>("Game Event", () -> Either.right(Registry.GAME_EVENT));

	public Either<IForgeRegistry<TYPE>, Registry<TYPE>> getRegistry()
	{
		return registry.get();
	}
}
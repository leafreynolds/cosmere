/*
 * File updated ~ 8 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tag;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

//Special thanks to Mekanism for showing how this works
//Based off of IntrinsicHolderTagsProvider.IntrinsicTagAppender but with a few shortcuts for forge registry entries and also a few more helpers and addition of SafeVarargs annotations
public class IntrinsicCosmereTagBuilder<TYPE> extends CosmereTagBuilder<TYPE, IntrinsicCosmereTagBuilder<TYPE>>
{

	private final Function<TYPE, ResourceKey<TYPE>> keyExtractor;

	public IntrinsicCosmereTagBuilder(Function<TYPE, ResourceKey<TYPE>> keyExtractor, TagBuilder builder, String modID)
	{
		super(builder, modID);
		this.keyExtractor = keyExtractor;
	}

	@SafeVarargs
	public final IntrinsicCosmereTagBuilder<TYPE> add(Supplier<TYPE>... elements)
	{
		return addTyped(Supplier::get, elements);
	}

	private ResourceLocation getKey(TYPE element)
	{
		return keyExtractor.apply(element).location();
	}

	@SafeVarargs
	public final IntrinsicCosmereTagBuilder<TYPE> add(TYPE... elements)
	{
		return add(this::getKey, elements);
	}

	@SafeVarargs
	public final <T> IntrinsicCosmereTagBuilder<TYPE> addTyped(Function<T, TYPE> converter, T... elements)
	{
		return add(converter.andThen(this::getKey), elements);
	}

	@SafeVarargs
	public final IntrinsicCosmereTagBuilder<TYPE> addOptional(TYPE... elements)
	{
		return addOptional(this::getKey, elements);
	}

	@SafeVarargs
	public final IntrinsicCosmereTagBuilder<TYPE> remove(TYPE... elements)
	{
		return remove(this::getKey, elements);
	}
}
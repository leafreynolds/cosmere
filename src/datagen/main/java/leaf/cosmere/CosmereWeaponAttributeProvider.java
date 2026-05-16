package leaf.cosmere;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class CosmereWeaponAttributeProvider implements DataProvider
{

	protected final PackOutput output;
	protected final String modId;

	public CosmereWeaponAttributeProvider(PackOutput output, String modId)
	{
		this.output = output;
		this.modId = modId;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		List<CompletableFuture<?>> futures = new ArrayList<>();
		registerWeaponAttributes((name, parent) -> futures.add(saveWeaponAttribute(cache, name, parent)));
		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	/**
	 * Override this in your mod-specific subclass to register weapon attributes.
	 * Call consumer.accept(name, parent) for each item.
	 */
	protected abstract void registerWeaponAttributes(BiConsumer<String, String> consumer);

	private CompletableFuture<?> saveWeaponAttribute(CachedOutput cache, String name, String parent)
	{
		JsonObject json = new JsonObject();
		json.addProperty("parent", parent);

		Path path = output.getOutputFolder(PackOutput.Target.DATA_PACK)
				.resolve(modId)
				.resolve("weapon_attributes")
				.resolve(name + ".json");

		return DataProvider.saveStable(cache, json, path);
	}

	@Override
	public String getName()
	{
		return "Weapon Attributes: " + modId;
	}
}

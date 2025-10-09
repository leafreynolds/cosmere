package leaf.cosmere.api;

import leaf.cosmere.api.manifestation.Manifestation;
import net.minecraft.client.KeyMapping;

public class Activator
{

        public KeyMapping keyMapping;
        public Manifestation manifest;
        public String name;
        public String category;

	public Activator(KeyMapping key, Manifestation manifestation)
	{
            keyMapping = key;
            manifest = manifestation;

	}

	public void setCategory(String category)
	{
            this.category = category;
            name = manifest.getName();

        }

	public KeyMapping getKeyMapping()
	{
		return keyMapping;
	}
    public Manifestation getManifestation() {return manifest;}

    public String getCategory() {return category;}

	public String getKeyName()
	{
		return "key.cosmere." + getCategory() + "." + name;
	}
	public String translator()
	{
		return name + "_" + category;
	}
}

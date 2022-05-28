/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.client.gui.SpriteIconPositioning;
import leaf.cosmere.manifestation.AManifestation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.HashMap;

public class ClientHelper
{
	public static ClientHelper instance = new ClientHelper();
	public static TextureAtlasSprite arrowUp;
	public static TextureAtlasSprite arrowDown;
	public static TextureAtlasSprite on;
	public static TextureAtlasSprite off;

	public static TextureAtlasSprite allomancy;
	public static TextureAtlasSprite feruchemy;
	public static TextureAtlasSprite surgebinding;
	private final HashMap<AManifestation, SpriteIconPositioning> manifestationSprites = new HashMap<>();
	public static TextureAtlasSprite blank;
	public static SpriteIconPositioning blankSIP;

	public void setIconForManifestation(AManifestation manifestation, SpriteIconPositioning sip)
	{
		manifestationSprites.put(manifestation, sip);
	}

	public SpriteIconPositioning getIconForManifestation(AManifestation manifestation)
	{
		if (!manifestationSprites.containsKey(manifestation))
		{
			return blankSIP;
		}
		return manifestationSprites.get(manifestation);
	}
}

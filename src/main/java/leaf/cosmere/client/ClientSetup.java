/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import leaf.cosmere.Cosmere;
import leaf.cosmere.client.gui.SpriteIconPositioning;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.utils.helpers.LogHelper;
import leaf.cosmere.utils.helpers.ResourceLocationHelper;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{


    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event)
    {
        event.enqueueWork(() ->
        {
        });

        //special thank you to @Random on the forge discord who told me that you have to tell the block it has transparency
        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (!metalType.hasOre())
                continue;

            RenderType cutoutMipped = RenderType.getCutoutMipped();
            RenderTypeLookup.setRenderLayer(metalType.getOreBlock(), cutoutMipped);
        }

        LogHelper.info("Client setup complete!");
    }

    //special thank you to the chisels and bits team who have an example of how to to register other sprites
    @SubscribeEvent
    public static void registerIconTextures(TextureStitchEvent.Pre event)
    {
        final AtlasTexture map = event.getMap();
        if (!map.getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE))
        {
            return;
        }

        event.addSprite(ResourceLocationHelper.prefix("icon/blank"));
        event.addSprite(ResourceLocationHelper.prefix("icon/arrow_up"));
        event.addSprite(ResourceLocationHelper.prefix("icon/arrow_down"));
        event.addSprite(ResourceLocationHelper.prefix("icon/on"));
        event.addSprite(ResourceLocationHelper.prefix("icon/off"));

        for (final Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (!metalType.hasAssociatedManifestation())
                continue;

            String metalToLower = metalType.toString().toLowerCase(Locale.ROOT);
            event.addSprite(ResourceLocationHelper.prefix("icon/allomancy/" + metalToLower));
            event.addSprite(ResourceLocationHelper.prefix("icon/feruchemy/" + metalToLower));
        }
    }

    //special thank you to the chisels and bits team who have an example of how to to register other sprites
    @SubscribeEvent
    public static void retrieveRegisteredIconSprites(final TextureStitchEvent.Post event)
    {
        final AtlasTexture map = event.getMap();
        if (!map.getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE))
        {
            return;
        }

        ClientHelper.blank =        map.getSprite(ResourceLocationHelper.prefix("icon/blank"));
        ClientHelper.arrowUp =      map.getSprite(ResourceLocationHelper.prefix("icon/arrow_up"));
        ClientHelper.arrowDown =    map.getSprite(ResourceLocationHelper.prefix("icon/arrow_down"));
        ClientHelper.on =           map.getSprite(ResourceLocationHelper.prefix("icon/on"));
        ClientHelper.off =          map.getSprite(ResourceLocationHelper.prefix("icon/off"));

        ClientHelper.blankSIP =     getSIP(map,"blank");

        for (final Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (!metalType.hasAssociatedManifestation())
                continue;

            //get allomantic version
            AManifestation allo = ManifestationRegistry.ALLOMANCY_POWERS.get(metalType).get();
            AManifestation feru = ManifestationRegistry.FERUCHEMY_POWERS.get(metalType).get();

            ClientHelper.instance.setIconForManifestation(allo, getSIP(map, "allomancy/" + metalType.toString()));
            ClientHelper.instance.setIconForManifestation(feru, getSIP(map, "feruchemy/" + metalType.toString()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static SpriteIconPositioning getSIP(final AtlasTexture map, final String path)
    {
        final SpriteIconPositioning sip = new SpriteIconPositioning();

        final ResourceLocation sprite = new ResourceLocation("cosmere", "icon/" + path.toLowerCase(Locale.ROOT));
        final ResourceLocation png = new ResourceLocation("cosmere", "textures/icon/" + path.toLowerCase(Locale.ROOT) + ".png");

        sip.sprite = map.getSprite(sprite);

        try
        {
            final IResource iresource = Minecraft.getInstance().getResourceManager().getResource(png);
            final BufferedImage bi = TextureUtils.readBufferedImage(iresource.getInputStream());

            int bottom = 0;
            int right = 0;
            sip.left = bi.getWidth();
            sip.top = bi.getHeight();

            for (int x = 0; x < bi.getWidth(); x++)
            {
                for (int y = 0; y < bi.getHeight(); y++)
                {
                    final int color = bi.getRGB(x, y);
                    final int a = color >> 24 & 0xff;
                    if (a > 0)
                    {
                        sip.left = Math.min(sip.left, x);
                        right = Math.max(right, x);

                        sip.top = Math.min(sip.top, y);
                        bottom = Math.max(bottom, y);
                    }
                }
            }

            sip.height = bottom - sip.top + 1;
            sip.width = right - sip.left + 1;

            sip.left /= bi.getWidth();
            sip.width /= bi.getWidth();
            sip.top /= bi.getHeight();
            sip.height /= bi.getHeight();
        } catch (final IOException e)
        {
            sip.height = 1;
            sip.width = 1;
            sip.left = 0;
            sip.top = 0;
        }

        return sip;
    }

}

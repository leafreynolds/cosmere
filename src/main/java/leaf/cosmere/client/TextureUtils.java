/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@OnlyIn(Dist.CLIENT)
public final class TextureUtils
{

    private TextureUtils()
    {
        throw new IllegalStateException("Tried to initialize: TextureUtils but this is a Utility class.");
    }


    //special thank you to the chisels and bits team who have an example of how to to register other sprites
    public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException
    {
        BufferedImage bufferedimage;

        try
        {
            bufferedimage = ImageIO.read(imageStream);
        }
        finally
        {
            IOUtils.closeQuietly(imageStream);
        }

        return bufferedimage;
    }

}

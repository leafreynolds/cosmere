/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Chisels and Bits team for their example of rendering a dynamic menu based on given elements!
 * https://github.com/ChiselsAndBits/Chisels-and-Bits
 *
 * At the moment it's just showing all manifestation powers that a user has,
 * but eventually will be subclassed to show menus by power type instead.
 *
 */

package leaf.cosmere.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.client.ClientHelper;
import leaf.cosmere.helpers.MathHelper;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.ChangeManifestationModeMessage;
import leaf.cosmere.network.packets.DeactivateCurrentManifestationsMessage;
import leaf.cosmere.network.packets.SetSelectedManifestationMessage;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static leaf.cosmere.registry.KeybindingRegistry.MANIFESTATION_MENU;

public class SpiritwebMenu extends Screen
{

    private final float TIME_SCALE = 0.01f;
    public static final SpiritwebMenu instance = new SpiritwebMenu();
    private SpiritwebCapability spiritweb = null;

    private float visibility = 0.0f;
    private boolean canShow = true;
    private Stopwatch lastChange = Stopwatch.createStarted();
    public AManifestation switchToPower = null;
    public ButtonAction doAction = null;
    public boolean actionUsed = false;

    protected ArrayList<RadialButtonRegion> radialButtonRegions = new ArrayList<>();
    protected ArrayList<SidedMenuButton> sidedMenuButtons = new ArrayList<>();

    protected SpiritwebMenu()
    {
        super(new StringTextComponent("Menu"));
    }

    public boolean raiseVisibility()
    {
        if (!canShow())
        {
            return false;
        }

        visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
        lastChange = Stopwatch.createStarted();
        return true;
    }

    public void decreaseVisibility()
    {
        setCanShow(true);
        visibility = MathHelper.clamp01(visibility - lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
        lastChange = Stopwatch.createStarted();
    }

    public boolean canShow()
    {
        return canShow;
    }

    public void setCanShow(final boolean canShow)
    {
        this.canShow = canShow;
    }

    public boolean isVisible()
    {
        return visibility > 0.001;
    }

    public void setScaledResolution( final int scaledWidth, final int scaledHeight)
    {
        width = scaledWidth;
        height = scaledHeight;
    }

    @Override
    public Minecraft getMinecraft()
    {
        return Minecraft.getInstance();
    }


    public void postRender(RenderGameOverlayEvent.Post event, SpiritwebCapability spiritweb)
    {
        this.spiritweb = spiritweb;
        final boolean wasVisible = isVisible();

        if (!MANIFESTATION_MENU.isInvalid() && MANIFESTATION_MENU.isKeyDown())
        {
            actionUsed = false;
            if (raiseVisibility())
            {
                getMinecraft().mouseHelper.ungrabMouse();
            }
        }
        else
        {
            if (!actionUsed)
            {
                if (switchToPower != null)
                {
                    //todo open menu sound
                    //ClientSide.instance.playRadialMenu();

                    Network.sendToServer(new SetSelectedManifestationMessage(switchToPower));
                }

                if (doAction != null)
                {
                    //todo open menu sound
                    //ClientSide.instance.playRadialMenu();
                    switch (doAction)
                    {
/*                               case ROLL_X:
                            PacketRotateVoxelBlob pri = new PacketRotateVoxelBlob(Direction.Axis.X, Rotation.CLOCKWISE_90);
                            ChiselsAndBits.getNetworkChannel().sendToServer(pri);
                            break;*/

                        case INACTIVE:
                            //deactivate current
                            if (spiritweb.selectedManifestationActive())
                            {
                                Network.sendToServer(new DeactivateCurrentManifestationsMessage());
                            }
                            break;
                        case ACTIVE:
                            //activate current
                            if (!spiritweb.selectedManifestationActive())
                            {
                                //Network.sendToServer(new ToggleManifestationMessage());
                            }
                            break;
                        case MODE_INCREASE:
                            //change mode to positive
                            Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), 1));
                            break;
                        case MODE_DECREASE:
                            //change mode to negative.
                            Network.sendToServer(new ChangeManifestationModeMessage(spiritweb.manifestation().getManifestationType(), spiritweb.manifestation().getPowerID(), -1));
                            break;
                    }
                }
            }

            actionUsed = true;
            decreaseVisibility();
        }

        if (isVisible())
        {
            final MainWindow window = event.getWindow();
            init(Minecraft.getInstance(), window.getScaledWidth(), window.getScaledHeight());
            setScaledResolution(window.getScaledWidth(), window.getScaledHeight());

            if (!wasVisible)
            {
                getMinecraft().currentScreen = SpiritwebMenu.instance;
                getMinecraft().mouseHelper.ungrabMouse();
            }

            if (getMinecraft().mouseHelper.isMouseGrabbed())
            {
                KeyBinding.unPressAllKeys();
            }
        }
        else
        {
            if (wasVisible)
            {
                getMinecraft().mouseHelper.grabMouse();
            }
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
    {
        this.visibility = 0f;
        canShow = false;
        this.minecraft.displayGuiScreen(null);

        if (this.minecraft.currentScreen == null)
        {
            this.minecraft.setGameFocused(true);
            this.minecraft.getSoundHandler().resume();
            this.minecraft.mouseHelper.grabMouse();
        }
        return true;
    }
    private static class SidedMenuButton
    {

        public double x1, x2;
        public double y1, y2;
        public boolean highlighted;

        public final ButtonAction action;
        public TextureAtlasSprite icon;
        public int color;
        public String name;
        public Direction textSide;

        public SidedMenuButton(
                final String name,
                final ButtonAction action,
                final double x,
                final double y,
                final TextureAtlasSprite ico,
                final Direction textSide)
        {
            this.name = name;
            this.action = action;
            x1 = x;
            x2 = x + 18;
            y1 = y;
            y2 = y + 18;
            icon = ico;
            color = 0xffffff;
            this.textSide = textSide;
        }

        public SidedMenuButton(
                final String name,
                final ButtonAction action,
                final double x,
                final double y,
                final int col,
                final Direction textSide)
        {
            this.name = name;
            this.action = action;
            x1 = x;
            x2 = x + 18;
            y1 = y;
            y2 = y + 18;
            color = col;
            this.textSide = textSide;
        }

    }

    static class RadialButtonRegion
    {

        public final AManifestation manifestation;
        public double x1, x2;
        public double y1, y2;
        public boolean highlighted;

        public RadialButtonRegion(final AManifestation manifestation)
        {
            this.manifestation = manifestation;
        }

    }

    protected void SetupButtons(double text_distance)
    {

        //todo buttons
        //todo button icons
        sidedMenuButtons.add(spiritweb.selectedManifestationActive()
                             ? new SidedMenuButton("gui.cosmere.other.inactive", ButtonAction.INACTIVE, -text_distance - 20, -50, ClientHelper.off, Direction.WEST)
                             : new SidedMenuButton("gui.cosmere.other.active", ButtonAction.ACTIVE, -text_distance - 20, -30, ClientHelper.on, Direction.WEST));

        sidedMenuButtons.add(new SidedMenuButton("gui.cosmere.mode.increase", ButtonAction.MODE_INCREASE, text_distance, -10, ClientHelper.arrowUp, Direction.EAST));
        sidedMenuButtons.add(new SidedMenuButton("gui.cosmere.mode.decrease", ButtonAction.MODE_DECREASE, text_distance, 10, ClientHelper.arrowDown, Direction.EAST));


        for (AManifestation manifestation : spiritweb.getAvailableManifestations())
        {
            radialButtonRegions.add(new RadialButtonRegion(manifestation));
        }
    }
    @Override
    public void render(final MatrixStack matrixStack, final int mouseX, final int mouseY, final float partialTicks)
    {
        if (spiritweb == null)
        {
            return;
        }

        matrixStack.push();

        final int start = (int) (visibility * 98) << 24;
        final int end = (int) (visibility * 128) << 24;

        fillGradient(matrixStack, 0, 0, width, height, start, end);

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        final double mouseVecX = mouseX - width / 2f;
        final double mouseVecY = (mouseY - height / 2f);
        double radians = Math.atan2(mouseVecY, mouseVecX);

        final double ring_inner_edge = 20;
        final double ring_outer_edge = 50;
        final double text_distance = 65;
        final double quarterCircle = Math.PI / 2.0;

        if (radians < -quarterCircle)
        {
            radians = radians + Math.PI * 2;
        }


        final double middle_x = width / 2f;
        final double middle_y = height / 2f;

        radialButtonRegions.clear();
        sidedMenuButtons.clear();

        SetupButtons(text_distance);

        switchToPower = null;
        doAction = null;

        if (!radialButtonRegions.isEmpty())
        {
            // todo test if I can get down to one button only
            final int totalButtons = Math.max(3, radialButtonRegions.size());
            int currentRegion = 0;
            final double fragment = Math.PI * 0.005;
            final double fragment2 = Math.PI * 0.0025;
            final double perObject = 2.0 * Math.PI / totalButtons;

            for (final RadialButtonRegion region : radialButtonRegions)
            {
                final double begin_rad = currentRegion * perObject - quarterCircle;
                final double end_rad = (currentRegion + 1) * perObject - quarterCircle;

                region.x1 = Math.cos(begin_rad);
                region.x2 = Math.cos(end_rad);
                region.y1 = Math.sin(begin_rad);
                region.y2 = Math.sin(end_rad);

                final double x1m1 = Math.cos(begin_rad + fragment) * ring_inner_edge;
                final double x2m1 = Math.cos(end_rad - fragment) * ring_inner_edge;
                final double y1m1 = Math.sin(begin_rad + fragment) * ring_inner_edge;
                final double y2m1 = Math.sin(end_rad - fragment) * ring_inner_edge;

                final double x1m2 = Math.cos(begin_rad + fragment2) * ring_outer_edge;
                final double x2m2 = Math.cos(end_rad - fragment2) * ring_outer_edge;
                final double y1m2 = Math.sin(begin_rad + fragment2) * ring_outer_edge;
                final double y2m2 = Math.sin(end_rad - fragment2) * ring_outer_edge;

                final float a = 0.5f;
                float f = 0f;

                final boolean showHighlight =
                        MathHelper.inTriangle(
                                x1m1, y1m1,
                                x2m2, y2m2,
                                x2m1, y2m1,
                                mouseVecX, mouseVecY)
                        || MathHelper.inTriangle(
                                x1m1, y1m1,
                                x1m2, y1m2,
                                x2m2, y2m2,
                                mouseVecX, mouseVecY);

                //if mouse is within the region, as defined by the two triangles
                if (begin_rad <= radians && radians <= end_rad && showHighlight)
                {
                    f = 1;
                    region.highlighted = true;
                    switchToPower = region.manifestation;
                }

                //draw first triangle
                buffer.pos(middle_x + x1m1, middle_y + y1m1, 0).color(f, f, f, a).endVertex();
                buffer.pos(middle_x + x2m1, middle_y + y2m1, 0).color(f, f, f, a).endVertex();
                //draw second triangle
                buffer.pos(middle_x + x2m2, middle_y + y2m2, 0).color(f, f, f, a).endVertex();
                buffer.pos(middle_x + x1m2, middle_y + y1m2, 0).color(f, f, f, a).endVertex();

                currentRegion++;
            }
        }

        for (final SidedMenuButton button : sidedMenuButtons)
        {
            final float a = 0.5f;
            float f = 0f;

            if (button.x1 <= mouseVecX && button.x2 >= mouseVecX && button.y1 <= mouseVecY && button.y2 >= mouseVecY)
            {
                f = 1;
                button.highlighted = true;
                doAction = button.action;
            }

            //set first triangle
            buffer.pos(middle_x + button.x1, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
            buffer.pos(middle_x + button.x1, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
            //set second triangle
            buffer.pos(middle_x + button.x2, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
            buffer.pos(middle_x + button.x2, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
        }

        //draw out what we've asked for
        tessellator.draw();

        //then we switch to icons
        RenderSystem.shadeModel(GL11.GL_FLAT);

        RenderSystem.enableTexture();
        RenderSystem.color4f(1, 1, 1, 1.0f);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.bindTexture(Minecraft.getInstance().getTextureManager().getTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getGlTextureId());

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        //put the icons on the region buttons
        for (final RadialButtonRegion mnuRgn : radialButtonRegions)
        {
            final double x = (mnuRgn.x1 + mnuRgn.x2) * 0.5 * (ring_outer_edge * 0.6 + 0.4 * ring_inner_edge);
            final double y = (mnuRgn.y1 + mnuRgn.y2) * 0.5 * (ring_outer_edge * 0.6 + 0.4 * ring_inner_edge);

            final SpriteIconPositioning sip = ClientHelper.instance.getIconForManifestation(mnuRgn.manifestation);

            final double scalex = 15 * sip.width * 0.5;
            final double scaley = 15 * sip.height * 0.5;
            final double x1 = x - scalex;
            final double x2 = x + scalex;
            final double y1 = y - scaley;
            final double y2 = y + scaley;

            final TextureAtlasSprite sprite = sip.sprite;

            final float f = 1.0f;
            final float a = 1.0f;

            final double u1 = sip.left * 16.0;
            final double u2 = (sip.left + sip.width) * 16.0;
            final double v1 = sip.top * 16.0;
            final double v2 = (sip.top + sip.height) * 16.0;

            buffer.pos(middle_x + x1, middle_y + y1, 0).tex(sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1)).color(f, f, f, a).endVertex();
            buffer.pos(middle_x + x1, middle_y + y2, 0).tex(sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v2)).color(f, f, f, a).endVertex();
            buffer.pos(middle_x + x2, middle_y + y2, 0).tex(sprite.getInterpolatedU(u2), sprite.getInterpolatedV(v2)).color(f, f, f, a).endVertex();
            buffer.pos(middle_x + x2, middle_y + y1, 0).tex(sprite.getInterpolatedU(u2), sprite.getInterpolatedV(v1)).color(f, f, f, a).endVertex();
        }

        //put the icons on the sided buttons
        for (final SidedMenuButton button : sidedMenuButtons)
        {
            final float f = switchToPower == null ? 1.0f : 0.5f;
            final float a = 1.0f;

            final double u1 = 0;
            final double u2 = 16;
            final double v1 = 0;
            final double v2 = 16;

            final TextureAtlasSprite sprite = button.icon == null ? ClientHelper.blank : button.icon;

            final double btnx1 = button.x1 + 1;
            final double btnx2 = button.x2 - 1;
            final double btny1 = button.y1 + 1;
            final double btny2 = button.y2 - 1;

            final float red = f * ((button.color >> 16 & 0xff) / 255.0f);
            final float green = f * ((button.color >> 8 & 0xff) / 255.0f);
            final float blue = f * ((button.color & 0xff) / 255.0f);

            buffer.pos(middle_x + btnx1, middle_y + btny1, 0).tex(sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v1)).color(red, green, blue, a).endVertex();
            buffer.pos(middle_x + btnx1, middle_y + btny2, 0).tex(sprite.getInterpolatedU(u1), sprite.getInterpolatedV(v2)).color(red, green, blue, a).endVertex();
            buffer.pos(middle_x + btnx2, middle_y + btny2, 0).tex(sprite.getInterpolatedU(u2), sprite.getInterpolatedV(v2)).color(red, green, blue, a).endVertex();
            buffer.pos(middle_x + btnx2, middle_y + btny1, 0).tex(sprite.getInterpolatedU(u2), sprite.getInterpolatedV(v1)).color(red, green, blue, a).endVertex();
        }

        tessellator.draw();

        // draw radial button strings
        for (final RadialButtonRegion button : radialButtonRegions)
        {
            //but only if that button is highlighted
            if (button.highlighted)
            {
                final double x = (button.x1 + button.x2) * 0.5;
                final double y = (button.y1 + button.y2) * 0.5;

                int fixed_x = (int) (x * text_distance);
                final int fixed_y = (int) (y * text_distance);

                //todo localisation check
                final String text = I18n.format(button.manifestation.translation().getKey());

                if (x <= -0.2)
                {
                    fixed_x -= font.getStringWidth(text);
                }
                else if (-0.2 <= x && x <= 0.2)
                {
                    fixed_x -= font.getStringWidth(text) / 2;
                }

                font.drawStringWithShadow(matrixStack, text, (int) middle_x + fixed_x, (int) middle_y + fixed_y, 0xffffffff);
            }
        }

        //draw sided button strings
        for (final SidedMenuButton sideButton : sidedMenuButtons)
        {
            //but only if that sided button is highlighted
            if (sideButton.highlighted)
            {
                final String text = I18n.format(sideButton.name);

                switch (sideButton.textSide)
                {
                    case WEST:
                        font.drawStringWithShadow(matrixStack, text, (int) (middle_x + sideButton.x1 - 8) - font.getStringWidth(text), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
                        break;
                    case EAST:
                        font.drawStringWithShadow(matrixStack, text, (int) (middle_x + sideButton.x2 + 8), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
                        break;
                    case UP:
                        font.drawStringWithShadow(matrixStack, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.getStringWidth(text) * 0.5), (int) (middle_y + sideButton.y1 - 14), 0xffffffff);
                        break;
                    case DOWN:
                        font.drawStringWithShadow(matrixStack, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.getStringWidth(text) * 0.5), (int) (middle_y + sideButton.y1 + 24), 0xffffffff);
                        break;
                }

            }
        }

        matrixStack.pop();
    }



}
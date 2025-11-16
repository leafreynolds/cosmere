/*
 * File updated ~ 10 - 2 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.client.gui;

import com.google.common.base.Stopwatch;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import leaf.cosmere.api.*;
import leaf.cosmere.api.MenuHelpers.MenuButton;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.math.MathHelper;
import leaf.cosmere.api.math.Vector2;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.client.gui.ButtonAction;
import leaf.cosmere.client.gui.SpiritwebMenu;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.ItemChargeHelper;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.client.gui.guiitems.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NicrosilMenu extends Screen
{
	final double TEXT_DISTANCE = 30;

	public static final NicrosilMenu instance = new NicrosilMenu();
	private SpiritwebCapability spiritweb = null;


	private boolean closed = true;
	private float visibility = 0.0f;
	private Stopwatch lastChange = Stopwatch.createStarted();
	public Manifestation selectedManifestation = null;
	public SidedMenuButton doAction = null;
	private Manifestations.ManifestationTypes selectedPowerType = Manifestations.ManifestationTypes.ALLOMANCY;

	private TransferredPower heldButton = null;

	protected SpiritwebMenuContainer spiritwebContainer = null;

	ArrayList<ItemStack> metalminds = new ArrayList<ItemStack>();
	protected ArrayList<MetalmindMenuContainer> metalmindContainers = new ArrayList<MetalmindMenuContainer>();

	protected ArrayList<NicroSpiritwebPowerButton> spiritwebPowerButtons = new ArrayList<>();

	protected ArrayList<HemalurgyPowerButton> hemalurgyPowerButtons = new ArrayList<>();
	protected ArrayList<SidedMenuButton> sidedMenuButtons = new ArrayList<>();
	protected ArrayList<MetalQuadrant> metalQuadrants = new ArrayList<>();


	private final List<String> m_infoText = new ArrayList<>();

	protected NicrosilMenu()
	{
		super(Component.literal("Menu"));
		this.minecraft = getMinecraft();
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	public void raiseVisibility()
	{
		final float TIME_SCALE = 0.01f;
		visibility = MathHelper.clamp01(visibility + lastChange.elapsed(TimeUnit.MILLISECONDS) * TIME_SCALE);
		lastChange = Stopwatch.createStarted();
	}

	public void setScaledResolution(final int scaledWidth, final int scaledHeight)
	{
		width = scaledWidth;
		height = scaledHeight;
	}

	@Override
	public Minecraft getMinecraft()
	{
		return Minecraft.getInstance();
	}


	public void postRender()
	{
		if (this.minecraft.screen == NicrosilMenu.instance)
		{
			if (this.closed)
			{
				final Window window = this.minecraft.getWindow();
				init(this.minecraft, window.getGuiScaledWidth(), window.getGuiScaledHeight());
				setScaledResolution(window.getGuiScaledWidth(), window.getGuiScaledHeight());

				this.spiritweb = SpiritwebMenu.instance.getSpiritweb();

				//no need to set if it's already open
				//this.minecraft.setScreen(NicrosilMenu.instance);
				visibility = 0;
				lastChange = Stopwatch.createStarted();

				selectedManifestation = this.spiritweb.getSelectedManifestation();

				SetupButtons();
				this.closed = false;
			}

			raiseVisibility();
		}
	}

	@Override
	public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers)
	{
		if (Keybindings.MANIFESTATION_MENU.matches(pKeyCode, pScanCode))
		{

			//CloseScreen();
		}
		return super.keyReleased(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button)
	{
		if (heldButton != null)
		{
			heldButton = null;
		}

		for (MenuButton spcontbutton : spiritwebContainer.menuButtons)
		{
			if (spcontbutton.highlight)
			{
				//todo implement heldbutton
			}
		}

		for (NicroSpiritwebPowerButton spiritwebPowerButton : spiritwebPowerButtons)
		{
			if (spiritwebPowerButton.highlighted)
			{
				/*
				if (button == 0)
				{
					Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(radialMenuButton.manifestation, 1));
				}
				else
				{
					Cosmere.packetHandler().sendToServer(new ChangeManifestationModeMessage(radialMenuButton.manifestation, -1));
				}
				return true;
				 */

				heldButton = new TransferredPower(spiritwebPowerButton, spiritwebPowerButton.strength);

			}
		}
		for (SidedMenuButton sidedMenuButton : sidedMenuButtons)
		{


			if (sidedMenuButton.highlighted)
			{
				if (heldButton == null)
				{
					if (sidedMenuButton.powerType != -1)
					{
						selectedPowerType = Manifestations.ManifestationTypes.valueOf(doAction.powerType).get();
						SetupButtons();
					}
					else if (sidedMenuButton.action != null)
					{
						//do other action
					}
				}
				else
				{
					heldButton = null;
				}

				return true;
			}


		}
		for(MenuButton menuButton : spiritwebContainer.menuButtons)
		{
			if(menuButton.highlight)
			{

			}
		}

		return true;
	}

	@Override
	public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta)
	{

		//1 = drop down


		//-1 = pick up


		System.out.println(pDelta);
		return true;
	}

	public void CloseScreen()
	{
		this.closed = true;
		this.minecraft.setScreen(null);
	}


	public static List<ItemStack> getHemalurgyItems(Player player)
	{
		if (player == null)
		{
			return Collections.emptyList();
		}


		List<ItemStack> curioChargeables = ItemChargeHelper.getChargeCurios(player);

		List<ItemStack> toReturn = new ArrayList<>(curioChargeables.size());

		for (ItemStack stack : curioChargeables)
		{
			if (!stack.isEmpty() && StackNBTHelper.verifyExistance(stack, "hemalurgy"))
			{
				toReturn.add(stack);
			}
		}

		return toReturn;
	}


	private static class SidedMenuButton
	{

		public double x1, x2;
		public double y1, y2;
		public boolean highlighted;

		public final ButtonAction action;
		public final int powerType;
		public int color;
		public String name;
		public Direction textSide;

		public SidedMenuButton(
				final String name,
				final ButtonAction action,
				final double x,
				final double y,
				final Direction textSide)
		{
			this.name = name;
			this.action = action;
			this.powerType = -1;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			color = 0xffffff;
			this.textSide = textSide;
		}

		public SidedMenuButton(
				final String name,
				final int powerType,
				final double x,
				final double y,
				final Direction textSide)
		{
			this.name = name;
			this.action = null;
			this.powerType = powerType;
			x1 = x;
			x2 = x + 18;
			y1 = y;
			y2 = y + 18;
			color = 0xffffff;
			this.textSide = textSide;
		}
	}

	static class PowerButton
	{
		public double centerX;
		public double centerY;
		public boolean highlighted;
		public double strength;
		//Way to define spiritweb powers from hemalurgy powers

	}

	static class NicroSpiritwebPowerButton extends PowerButton
	{

		public final Manifestation manifestation;

		public NicroSpiritwebPowerButton(final Manifestation manifestation)
		{
			this.manifestation = manifestation;
			this.strength = manifestation.getStrength(instance.spiritweb, true);

		}

	}


	//Class representing the held power
	class TransferredPower
	{
		public PowerButton powerButton;
		public double strength;

		public TransferredPower(NicroSpiritwebPowerButton powerButton, double initialAmount)
		{
			this.powerButton = powerButton;
			this.strength = initialAmount;
		}

		public void increaseAmount()
		{
			double strengthRemaining = powerButton.strength - strength;

			if (strengthRemaining - 1 >= 0)
			{
				this.strength += 1;
			}
			else
			{
				this.strength += strengthRemaining;
			}
		}

		//returns if it should let go of the button
		public boolean decreaseAmount()
		{
			if (strength - 1 > 0)
			{
				this.strength -= 1;
				return false;
			}
			else
			{
				strength = 0;
				return true;
			}
		}

		public boolean allTaken()
		{
			return powerButton.strength - strength <= 0;
		}

	}

	static class MetalQuadrant
	{
		public final Metals.MetalType metalType;
		public double centerX;
		public double centerY;
		public static double width = 50;
		public static double height = 40;

		public MetalQuadrant(final Metals.MetalType metalType, double centerX, double centerY)
		{
			this.metalType = metalType;
			this.centerX = centerX;
			this.centerY = centerY;
		}

	}

	protected void SetupButtons()
	{
		spiritwebPowerButtons.clear();
		sidedMenuButtons.clear();
		//metalQuadrants.clear();

		if (spiritwebContainer == null)
		{
			spiritwebContainer = new SpiritwebMenuContainer(width / 2f, 100, 1, 1);
		}

		spiritwebContainer.clearButtons();

		metalmindContainers.clear();


		//todo add buttons to container


		List<ItemStack> hemalurgySpikes = null;

		if (spiritweb.getLiving() instanceof Player player)
		{
			hemalurgySpikes = getHemalurgyItems(player);

			for (ItemStack hema : hemalurgySpikes)
			{

				StringBuilder asd = new StringBuilder();

				String itemname = hema.getItem().getName(hema).getString().toLowerCase().replace(" ", "_");

				System.out.println(itemname);
				System.out.println(StackNBTHelper.get(hema, "hemalurgy").toString());


				//{
				// "allomancy:aluminum":5.0d,
				// "allomancy:atium":5.0d,
				// "allomancy:bendalloy":5.0d,
				// "allomancy:brass":5.0d,
				// "allomancy:bronze":5.0d,
				// "allomancy:cadmium":5.0d,
				// "allomancy:chromium":5.0d,
				// "allomancy:copper":5.0d,
				// "allomancy:duralumin":5.0d,
				// "allomancy:electrum":5.0d,
				// "allomancy:gold":5.0d,
				// "allomancy:iron":5.0d,
				// "allomancy:nicrosil":5.0d,
				// "allomancy:pewter":5.0d,
				// "allomancy:steel":5.0d,
				// "allomancy:tin":5.0d,
				// "allomancy:zinc":5.0d,
				// "aondor:aondor":5.0d,"aviar:aviar":5.0d,"awakening:awakening":5.0d,"cosmere:none":5.0d,"cosmeretools:tools":5.0d,"example:example":5.0d,"feruchemy:aluminum":5.0d,"feruchemy:atium":5.0d,"feruchemy:bendalloy":5.0d,"feruchemy:brass":5.0d,"feruchemy:bronze":5.0d,"feruchemy:cadmium":5.0d,"feruchemy:chromium":5.0d,"feruchemy:copper":5.0d,"feruchemy:duralumin":5.0d,"feruchemy:electrum":5.0d,"feruchemy:gold":5.0d,"feruchemy:iron":5.0d,"feruchemy:nicrosil":5.0d,"feruchemy:pewter":5.0d,"feruchemy:steel":5.0d,"feruchemy:tin":5.0d,"feruchemy:zinc":5.0d,"sandmastery:cushion":5.0d,"sandmastery:elevate":5.0d,"sandmastery:launch":5.0d,"sandmastery:platform":5.0d,"sandmastery:projectile":5.0d,"soulforgery:soulforgery":5.0d}

				//For icon-making

				//translation{key='item.hemalurgy.aluminum_spike', args=[]}_spike

				//Pewter Spike_spike
				//translation{key='item.hemalurgy.pewter_spike', args=[]}_spike
				//class net.minecraft.network.chat.MutableComponent_spike
			}

			metalminds.clear();
			metalminds.addAll(ItemChargeHelper.getChargeItems(player));
			metalminds.addAll(ItemChargeHelper.getChargeCurios(player));

			for (ItemStack stack : metalminds)
			{
				if (stack.getItem() instanceof ChargeableMetalCurioItem chargeable)
				{
					if (chargeable.getMetalType() == Metals.MetalType.NICROSIL)
					{
						metalmindContainers.add(new MetalmindMenuContainer(200, 200, 2, 4, stack));
					}
				}
			}

		}


		final List<Manifestation> availableManifestations = spiritweb.getAvailableManifestations();

		if (availableManifestations.size() <= 16)
		{
			if (hemalurgySpikes != null)
			{
				for (ItemStack stack : hemalurgySpikes)
				{
					if (stack.getItem() instanceof ChargeableMetalCurioItem metalCurioItem)
					{

					}
				}

			}

			for (Manifestation manifestation : availableManifestations)
			{

				if (manifestation.getStrength(spiritweb, true) > 0)
				{
					spiritwebPowerButtons.add(new NicroSpiritwebPowerButton(manifestation));
					spiritwebContainer.addButton(new SpiritwebPowerButton(100, 100, manifestation, spiritweb));
				}
			}
		}
		else
		{
			Set<Manifestations.ManifestationTypes> foundPowerTypes = new HashSet<>();

			for (Manifestation manifestation : availableManifestations)
			{

				if (manifestation.getManifestationType() == selectedPowerType)
				{
					if (manifestation.getStrength(spiritweb, true) > 0)
					{
						spiritwebPowerButtons.add(new NicroSpiritwebPowerButton(manifestation));
						spiritwebContainer.addButton(new SpiritwebPowerButton(100, 100, manifestation, spiritweb));
					}
				}
				foundPowerTypes.add(manifestation.getManifestationType());
			}

			for (Manifestations.ManifestationTypes foundPowerType : foundPowerTypes)
			{
				final int index = foundPowerType.getID() - 1;
				final double v = TEXT_DISTANCE * index;
				sidedMenuButtons.add(
						new SidedMenuButton(
								foundPowerType.getName(),
								foundPowerType.getID(),
								v - ((TEXT_DISTANCE * foundPowerTypes.size()) / 2) + 5,
								-90,
								Direction.UP)
				);

			}
		}


	}

	@Override
	public void render(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTicks)
	{
		PoseStack matrixStack = guiGraphics.pose();
		if (spiritweb == null)
		{
			return;
		}

		matrixStack.pushPose();

		final int start = (int) (visibility * 98) << 24;
		final int end = (int) (visibility * 128) << 24;

		guiGraphics.fillGradient(0, 0, width, height, start, end);

		//RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		final Tesselator tessellator = Tesselator.getInstance();
		final BufferBuilder buffer = tessellator.getBuilder();

		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

		final double mouseVecX = mouseX - width / 2f;
		final double mouseVecY = (mouseY - height / 2f);

		final double middle_x = width / 2f;
		final double middle_y = height / 2f;

		selectedManifestation = null;
		doAction = null;

		//render the button backgrounds

		//spiritwebContainer.setPosition(middle_x, 100);

		//spiritwebContainer.highlightButtons(mouseX, mouseY);
		//spiritwebContainer.setPosition(middle_x, 100);
		spiritwebContainer.renderContainer(buffer, mouseVecX, mouseVecY, middle_x, middle_y);

		//newButton.renderButton(buffer);

		renderSpiritwebButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);
		renderSidedButtons(buffer, mouseVecX, mouseVecY, middle_x, middle_y);


		//render the metal quadrant backgrounds
		//renderMetalQuadrants(buffer);

		//draw out what we've asked for
		tessellator.end();

		drawIcons(guiGraphics, buffer, middle_x, middle_y);


		// draw radial button strings
		//renderRadialButtonStrings(guiGraphics, (int) middle_x, (int) middle_y);
		//draw sided button strings
		//renderSidedButtonStrings(guiGraphics, middle_x, middle_y);
		//draw quadrant strings
		//renderMetalQuadrantsStrings(guiGraphics);
		//do extra text info stuff
		//renderAnyExtraInfoTexts(guiGraphics, (int) middle_x, (int) middle_y);

		matrixStack.popPose();
	}

	private void drawIcons(@NotNull GuiGraphics guiGraphics, BufferBuilder buffer, double middle_x, double middle_y)
	{
		PoseStack matrixStack = guiGraphics.pose();
		matrixStack.pushPose();
		//RenderSystem.enableTexture();
		RenderSystem.enableBlend();

		//then we switch to icons
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		//RenderSystem.bindTexture(Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).getId());
		//buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

		//put the icons on the region buttons
		renderRadialButtonIcons(guiGraphics, middle_x, middle_y);
		//put the icons on the sided buttons
		renderSidedButtonIcons(guiGraphics, middle_x, middle_y);

		matrixStack.popPose();
	}

	//Extra Info

	private void renderAnyExtraInfoTexts(GuiGraphics guiGraphics, int middle_x, int middle_y)
	{
		int leftSideX = 10;
		final int[] y = {(int) middle_y / 2};

		if (selectedPowerType == Manifestations.ManifestationTypes.SANDMASTERY)
		{
			m_infoText.clear();

			for (ISpiritwebSubmodule spiritwebSubmodule : spiritweb.getSubmodules().values())
			{
				spiritwebSubmodule.collectMenuInfo(m_infoText);
			}

			for (String s : m_infoText)
			{
				if (s.toLowerCase().contains("hydration"))
				{
					guiGraphics.drawString(font, s, leftSideX, y[0], 0xffffffff);
					y[0] += 10;
				}
			}
		}

		if (selectedManifestation == null)
		{
			return;
		}

		y[0] = (int) middle_y / 2;
		int rightSideX = middle_x + 35;

		guiGraphics.drawString(font, I18n.get(selectedManifestation.getTranslationKey()), rightSideX, y[0], 0xffffffff);
		//todo mode translation
		guiGraphics.drawString(font, "Mode: " + spiritweb.getMode(selectedManifestation), rightSideX, y[0] + 10, 0xffffffff);

	}

	//Quadrant Strings
	/*private void renderMetalQuadrantsStrings(GuiGraphics guiGraphics)
	{
		PoseStack matrixStack = guiGraphics.pose();
		m_infoText.clear();

		List<Manifestation> maniList = spiritweb.getAvailableManifestations();
		int maniListSize = maniList.size();
		boolean manifestationNotNull = selectedManifestation != null;
		boolean inMetalSubmenu = (selectedPowerType == Manifestations.ManifestationTypes.ALLOMANCY || selectedPowerType == Manifestations.ManifestationTypes.FERUCHEMY) && maniListSize > 16;
		boolean shouldShowAllomancy = maniListSize <= 16 && manifestationNotNull && selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY;
		boolean shouldShowFeruchemy = maniListSize <= 16 && manifestationNotNull && selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY;
		boolean manifestationIsSelected = shouldShowAllomancy || shouldShowFeruchemy;

		for (ISpiritwebSubmodule submodule : spiritweb.getSubmodules().values())
		{
			submodule.collectMenuInfo(m_infoText);
		}

		for (MetalQuadrant quad : metalQuadrants)
		{
			boolean foundNumber = false;
			boolean selectedAllomancyType = selectedPowerType == Manifestations.ManifestationTypes.ALLOMANCY || shouldShowAllomancy;
			boolean selectedFeruchemyType = selectedPowerType == Manifestations.ManifestationTypes.FERUCHEMY || shouldShowFeruchemy;

			// if there are submenus, or if a manifestation is selected in the menu, proceed
			if (inMetalSubmenu || manifestationIsSelected)
			{
				for (String s : m_infoText)
				{
					if (((inMetalSubmenu && selectedAllomancyType) || shouldShowAllomancy) && s.toLowerCase().contains("a. " + quad.metalType.getName()) && maniList.contains(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(quad.metalType.getID())))
					{
						String displayString = s.split(":")[1].stripLeading();
						guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) quad.centerY + font.lineHeight, 0xffffffff);

						displayString = quad.metalType.getName().substring(0,1).toUpperCase() + quad.metalType.getName().substring(1);
						guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) (quad.centerY - font.lineHeight*1.5F), 0xffffffff);

						foundNumber = true;
						break;
					}
					else if (((inMetalSubmenu && selectedFeruchemyType) || shouldShowFeruchemy) && s.toLowerCase().contains("f. " + quad.metalType.getName()) && maniList.contains(Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(quad.metalType.getID())))
					{
						String displayString = s.split(":")[1].stripLeading();
						guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) quad.centerY + font.lineHeight, 0xffffffff);

						displayString = quad.metalType.getName().substring(0,1).toUpperCase() + quad.metalType.getName().substring(1);
						guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) (quad.centerY - font.lineHeight*1.5F), 0xffffffff);

						foundNumber = true;
						break;
					}
				}
			}

			boolean alloManiListContains = maniList.contains(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(quad.metalType.getID()));
			boolean feruManiListContains = maniList.contains(Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(quad.metalType.getID()));

			boolean shouldDrawMetalNames = !foundNumber && ((inMetalSubmenu && ((selectedAllomancyType && alloManiListContains) || (selectedFeruchemyType && feruManiListContains)))
														|| (!inMetalSubmenu && ((shouldShowAllomancy && alloManiListContains) || (shouldShowFeruchemy && feruManiListContains))));
			if (shouldDrawMetalNames)
			{
				String displayString;
				displayString = "0";
				guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) (quad.centerY + font.lineHeight), 0xffffffff);

				displayString = quad.metalType.getName().substring(0,1).toUpperCase() + quad.metalType.getName().substring(1);
				guiGraphics.drawString(font, displayString, (int) (quad.centerX - font.width(displayString)/2F), (int) (quad.centerY - font.lineHeight*1.5F), 0xffffffff);
			}
		}
	}*/

	private void renderSidedButtonStrings(GuiGraphics guiGraphics, double middle_x, double middle_y)
	{
		for (final SidedMenuButton sideButton : sidedMenuButtons)
		{
			//but only if that sided button is highlighted
			if (sideButton.highlighted)
			{
				final String text = I18n.get(sideButton.name);

				switch (sideButton.textSide)
				{
					case WEST:
						guiGraphics.drawString(font, text, (int) (middle_x + sideButton.x1 - 8) - font.width(text), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case EAST:
						guiGraphics.drawString(font, text, (int) (middle_x + sideButton.x2 + 8), (int) (middle_y + sideButton.y1 + 6), 0xffffffff);
						break;
					case UP:
						guiGraphics.drawString(font, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 - 14), 0xffffffff);
						break;
					case DOWN:
						guiGraphics.drawString(font, text, (int) (middle_x + (sideButton.x1 + sideButton.x2) * 0.5 - font.width(text) * 0.5), (int) (middle_y + sideButton.y1 + 24), 0xffffffff);
						break;
				}

			}
		}
	}

	private void renderRadialButtonStrings(GuiGraphics guiGraphics, int middle_x, int middle_y)
	{
		for (final NicroSpiritwebPowerButton button : spiritwebPowerButtons)
		{
			//but only if that button is highlighted
			if (button.highlighted)
			{
				final double x = button.centerX;
				final double y = button.centerY;

				int fixed_x = (int) x;//(x + TEXT_DISTANCE);
				final int fixed_y = (int) y + 20;//(y + TEXT_DISTANCE);

				//todo localisation check
				final String text = I18n.get(button.manifestation.getTranslationKey());

				fixed_x = (int) (x > 0
				                 ? fixed_x - (font.width(text) + 10)
				                 : fixed_x + 10);

				guiGraphics.drawString(font, text, middle_x + fixed_x, middle_y + fixed_y, 0xffffffff);

				//no need to keep searching, we only keep one highlighted at a time.
				break;
			}
		}
	}

	private void renderSidedButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{
		final StringBuilder stringBuilder = new StringBuilder();
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			stringBuilder.setLength(0);
			final double x = (button.x1 + button.x2) / 2 + 0.01;
			final double y = (button.y1 + button.y2) / 2 + 0.01;

			stringBuilder
					.append("textures/icon/")
					.append(button.name)
					.append(".png");

			//RenderSystem.setShaderTexture(0, new ResourceLocation(button.name, stringBuilder.toString()));
			guiGraphics.blit(new ResourceLocation(button.name, stringBuilder.toString()), (int) (middleX + x - 8), (int) (middleY + y - 8), 16, 16, 0, 0, 18, 18, 18, 18);

		}
	}

	private void renderRadialButtonIcons(GuiGraphics guiGraphics, double middleX, double middleY)
	{


		final StringBuilder stringBuilder = new StringBuilder();
		for (final NicroSpiritwebPowerButton menuRegion : spiritwebPowerButtons)
		{
			stringBuilder.setLength(0);
			final double x = menuRegion.centerX;
			final double y = menuRegion.centerY;

			final double scalex = 15 * 0.5;
			final double scaley = 15 * 0.5;
			final double x1 = x - scalex;
			final double y1 = y - scaley;

			Manifestation mani = menuRegion.manifestation;
			final Manifestations.ManifestationTypes manifestationType = mani.getManifestationType();
			String manifestationTypeName = manifestationType.getName();
			stringBuilder
					.append("textures/icon/")
					.append(manifestationTypeName)
					.append("/");

			switch (manifestationType)
			{
				case ALLOMANCY:
				case FERUCHEMY:
					if (mani instanceof IHasMetalType metalType)
					{
						stringBuilder.append(metalType.getMetalType().getName());
					}
					break;
				case SURGEBINDING:
					stringBuilder.append(mani.getName());
					break;
				case AON_DOR:
					break;
				case AWAKENING:
					break;
			}

			stringBuilder.append(".png");
			final ResourceLocation textureLocation = new ResourceLocation(mani.getRegistryName().getNamespace(), stringBuilder.toString());
			RenderSystem.setShaderTexture(0, textureLocation);
			guiGraphics.blit(textureLocation,
					(int) (middleX + x1),
					(int) (middleY + y1),
					16,
					16,
					0,
					0,
					18,
					18,
					18,
					18);

		}
	}

	private void renderSidedButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		for (final SidedMenuButton button : sidedMenuButtons)
		{
			final float a = 0.5f;
			float f;
			if (button.x1 <= mouseVecX && button.x2 >= mouseVecX && button.y1 <= mouseVecY && button.y2 >= mouseVecY)
			{
				f = 1;
				button.highlighted = true;
				doAction = button;
			}
			else
			{
				button.highlighted = false;

				//highlight button, but don't draw string unless mouse over
				f = selectedPowerType.getID() == button.powerType
				    ? 1
				    : 0;
			}

			//set first triangle
			buffer.vertex(middle_x + button.x1, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + button.x1, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
			//set second triangle
			buffer.vertex(middle_x + button.x2, middle_y + button.y2, 0).color(f, f, f, a).endVertex();
			buffer.vertex(middle_x + button.x2, middle_y + button.y1, 0).color(f, f, f, a).endVertex();
		}
	}

	//MetalQuadrants

	private void renderMetalQuadrants(BufferBuilder buffer)
	{
		List<Manifestation> maniList = spiritweb.getAvailableManifestations();
		boolean manifestationIsSelected = selectedManifestation != null && maniList.size() <= 16 && (selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY || selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY);
		boolean allomancySubmenuOpen = selectedPowerType == Manifestations.ManifestationTypes.ALLOMANCY && maniList.size() > 16;
		boolean feruchemySubmenuOpen = selectedPowerType == Manifestations.ManifestationTypes.FERUCHEMY && maniList.size() > 16;
		boolean hasSubmenu = allomancySubmenuOpen || feruchemySubmenuOpen;
		boolean allomancySelected = !hasSubmenu && manifestationIsSelected && selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY;
		boolean feruchemySelected = !hasSubmenu && manifestationIsSelected && selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY;
		int r = 0, g = 0, b = 0, a = 127;        // 127 is halfway between 0 and 255, so 0.5 transparency

		for (MetalQuadrant quadrant : metalQuadrants)
		{
			Manifestation alloMani = Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(quadrant.metalType.getID());
			Manifestation feruMani = Manifestations.ManifestationTypes.FERUCHEMY.getManifestation(quadrant.metalType.getID());

			// if player doesn't have the manifestation, skip it
			if ((hasSubmenu && ((allomancySubmenuOpen && maniList.contains(alloMani)) || (feruchemySubmenuOpen && maniList.contains(feruMani))))
					|| (!hasSubmenu && ((allomancySelected && maniList.contains(alloMani)) || (feruchemySelected && maniList.contains(feruMani)))))
			{
				buffer.vertex(quadrant.centerX - MetalQuadrant.width / 2, quadrant.centerY - MetalQuadrant.height / 2, 0).color(r, g, b, a).endVertex();
				buffer.vertex(quadrant.centerX - MetalQuadrant.width / 2, quadrant.centerY + MetalQuadrant.height / 2, 0).color(r, g, b, a).endVertex();
				buffer.vertex(quadrant.centerX + MetalQuadrant.width / 2, quadrant.centerY + MetalQuadrant.height / 2, 0).color(r, g, b, a).endVertex();
				buffer.vertex(quadrant.centerX + MetalQuadrant.width / 2, quadrant.centerY - MetalQuadrant.height / 2, 0).color(r, g, b, a).endVertex();
			}
		}
	}

	private void renderSpiritwebButtons(BufferBuilder buffer, double mouseVecX, double mouseVecY, double middle_x, double middle_y)
	{
		if (!spiritwebPowerButtons.isEmpty())
		{
			final float ring_inner_edge = -20;
			final float ring_outer_edge = -60;

			final int totalButtons = spiritwebPowerButtons.size();


			Vector2 innerEdge;
			Vector2 outerEdge;

			int drawMode = spiritwebPowerButtons.size();

			innerEdge = new Vector2(0, ring_inner_edge);
			outerEdge = new Vector2(0, ring_outer_edge);

			//Goes through every Radial button
			int buttonAmount = spiritwebPowerButtons.size() - 1;
			int i = buttonAmount;
			while (i >= 0)
			{
				NicroSpiritwebPowerButton region = spiritwebPowerButtons.get(i);
				//left side inner point
				double x1m1;
				double y1m1;

				//left side outer point
				double x2m1;
				double y2m1;

				//right side inner point
				double x1m2;
				double y1m2;

				//right side outer point
				double x2m2;
				double y2m2;


				if (heldButton != null && heldButton.powerButton == region)
				{
					//Left side upper
					x1m1 = mouseVecX - 10;
					y1m1 = mouseVecY - 10;

					//Left side downer
					x2m1 = mouseVecX - 10;
					y2m1 = mouseVecY + 10;

					//Right side upper
					x1m2 = mouseVecX + 10;
					y1m2 = mouseVecY - 10;

					//Right side downer
					x2m2 = mouseVecX + 10;
					y2m2 = mouseVecY + 10;
				}
				else
				{
					final int positionAdjustX = 0;
					final int positionAdjustY = -50;
					//Left side upper
					x1m1 = (25 * (i - (double) buttonAmount / 2)) - 10 + positionAdjustX;
					y1m1 = -10 + positionAdjustY;

					//Left side downer
					x2m1 = (25 * (i - (double) buttonAmount / 2)) - 10 + positionAdjustX;
					y2m1 = 10 + positionAdjustY;

					//Right side Upper
					x1m2 = (25 * (i - (double) buttonAmount / 2)) + 10 + positionAdjustX;
					y1m2 = -10 + positionAdjustY;

					//Right side Downer
					x2m2 = (25 * (i - (double) buttonAmount / 2)) + 10 + positionAdjustX;
					y2m2 = 10 + positionAdjustY;
				}


				//the center of a regular polygon can be found by adding up
				// all corner positions and divide by the total count
				region.centerX = (x1m1 + x2m1 + x1m2 + x2m2) / 4;
				region.centerY = (y1m1 + y2m1 + y1m2 + y2m2) / 4;


				float opacity = 0.5f;
				float brightness = 0f;

				final boolean showHighlight;

				{
					showHighlight =
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
				}

				//if mouse is within the region, as defined by the two triangles
				//if (begin_rad <= mouseAngle && mouseAngle <= end_rad && showHighlight)
				if (showHighlight)
				{
					brightness = 0.1f;
					region.highlighted = true;
					selectedManifestation = region.manifestation;
				}
				else
				{
					region.highlighted = false;
				}

				float lerpPositive = 0;

				if (region.manifestation != null)
				{
					int mode = region.manifestation.getMode(spiritweb);
					int modeMin = region.manifestation.modeMin(spiritweb);
					int modeMax = region.manifestation.modeMax(spiritweb);

					lerpPositive = MathHelper.InverseLerp(0, 16, (float) region.strength - 0.1f);

				}

				float r = brightness;
				float g = brightness;
				float b = (lerpPositive / 2 + 0.3f) + brightness;

				opacity = lerpPositive / 4 + 0.3f;


				{
					//set the square pos
					buffer.vertex(middle_x + x1m1, middle_y + y1m1, 0).color(r, g, b, opacity).endVertex();
					buffer.vertex(middle_x + x2m1, middle_y + y2m1, 0).color(r, g, b, opacity).endVertex();

					buffer.vertex(middle_x + x2m2, middle_y + y2m2, 0).color(r, g, b, opacity).endVertex();
					buffer.vertex(middle_x + x1m2, middle_y + y1m2, 0).color(r, g, b, opacity).endVertex();
				}

				i--;
			}
		}
	}


}
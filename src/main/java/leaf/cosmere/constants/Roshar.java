/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.manifestation.surgebinding.SurgebindingBase;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Roshar
{
	public enum GemSize
	{
		BROAM,
		MARK,
		CHIP;

		public float getChargeModifier()
		{
			switch (this)
			{
				case BROAM:
					return 1f;
				case MARK:
					return 0.5f;
				case CHIP:
					return 0.1f;
			}
			return 0;
		}
	}

	public enum Surges
	{
		ADHESION(0),
		GRAVITATION(1),
		DIVISION(2),
		ABRASION(3),
		PROGRESSION(4),
		ILLUMINATION(5),
		TRANSFORMATION(6),
		TRANSPORTATION(7),
		COHESION(8),
		TENSION(9);

		private final int id;

		Surges(int id)
		{
			this.id = id;
		}

		public static Optional<Surges> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(surge -> surge.id == value)
					.findFirst();
		}

		public int getID()
		{
			return id;
		}

		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
		}

		public SurgebindingBase getSurge()
		{
			return (SurgebindingBase) ManifestationRegistry.SURGEBINDING_POWERS.get(this).get();
		}

		public int getColor()
		{
			return 0;
		}

		public RegistryObject<Attribute> getAttribute()
		{
			final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.SURGEBINDING_ATTRIBUTES.get(this);
			return attributeRegistryObject;
		}

		public String getRegistryName()
		{
			return "surgebinding_" + getName();
		}
	}

	//https://coppermind.net/wiki/Polestone
	public enum Polestone implements Tier
	{
		SAPPHIRE(0),
		SMOKESTONE(1),
		RUBY(2),
		DIAMOND(3),
		EMERALD(4),
		GARNET(5),
		ZIRCON(6),
		AMETHYST(7),
		TOPAZ(8),
		HELIODOR(9);


		private final int id;

		Polestone(int id)
		{
			this.id = id;
		}

		public static Optional<Polestone> valueOf(int value)
		{
			return Arrays.stream(values())
					.filter(gemType -> gemType.id == value)
					.findFirst();
		}

		public int getID()
		{
			return id;
		}

		public String getName()
		{
			return name().toLowerCase(Locale.ROOT);
		}


		public String getAssociatedOrder()
		{
			switch (this)
			{
				default:
				case SAPPHIRE:
					return "windrunner";
				case SMOKESTONE:
					return "skybreaker";
				case RUBY:
					return "dustbringer";
				case DIAMOND:
					return "edgedancer";
				case EMERALD:
					return "truthwatcher";
				case GARNET:
					return "lightweaver";
				case ZIRCON:
					return "elsecaller";
				case AMETHYST:
					return "willshaper";
				case TOPAZ:
					return "stoneward";
				case HELIODOR:
					return "bondsmith";
			}
		}

		public int getColorValue()
		{
			return getColor().getRGB();
		}

		public Color getColor()
		{
			switch (this)
			{
				default:
				case SAPPHIRE:
					return GemColours.SAPPHIRE;
				case SMOKESTONE:
					return GemColours.SMOKESTONE;
				case RUBY:
					return GemColours.RUBY;
				case DIAMOND:
					return GemColours.DIAMOND;
				case EMERALD:
					return GemColours.EMERALD;
				case GARNET:
					return GemColours.GARNET;
				case ZIRCON:
					return GemColours.ZIRCON;
				case AMETHYST:
					return GemColours.AMETHYST;
				case TOPAZ:
					return GemColours.TOPAZ;
				case HELIODOR:
					return GemColours.HELIODOR;
			}
		}

		public Item getPolestoneItem(GemSize size)
		{
			switch (size)
			{
				case BROAM:
					return ItemsRegistry.POLESTONE_BROAMS.get(this).get();
				case MARK:
					return ItemsRegistry.POLESTONE_MARKS.get(this).get();
				case CHIP:
					return ItemsRegistry.POLESTONE_CHIPS.get(this).get();
			}

			return null;
		}


		@Override
		public int getUses()
		{
			return 0;
		}

		@Override
		public float getSpeed()
		{
			return 0;
		}

		@Override
		public float getAttackDamageBonus()
		{
			return 0;
		}

		@Override
		public int getLevel()
		{
			return 0;
		}

		@Override
		public int getEnchantmentValue()
		{
			return 0;
		}

		@Override
		public Ingredient getRepairIngredient()
		{
			return null;
		}

		public RegistryObject<Attribute> getFirstSurgeAttribute()
		{
			switch (this)
			{
				default:
				case SAPPHIRE://Adhesion & Gravitation
					return Surges.ADHESION.getAttribute();
				case SMOKESTONE://Gravitation & Division
					return Surges.GRAVITATION.getAttribute();
				case RUBY://Division & Abrasion
					return Surges.DIVISION.getAttribute();
				case DIAMOND://Abrasion & Progression
					return Surges.ABRASION.getAttribute();
				case EMERALD://Progression & Illumination
					return Surges.PROGRESSION.getAttribute();
				case GARNET://Illumination & Transformation
					return Surges.ILLUMINATION.getAttribute();
				case ZIRCON://	Transformation & Transportation
					return Surges.TRANSFORMATION.getAttribute();
				case AMETHYST://Transportation & Cohesion
					return Surges.TRANSPORTATION.getAttribute();
				case TOPAZ://Cohesion & Tension
					return Surges.COHESION.getAttribute();
				case HELIODOR://Tension & Adhesion
					return Surges.TENSION.getAttribute();
			}
		}

		public RegistryObject<Attribute> getSecondSurgeAttribute()
		{
			switch (this)
			{
				default:
				case SAPPHIRE://Adhesion & Gravitation
					return Surges.GRAVITATION.getAttribute();
				case SMOKESTONE://Gravitation & Division
					return Surges.DIVISION.getAttribute();
				case RUBY://Division & Abrasion
					return Surges.ABRASION.getAttribute();
				case DIAMOND://Abrasion & Progression
					return Surges.PROGRESSION.getAttribute();
				case EMERALD://Progression & Illumination
					return Surges.ILLUMINATION.getAttribute();
				case GARNET://Illumination & Transformation
					return Surges.TRANSFORMATION.getAttribute();
				case ZIRCON://Transformation & Transportation
					return Surges.TRANSPORTATION.getAttribute();
				case AMETHYST://Transportation & Cohesion
					return Surges.COHESION.getAttribute();
				case TOPAZ://Cohesion & Tension
					return Surges.TENSION.getAttribute();
				case HELIODOR://Tension & Adhesion
					return Surges.ADHESION.getAttribute();
			}
		}
	}


	private static class GemColours
	{
		public static final Color SAPPHIRE = Color.decode("#3991f3");
		public static final Color SMOKESTONE = Color.decode("#c0a6b3");
		public static final Color RUBY = Color.decode("#b40502");
		public static final Color DIAMOND = Color.decode("#f5faf3");
		public static final Color EMERALD = Color.decode("#52b984");
		public static final Color GARNET = Color.decode("#e03935");
		public static final Color ZIRCON = Color.decode("#3ab7bb");
		public static final Color AMETHYST = Color.decode("#c975e4");
		public static final Color TOPAZ = Color.decode("#e3681a");
		public static final Color HELIODOR = Color.decode("#f3dd25");
	}
}

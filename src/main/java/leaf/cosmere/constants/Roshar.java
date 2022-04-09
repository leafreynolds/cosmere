/*
 * File created ~ 9 - 4 - 2022 ~ Leaf
 */

package leaf.cosmere.constants;

import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

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


		public int getColor()
		{
			return 0;
		}
	}

	public enum Polestone implements IItemTier
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
					return GemColours.WINDRUNNER;
				case SMOKESTONE:
					return GemColours.SKYBREAKER;
				case RUBY:
					return GemColours.DUSTBRINGER;
				case DIAMOND:
					return GemColours.EDGEDANCER;
				case EMERALD:
					return GemColours.TRUTHWATCHER;
				case GARNET:
					return GemColours.LIGHTWEAVER;
				case ZIRCON:
					return GemColours.ELSECALLER;
				case AMETHYST:
					return GemColours.WILLSHAPER;
				case TOPAZ:
					return GemColours.STONEWARD;
				case HELIODOR:
					return GemColours.BONDSMITH;
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

	}


	private static class GemColours
	{
		public static final Color WINDRUNNER = Color.decode("#4a6682");
		public static final Color SKYBREAKER = Color.decode("#716b79");
		public static final Color DUSTBRINGER = Color.decode("#8c4f49");
		public static final Color EDGEDANCER = Color.decode("#8d8e92");
		public static final Color TRUTHWATCHER = Color.decode("#4d7661");
		public static final Color LIGHTWEAVER = Color.decode("#7c515b");
		public static final Color ELSECALLER = Color.decode("#4f5b6d");
		public static final Color WILLSHAPER = Color.decode("#6b4b66");
		public static final Color STONEWARD = Color.decode("#807158");
		public static final Color BONDSMITH = Color.decode("#aeac79");
	}
}

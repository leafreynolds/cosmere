package leaf.cosmere.metallurgy.common.util;

import leaf.cosmere.api.helpers.StackNBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for managing alloy composition data in NBT.
 * Handles metal percentages and contamination tracking.
 * Supports any metal from any mod using registry names.
 */
public class AlloyComposition {
    private static final String TAG_COMPOSITION = "composition";
    private static final String TAG_CONTAMINATION = "contamination";
    private static final String TAG_PERCENTAGE = "percentage";

    /**
     * Sets the composition of metals in an item stack.
     * 
     * @param stack       The item stack to modify
     * @param composition Map of metal registry name (e.g., "minecraft:iron_ingot")
     *                    to percentage (0.0 to 1.0)
     */
    public static void setComposition(ItemStack stack, Map<String, Double> composition) {
        CompoundTag compTag = new CompoundTag();
        for (Map.Entry<String, Double> entry : composition.entrySet()) {
            compTag.putDouble(entry.getKey(), entry.getValue());
        }
        StackNBTHelper.setCompound(stack, TAG_COMPOSITION, compTag);
    }

    /**
     * Gets the composition of metals from an item stack.
     * 
     * @param stack The item stack to read
     * @return Map of metal registry name to percentage (0.0 to 1.0)
     */
    public static Map<String, Double> getComposition(ItemStack stack) {
        Map<String, Double> composition = new HashMap<>();
        CompoundTag compTag = StackNBTHelper.getCompound(stack, TAG_COMPOSITION, false);

        for (String key : compTag.getAllKeys()) {
            composition.put(key, compTag.getDouble(key));
        }

        return composition;
    }

    /**
     * Sets the general contamination percentage for non-metal impurities.
     * 
     * @param stack         The item stack to modify
     * @param contamination Contamination percentage (0.0 to 1.0)
     */
    public static void setContamination(ItemStack stack, double contamination) {
        StackNBTHelper.setDouble(stack, TAG_CONTAMINATION, contamination);
    }

    /**
     * Gets the general contamination percentage.
     * 
     * @param stack The item stack to read
     * @return Contamination as a value from 0.0 to 1.0 (defaults to 0.0)
     */
    public static double getContamination(ItemStack stack) {
        return StackNBTHelper.getDouble(stack, TAG_CONTAMINATION, 0.0);
    }

    /**
     * Sets the percentage for a metal fragment (how much of an ingot it
     * represents).
     * 
     * @param stack      The item stack to modify
     * @param percentage The percentage (0.0 to 1.0)
     */
    public static void setFragmentPercentage(ItemStack stack, double percentage) {
        StackNBTHelper.setDouble(stack, TAG_PERCENTAGE, percentage);
    }

    /**
     * Gets the percentage for a metal fragment.
     * 
     * @param stack The item stack to read
     * @return The percentage (0.0 to 1.0), defaults to 1.0 if not set
     */
    public static double getFragmentPercentage(ItemStack stack) {
        return StackNBTHelper.getDouble(stack, TAG_PERCENTAGE, 1.0);
    }

    /**
     * Checks if an item has composition data.
     * 
     * @param stack The item stack to check
     * @return true if composition data exists
     */
    public static boolean hasComposition(ItemStack stack) {
        return StackNBTHelper.verifyExistance(stack, TAG_COMPOSITION);
    }

    /**
     * Helper method to get a display name for a metal from its registry name.
     * Extracts the path portion and formats it nicely.
     * 
     * @param registryName The registry name (e.g., "minecraft:iron_ingot")
     * @return A formatted display name (e.g., "Iron")
     */
    public static String getDisplayName(String registryName) {
        try {
            ResourceLocation rl = new ResourceLocation(registryName);
            String path = rl.getPath();

            // Remove common suffixes
            path = path.replace("_ingot", "")
                    .replace("_powder", "")
                    .replace("_fragment", "");

            // Convert underscores to spaces and capitalize
            String[] parts = path.split("_");
            StringBuilder result = new StringBuilder();
            for (String part : parts) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1));
            }
            return result.toString();
        } catch (Exception e) {
            // Fallback to just using the registry name
            return registryName;
        }
    }

    public static void addContaminationTooltip(ItemStack stack, List<Component> tooltip) {
        // Display composition if present
        if (AlloyComposition.hasComposition(stack)) {
            Map<String, Double> composition = AlloyComposition.getComposition(stack);

            if (!composition.isEmpty()) {
                tooltip.add(Component.literal("Composition:").withStyle(ChatFormatting.GRAY));

                for (Map.Entry<String, Double> entry : composition.entrySet()) {
                    double percentage = entry.getValue() * 100.0;
                    String metalName = AlloyComposition.getDisplayName(entry.getKey());
                    tooltip.add(Component.literal(String.format("  %s: %.2f%%", metalName, percentage))
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }

            // Display contamination if present
            double contamination = AlloyComposition.getContamination(stack);
            if (contamination > 0.0) {
                ChatFormatting contamColor = ChatFormatting.RED;
                tooltip.add(Component.literal(String.format("Contamination: %.2f%%", contamination * 100.0))
                        .withStyle(contamColor));
            }
        } else {
            tooltip.add(Component.literal("No composition data").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}

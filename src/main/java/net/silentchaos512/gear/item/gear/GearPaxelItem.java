package net.silentchaos512.gear.item.gear;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.silentchaos512.gear.api.item.GearType;

import java.util.Set;

public class GearPaxelItem extends GearPickaxeItem {
    private static final Set<Material> PAXEL_EFFECTIVE_MATERIALS = ImmutableSet.of(
            Material.DECORATION,
            Material.GLASS,
            Material.PISTON,
            Material.BUILDABLE_GLASS,
            Material.BAMBOO,
            Material.LEAVES,
            Material.PLANT,
            Material.REPLACEABLE_PLANT,
            Material.WOOD
    );

    public GearPaxelItem(GearType gearType) {
        super(gearType, PAXEL_EFFECTIVE_MATERIALS);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return getGearType().canPerformAction(toolAction);
    }
}
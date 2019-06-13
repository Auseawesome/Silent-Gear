package net.silentchaos512.gear.inventory;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.silentchaos512.gear.block.craftingstation.CraftingStationTileEntity;

public final class InventoryCraftingStation extends CraftingInventory {
    private final Container container;
    private final NonNullList<ItemStack> stacks;

    @SuppressWarnings("TypeMayBeWeakened") // So no random types get thrown in by mistake
    public InventoryCraftingStation(Container eventHandlerIn, CraftingStationTileEntity tile, int width, int height) {
        super(eventHandlerIn, width, height);
        this.container = eventHandlerIn;
        // This hold everything after the crafting grid, InventoryCrafting will only give 9 slots
        this.stacks = NonNullList.withSize(tile.getSizeInventory() - CraftingStationTileEntity.CRAFTING_GRID_SIZE, ItemStack.EMPTY);

        // Fill crafting grid slots
        for (int i = 0; i < CraftingStationTileEntity.CRAFTING_GRID_SIZE; ++i) {
            setInventorySlotContents(i, tile.getStackInSlot(i + CraftingStationTileEntity.CRAFTING_GRID_START));
        }

        this.container.onCraftMatrixChanged(this);
    }

    @Override
    public void clear() {
        super.clear();
        stacks.clear();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index < CraftingStationTileEntity.CRAFTING_GRID_SIZE) {
            return super.decrStackSize(index, count);
        }

        ItemStack stack = ItemStackHelper.getAndSplit(stacks, index - CraftingStationTileEntity.CRAFTING_GRID_SIZE, count);
        if (!stack.isEmpty()) {
            markDirty();
        }
        return stack;
    }

    @Override
    public int getSizeInventory() {
        return super.getSizeInventory() + stacks.size();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < CraftingStationTileEntity.CRAFTING_GRID_SIZE) {
            return super.getStackInSlot(index);
        }
        return index < getSizeInventory() ? stacks.get(index - CraftingStationTileEntity.CRAFTING_GRID_SIZE) : ItemStack.EMPTY;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return super.isEmpty();
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index < CraftingStationTileEntity.CRAFTING_GRID_SIZE) {
            return super.removeStackFromSlot(index);
        }
        return ItemStackHelper.getAndRemove(stacks, index - CraftingStationTileEntity.CRAFTING_GRID_SIZE);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < CraftingStationTileEntity.CRAFTING_GRID_SIZE) {
            super.setInventorySlotContents(index, stack);
        } else {
            stacks.set(index - CraftingStationTileEntity.CRAFTING_GRID_SIZE, stack);
        }
    }
}

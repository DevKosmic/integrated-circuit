package net.replaceitem.integratedcircuit;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class IntegratedCircuitCloningRecipe extends CustomRecipe {
    public IntegratedCircuitCloningRecipe(CraftingBookCategory category) {
        super(category);
    }
    
    public static DataComponentType<CustomData> CLONED_COMPONENT = IntegratedCircuit.CIRCUIT_DATA;

    @Override
    public boolean matches(CraftingInput inventory, Level world) {
        int sourceIndex = -1;
        int destIndex = -1;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getItem(i);

            if(stack.isEmpty()) continue;
            if(!(stack.getItem() instanceof IntegratedCircuitItem)) return false;

            if(stack.has(CLONED_COMPONENT)) {
                if(sourceIndex != -1) return false;// Only one should have data
                sourceIndex = i;
            } else {
               if(destIndex != -1) return false;
               destIndex = i;
            }
        }

        return sourceIndex != -1 && destIndex != -1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider wrapperLookup) {
        int sourceIndex = -1;
        int destIndex = -1;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if(stack.isEmpty()) continue;
            if(!(stack.getItem() instanceof IntegratedCircuitItem)) return ItemStack.EMPTY;

            if(stack.has(CLONED_COMPONENT)) {
                if(sourceIndex != -1) return ItemStack.EMPTY; // Only one should have NBT data
                sourceIndex = i;
            } else {
                if(destIndex != -1) return ItemStack.EMPTY;
                destIndex = i;
            }
        }

        if(sourceIndex != -1 && destIndex != -1) {
            ItemStack source = input.getItem(sourceIndex);
            ItemStack dest = input.getItem(destIndex);

            ItemStack craftedStack = dest.copyWithCount(1);
            craftedStack.set(CLONED_COMPONENT, source.get(CLONED_COMPONENT));
            craftedStack.set(DataComponents.CUSTOM_NAME, source.get(DataComponents.CUSTOM_NAME));
            return craftedStack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remainder = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if(stack.isEmpty()) continue;
            if(!(stack.getItem() instanceof IntegratedCircuitItem)) return remainder;

            if(stack.has(CLONED_COMPONENT)) {
                remainder.set(i, stack.copyWithCount(1));
                return remainder;
            }
        }
        return remainder;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return IntegratedCircuit.CIRCUIT_CLONING_RECIPE;
    }
}
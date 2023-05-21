package de.dafuqs.spectrum.inventories.slots;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.screen.slot.*;

public class ReadOnlySlot extends Slot {
	
	public ReadOnlySlot(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}
	
	@Override
	public boolean canInsert(ItemStack stack) {
		return false;
	}
	
	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {
		return false;
	}
	
}

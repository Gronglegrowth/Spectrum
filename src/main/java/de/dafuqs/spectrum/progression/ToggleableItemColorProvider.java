package de.dafuqs.spectrum.progression;

import net.minecraft.client.color.item.*;
import net.minecraft.item.*;

public class ToggleableItemColorProvider implements ItemColorProvider {
	
	final ItemColorProvider vanillaProvider;
	boolean shouldApply;
	
	public ToggleableItemColorProvider(ItemColorProvider vanillaProvider) {
		this.vanillaProvider = vanillaProvider;
		this.shouldApply = true;
	}
	
	public void setShouldApply(boolean shouldApply) {
		this.shouldApply = shouldApply;
	}
	
	@Override
	public int getColor(ItemStack stack, int tintIndex) {
		if (shouldApply) {
			return vanillaProvider.getColor(stack, tintIndex);
		} else {
			// no tint
			return 16777215;
		}
	}
}

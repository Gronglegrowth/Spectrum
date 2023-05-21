package de.dafuqs.spectrum.blocks.decoration;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.util.*;

import java.util.*;

public class PigmentBlock extends Block {
	
	private static final Map<DyeColor, PigmentBlock> BLOCKS = Maps.newEnumMap(DyeColor.class);
	protected final DyeColor color;
	
	public PigmentBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		BLOCKS.put(color, this);
	}
	
	public DyeColor getColor() {
		return this.color;
	}
	
	public static PigmentBlock byColor(DyeColor color) {
		return BLOCKS.get(color);
	}
	
}

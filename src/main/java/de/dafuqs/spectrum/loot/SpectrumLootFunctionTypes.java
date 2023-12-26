package de.dafuqs.spectrum.loot;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.loot.functions.*;
import net.minecraft.loot.function.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

public class SpectrumLootFunctionTypes {
	
	public static LootFunctionType DYE_RANDOMLY;
	public static LootFunctionType FERMENT_RANDOMLY;
	public static LootFunctionType SET_NBT_RANDOMLY;
	public static LootFunctionType FILL_POTION_FILLABLE;
	
	private static LootFunctionType register(String id, JsonSerializer<? extends LootFunction> jsonSerializer) {
		return Registry.register(Registries.LOOT_FUNCTION_TYPE, SpectrumCommon.locate(id), new LootFunctionType(jsonSerializer));
	}
	
	public static void register() {
		DYE_RANDOMLY = register("dye_randomly", new DyeRandomlyLootFunction.Serializer());
		FERMENT_RANDOMLY = register("ferment_randomly", new FermentRandomlyLootFunction.Serializer());
		SET_NBT_RANDOMLY = register("merge_nbt_randomly", new MergeNbtRandomlyLootFunction.Serializer());
		FILL_POTION_FILLABLE = register("fill_potion_fillable", new FillPotionFillableLootCondition.Serializer());
	}
	
}

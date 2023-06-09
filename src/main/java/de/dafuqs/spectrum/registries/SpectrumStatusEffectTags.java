package de.dafuqs.spectrum.registries;

import de.dafuqs.spectrum.*;
import net.minecraft.entity.effect.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;

import java.util.*;

public class SpectrumStatusEffectTags {
	
	public static TagKey<StatusEffect> UNCURABLE;
	public static TagKey<StatusEffect> NO_DURATION_EXTENSION;
	
	public static void register() {
		UNCURABLE = of("uncurable");
		NO_DURATION_EXTENSION = of("no_duration_extension");
	}
	
	private static TagKey<StatusEffect> of(String id) {
		return TagKey.of(Registry.MOB_EFFECT_KEY, SpectrumCommon.locate(id));
	}
	
	public static boolean isIn(TagKey<StatusEffect> tag, StatusEffect effect) {
		int id = Registry.STATUS_EFFECT.getRawId(effect);
		Optional<RegistryEntry<StatusEffect>> entry = Registry.STATUS_EFFECT.getEntry(id);
		return entry.map(statusEffectRegistryEntry -> statusEffectRegistryEntry.isIn(tag)).orElse(false);
	}
	
	public static boolean isUncurable(StatusEffect statusEffect) {
		return isIn(SpectrumStatusEffectTags.UNCURABLE, statusEffect);
	}
	
}

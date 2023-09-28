package de.dafuqs.spectrum.blocks.boom;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.food.beverages.properties.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class IncandescentAmalgamItem extends BlockItem implements DamageAwareItem, FermentedItem {
	
	public IncandescentAmalgamItem(Block block, Settings settings) {
		super(block, settings);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		stack = super.finishUsing(stack, world, user);
		
		user.damage(SpectrumDamageSources.INCANDESCENCE, 500.0F);
		
		float explosionPower = getExplosionPower(stack);
		world.createExplosion(user, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), explosionPower / 5, false, Explosion.DestructionType.DESTROY);
		world.createExplosion(user, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(user), user.getX(), user.getY(), user.getZ(), explosionPower, true, Explosion.DestructionType.NONE);
		
		if (user.isAlive() && user instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isCreative()) {
			Support.grantAdvancementCriterion(serverPlayerEntity, "survive_drinking_incandescent_amalgam", "survived_drinking_incandescent_amalgam");
		}
		
		return stack;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip").formatted(Formatting.GRAY));
		tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip_power", getExplosionPower(stack)).formatted(Formatting.GRAY));
		if (FermentedItem.isPreviewStack(stack)) {
			tooltip.add(Text.translatable("block.spectrum.incandescent_amalgam.tooltip.preview").formatted(Formatting.GRAY));
		}
	}
	
	@Override
	public void onItemEntityDamaged(DamageSource source, float amount, ItemEntity itemEntity) {
		// remove the itemEntity before dealing damage, otherwise it would cause a stack overflow
		ItemStack stack = itemEntity.getStack();
		itemEntity.remove(Entity.RemovalReason.KILLED);
		
		float explosionPower = getExplosionPower(stack);
		itemEntity.world.createExplosion(itemEntity, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), explosionPower / 8F, false, Explosion.DestructionType.DESTROY);
		itemEntity.world.createExplosion(itemEntity, SpectrumDamageSources.INCANDESCENCE, new EntityExplosionBehavior(itemEntity), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), explosionPower, true, Explosion.DestructionType.NONE);
	}
	
	@Override
	public BeverageProperties getBeverageProperties(ItemStack stack) {
		return BeverageProperties.getFromStack(stack);
	}
	
	public float getExplosionPower(ItemStack stack) {
		return getBeverageProperties(stack).alcPercent + stack.getCount() / 8F;
	}
	
}
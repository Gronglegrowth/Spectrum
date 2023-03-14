package de.dafuqs.spectrum.compat.emi.recipes;

import java.util.List;

import de.dafuqs.spectrum.compat.emi.SpectrumRecipeCategories;
import de.dafuqs.spectrum.compat.emi.SpectrumEmiRecipe;
import de.dafuqs.spectrum.recipe.fusion_shrine.FusionShrineRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget.Alignment;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;

public class FusionShrineEmiRecipe extends SpectrumEmiRecipe<FusionShrineRecipe> {
	private final List<OrderedText> texts;

	public FusionShrineEmiRecipe(FusionShrineRecipe recipe) {
		super(SpectrumRecipeCategories.FUSION_SHRINE, FusionShrineRecipe.UNLOCK_IDENTIFIER, recipe, 134, 60);
		if (recipe.getDescription().isPresent()) {
			MinecraftClient client = MinecraftClient.getInstance();
			texts = client.textRenderer.wrapLines(recipe.getDescription().get(), width);
		} else {
			texts = List.of();
		}
		input = recipe.getIngredientStacks().stream().map(s -> EmiIngredient.of(s.getStacks().stream().map(EmiStack::of).toList())).toList();
		output = List.of(EmiStack.of(recipe.getOutput()));
	}

	@Override
	public int getDisplayHeight() {
		if (isUnlocked()) {
			return height + texts.size() * 10;
		} else {
			return super.getDisplayHeight();
		}
	}

	@Override
	public void addUnlockedWidgets(WidgetHolder widgets) {
		// shrine + fluid
		if (!input.get(0).isEmpty()) {
			widgets.addSlot(EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT), 10, 25).drawBack(false);
			widgets.addSlot(input.get(0), 30, 25).drawBack(false);
		} else {
			widgets.addSlot(EmiStack.of(SpectrumBlocks.FUSION_SHRINE_BASALT), 20, 25).drawBack(false);
		}

		// input slots
		int startX = Math.max(-10, 20 - input.size() * 10);
		for (int i = 1; i < input.size(); i++) {
			widgets.addSlot(input.get(i), startX + i * 20, 0);
		}
		
		widgets.addSlot(output.get(0), 90, 20).output(true).recipeContext(this);
		
		widgets.addFillingArrow(60, 25, recipe.getCraftingTime() * 50);

		for (int i = 0; i < texts.size(); i++) {
			widgets.addText(texts.get(i), 0, 50 + i * 10, 0x3f3f3f, false);
		}

		widgets.addText(getCraftingTimeText(recipe.getCraftingTime(), recipe.getExperience()), width / 2, 50 + texts.size() * 10, 0x3f3f3f, false).horizontalAlign(Alignment.CENTER);
	}
}
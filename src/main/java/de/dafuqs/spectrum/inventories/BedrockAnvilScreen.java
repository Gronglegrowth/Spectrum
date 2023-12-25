package de.dafuqs.spectrum.inventories;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.network.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.lwjgl.glfw.*;

@Environment(EnvType.CLIENT)
public class BedrockAnvilScreen extends HandledScreen<BedrockAnvilScreenHandler> implements ScreenHandlerListener {
	
	private static final Identifier TEXTURE = SpectrumCommon.locate("textures/gui/container/bedrock_anvil.png");
	private final PlayerEntity player;
	private TextFieldWidget nameField;
	private TextFieldWidget loreField;
	
	public BedrockAnvilScreen(BedrockAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.player = inventory.player;
		
		this.titleX = 60;
		this.titleY = this.titleY + 2;
		this.playerInventoryTitleY = 95;
		this.backgroundHeight = 190;
	}
	
	@Override
	public void handledScreenTick() {
		super.handledScreenTick();
		this.nameField.tick();
		this.loreField.tick();
	}
	
	@Override
	protected void init() {
		super.init();
		this.setup();
		handler.addListener(this);
	}
	
	@Override
	public void removed() {
		super.removed();
		handler.removeListener(this);
	}
	
	@Override
	public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		renderBackground(drawContext);
		super.render(drawContext, mouseX, mouseY, delta);
		
		RenderSystem.disableBlend();
		renderForeground(drawContext, mouseX, mouseY, delta);
		drawMouseoverTooltip(drawContext, mouseX, mouseY);
	}
	
	protected void setup() {
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		
		this.nameField = new TextFieldWidget(this.textRenderer, i + 62, j + 24, 98, 12, Text.translatable("container.spectrum.bedrock_anvil"));
		this.nameField.setFocusUnlocked(false);
		this.nameField.setEditable(false);
		this.nameField.setEditableColor(-1);
		this.nameField.setUneditableColor(-1);
		this.nameField.setDrawsBackground(false);
		this.nameField.setMaxLength(50);
		this.nameField.setText("");
		this.nameField.setChangedListener(this::onRenamed);
		this.addSelectableChild(this.nameField);
		
		this.loreField = new TextFieldWidget(this.textRenderer, i + 45, j + 76, 116, 12, Text.translatable("container.spectrum.bedrock_anvil.lore"));
		this.loreField.setFocusUnlocked(false);
		this.loreField.setEditable(false);
		this.loreField.setEditableColor(-1);
		this.loreField.setUneditableColor(-1);
		this.loreField.setDrawsBackground(false);
		this.loreField.setMaxLength(200);
		this.loreField.setText("");
		this.loreField.setChangedListener(this::onLoreChanged);
		this.addSelectableChild(this.loreField);
	}
	
	@Override
	public void resize(MinecraftClient client, int width, int height) {
		String string = this.nameField.getText();
		init(client, width, height);
		nameField.setText(string);
		
		String string2 = this.loreField.getText();
		init(client, width, height);
		loreField.setText(string2);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			client.player.closeHandledScreen();
		}
		
		if (keyCode == GLFW.GLFW_KEY_TAB) {
			Element focusedElement = getFocused();
			if (focusedElement == this.nameField) {
				this.nameField.setFocused(false);
				setFocused(this.loreField);
			} else if (focusedElement == this.loreField) {
				this.loreField.setFocused(false);
				setFocused(this.nameField);
			}
		}
		
		return this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.isActive()
				|| this.loreField.keyPressed(keyCode, scanCode, modifiers) || this.loreField.isActive()
				|| super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	private void onRenamed(String name) {
		if (!name.isEmpty()) {
			String string = name;
			Slot slot = handler.getSlot(0);
			if (slot != null && slot.hasStack() && !slot.getStack().hasCustomName() && name.equals(slot.getStack().getName().getString())) {
				string = "";
			}
			
			if (handler.setNewItemName(string)) {
				PacketByteBuf packetByteBuf = PacketByteBufs.create();
				packetByteBuf.writeString(name);
				ClientPlayNetworking.send(SpectrumC2SPackets.RENAME_ITEM_IN_BEDROCK_ANVIL_PACKET_ID, packetByteBuf);
			}
		}
	}
	
	private void onLoreChanged(String lore) {
		handler.setNewItemLore(lore);
		
		PacketByteBuf packetByteBuf = PacketByteBufs.create();
		packetByteBuf.writeString(lore);
		ClientPlayNetworking.send(SpectrumC2SPackets.ADD_LORE_IN_BEDROCK_ANVIL_PACKET_ID, packetByteBuf);
	}
	
	@Override
	protected void drawForeground(DrawContext drawContext, int mouseX, int mouseY) {
		RenderSystem.disableBlend();
		var textRenderer = this.textRenderer;
		drawContext.drawText(textRenderer, this.title, this.titleX, this.titleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, RenderHelper.GREEN_COLOR, false);
		drawContext.drawText(textRenderer, Text.translatable("container.spectrum.bedrock_anvil.lore"), playerInventoryTitleX, 76, RenderHelper.GREEN_COLOR, false);

		int levelCost = (this.handler).getLevelCost();
		if (levelCost > 0 || this.handler.getSlot(2).hasStack()) {
			int textColor = 8453920;
			Text costText;
			if (!handler.getSlot(2).hasStack()) {
				costText = null;
			} else {
				costText = Text.translatable("container.repair.cost", levelCost);
				if (!handler.getSlot(2).canTakeItems(this.player)) {
					textColor = 16736352;
				}
			}
			
			if (costText != null) {
				int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(costText) - 2;
				drawContext.fill(k - 2, 67 + 24, this.backgroundWidth - 8, 79 + 24, 1325400064);
				drawContext.drawText(textRenderer, costText, k, 93, textColor, true);
			}
		}
	}
	
	@Override
	protected void drawBackground(DrawContext drawContext, float delta, int mouseX, int mouseY) {
		int i = (this.width - this.backgroundWidth) / 2;
		int j = (this.height - this.backgroundHeight) / 2;
		
		// the background
		drawContext.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
		
		// the text field backgrounds
		drawContext.drawTexture(TEXTURE, i + 59, j + 20, 0, this.backgroundHeight + (handler.getSlot(0).hasStack() ? 0 : 16), 110, 16);
		drawContext.drawTexture(TEXTURE, i + 42, j + 72, 0, this.backgroundHeight + (handler.getSlot(0).hasStack() ? 32 : 48), 127, 16);
		
		if ((handler.getSlot(0).hasStack() || handler.getSlot(1).hasStack()) && !handler.getSlot(2).hasStack()) {
			drawContext.drawTexture(TEXTURE, i + 99, j + 45, this.backgroundWidth, 0, 28, 21);
		}
	}
	
	public void renderForeground(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		this.nameField.render(drawContext, mouseX, mouseY, delta);
		this.loreField.render(drawContext, mouseX, mouseY, delta);
	}
	
	@Override
	public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
		if (slotId == 0) {
			if (stack.isEmpty()) {
				this.nameField.setEditable(false);
				this.loreField.setEditable(false);
				this.nameField.setFocusUnlocked(false);
				this.loreField.setFocusUnlocked(false);
				this.nameField.setFocused(false);
				this.loreField.setFocused(false);
				this.nameField.setChangedListener(null);
				this.loreField.setChangedListener(null);
				this.nameField.setText("");
				this.loreField.setText("");
				this.nameField.setChangedListener(this::onRenamed);
				this.loreField.setChangedListener(this::onLoreChanged);
			} else {
				this.nameField.setEditable(true);
				this.loreField.setEditable(true);
				this.nameField.setFocusUnlocked(true);
				this.loreField.setFocusUnlocked(true);
				this.nameField.setFocused(true);
				this.nameField.setText(stack.getName().getString());
				
				String loreString = LoreHelper.getStringFromLoreTextArray(LoreHelper.getLoreList(stack));
				this.loreField.setText(loreString);
			}
			this.setFocused(this.nameField);
		}
	}
	
	@Override
	public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
	
	}
	
}

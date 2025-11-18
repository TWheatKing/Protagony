package com.twk.protagony.client.screen

import com.twk.protagony.network.ClassSelectionPayload
import com.twk.protagony.system.playerclass.PlayerClass
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.Click
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ClassSelectionScreen : Screen(Text.literal("Choose Your Class")) {

    private val classes = PlayerClass.entries
    private var selectedClass: PlayerClass? = null
    private var hoveredCard: Int? = null

    private var cardWidth = 0
    private var cardHeight = 0
    private var cardGap = 0
    private var cardTopMargin = 0

    private data class CardBounds(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int
    ) {
        fun contains(mouseX: Double, mouseY: Double): Boolean {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height
        }
    }

    private val cardBounds = mutableListOf<CardBounds>()

    override fun init() {
        super.init()
        calculateCardBounds()
    }

    private fun calculateCardBounds() {
        cardBounds.clear()

        val sideMargin = (width * 0.05).toInt().coerceAtLeast(20)
        val topMargin = 80
        val bottomMargin = 100
        val availableWidth = width - (sideMargin * 2)
        val availableHeight = height - topMargin - bottomMargin

        val maxCardsInRow = classes.size
        val idealCardWidth = (availableWidth / (maxCardsInRow + 0.3)).toInt()
        val idealCardHeight = (availableHeight * 0.8).toInt()

        cardWidth = idealCardWidth.coerceIn(140, 220)
        cardHeight = idealCardHeight.coerceIn(200, 300)
        cardGap = (width * 0.02).toInt().coerceIn(10, 25)
        cardTopMargin = topMargin

        val totalWidth = (cardWidth * maxCardsInRow) + (cardGap * (maxCardsInRow - 1))
        val startX = ((width - totalWidth) / 2).coerceAtLeast(sideMargin)

        classes.forEachIndexed { index, _ ->
            val x = startX + (index * (cardWidth + cardGap))
            val y = cardTopMargin
            cardBounds.add(CardBounds(x, y, cardWidth, cardHeight))
        }
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        renderInGameBackground(context)

        // Title
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("CHOOSE YOUR CLASS").formatted(Formatting.BOLD, Formatting.GOLD),
            width / 2,
            25,
            0xFFFFAA00.toInt()
        )

        // Subtitle
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("This choice is permanent").formatted(Formatting.GRAY, Formatting.ITALIC),
            width / 2,
            42,
            0xFF888888.toInt()
        )

        // Update hovered card
        hoveredCard = null
        cardBounds.forEachIndexed { index, bounds ->
            if (bounds.contains(mouseX.toDouble(), mouseY.toDouble())) {
                hoveredCard = index
            }
        }

        // Draw confirm button if class selected
        if (selectedClass != null) {
            drawConfirmButton(context, mouseX, mouseY)
        }

        // Draw class cards
        classes.forEachIndexed { index, playerClass ->
            val bounds = cardBounds[index]
            val isHovered = hoveredCard == index
            val isSelected = selectedClass == playerClass

            drawClassCard(context, playerClass, bounds, isHovered, isSelected)
        }

        super.render(context, mouseX, mouseY, delta)
    }

    private fun drawClassCard(
        context: DrawContext,
        playerClass: PlayerClass,
        bounds: CardBounds,
        isHovered: Boolean,
        isSelected: Boolean
    ) {
        // Card background
        val backgroundColor = when {
            isSelected -> 0xCC333333.toInt()
            isHovered -> 0xAA222222.toInt()
            else -> 0x88111111.toInt()
        }
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, backgroundColor)

        // Border
        val borderColor = when {
            isSelected -> getColorForClass(playerClass)
            isHovered -> 0xFFCCCCCC.toInt()
            else -> 0xFF666666.toInt()
        }
        drawBorder(context, bounds, borderColor)

        val centerX = bounds.x + (bounds.width / 2)
        var currentY = bounds.y + 16

        // Class name
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal(playerClass.displayName).formatted(Formatting.BOLD),
            centerX,
            currentY,
            getColorForClass(playerClass)
        )
        currentY += 20

        // Description
        val descLines = wrapText(playerClass.description, bounds.width - 20)
        descLines.forEach { line ->
            context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal(line).formatted(Formatting.GRAY),
                centerX,
                currentY,
                0xFFAAAAAA.toInt()
            )
            currentY += 10
        }

        currentY += 16

        // Bonuses header
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("STARTING BONUSES").formatted(Formatting.BOLD),
            centerX,
            currentY,
            0xFF55FF55.toInt()
        )
        currentY += 12

        // Bonuses list
        playerClass.startingBonuses.forEach { bonus ->
            val lines = wrapText("• $bonus", bounds.width - 30)
            lines.forEach { line ->
                context.drawText(
                    textRenderer,
                    Text.literal(line),
                    bounds.x + 15,
                    currentY,
                    0xFF55FF55.toInt(),
                    true
                )
                currentY += 10
            }
        }

        // Selection hint at bottom
        if (isHovered && !isSelected) {
            context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("Click to Select").formatted(Formatting.ITALIC),
                centerX,
                bounds.y + bounds.height - 20,
                0xFFFFFF55.toInt()
            )
        } else if (isSelected) {
            context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("✓ SELECTED").formatted(Formatting.BOLD),
                centerX,
                bounds.y + bounds.height - 20,
                0xFF55FF55.toInt()
            )
        }
    }

    private fun drawConfirmButton(context: DrawContext, mouseX: Int, mouseY: Int) {
        val buttonWidth = 180
        val buttonHeight = 35
        val margin = 20
        val buttonX = width - buttonWidth - margin
        val buttonY = margin

        val isHovered = mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                mouseY >= buttonY && mouseY <= buttonY + buttonHeight

        val buttonColor = if (isHovered) 0xCC44FF44.toInt() else 0xAA22AA22.toInt()
        val borderColor = if (isHovered) 0xFF55FF55.toInt() else 0xFF33CC33.toInt()

        context.fill(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, buttonColor)
        drawBorder(context, CardBounds(buttonX, buttonY, buttonWidth, buttonHeight), borderColor)

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("CONFIRM").formatted(Formatting.BOLD),
            buttonX + buttonWidth / 2,
            buttonY + (buttonHeight - 8) / 2,
            0xFFFFFFFF.toInt()
        )
    }

    private fun drawBorder(context: DrawContext, bounds: CardBounds, color: Int) {
        val thickness = 2
        context.fill(bounds.x - thickness, bounds.y - thickness, bounds.x + bounds.width + thickness, bounds.y, color)
        context.fill(bounds.x - thickness, bounds.y + bounds.height, bounds.x + bounds.width + thickness, bounds.y + bounds.height + thickness, color)
        context.fill(bounds.x - thickness, bounds.y, bounds.x, bounds.y + bounds.height, color)
        context.fill(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width + thickness, bounds.y + bounds.height, color)
    }

    override fun mouseReleased(click: Click): Boolean {
        val mouseX = click.x
        val mouseY = click.y

        if (click.button() == 0) {
            // Check confirm button
            if (selectedClass != null) {
                val buttonWidth = 180
                val buttonHeight = 35
                val margin = 20
                val buttonX = width - buttonWidth - margin
                val buttonY = margin

                if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
                    confirmSelection()
                    return true
                }
            }

            // Check class cards
            cardBounds.forEachIndexed { index, bounds ->
                if (bounds.contains(mouseX, mouseY)) {
                    selectedClass = classes[index]
                    return true
                }
            }
        }

        return super.mouseReleased(click)
    }

    private fun confirmSelection() {
        selectedClass?.let { playerClass ->
            ClientPlayNetworking.send(ClassSelectionPayload(playerClass.id))
            close()
        }
    }

    override fun shouldPause(): Boolean = true
    override fun shouldCloseOnEsc(): Boolean = false

    private fun getColorForClass(playerClass: PlayerClass): Int {
        return when (playerClass) {
            PlayerClass.SCAVENGER -> 0xFF55FF55.toInt()
            PlayerClass.INITIATE -> 0xFF55FFFF.toInt()
            PlayerClass.VANGUARD -> 0xFFFF5555.toInt()
        }
    }

    private fun wrapText(text: String, maxWidth: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = ""

        words.forEach { word ->
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            if (textRenderer.getWidth(testLine) <= maxWidth) {
                currentLine = testLine
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine)
                }
                currentLine = word
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }
}
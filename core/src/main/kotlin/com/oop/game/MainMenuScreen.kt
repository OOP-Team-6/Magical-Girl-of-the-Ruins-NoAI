package com.oop.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class MainMenuScreen(
    screenWidth: Float,
    screenHeight: Float,
    private val game: OopGame
) : GameWorld(screenWidth, screenHeight, screenWidth, screenHeight) {

    private val backgroundTexture: Texture
    private val bannerTexture: Texture

    init {
        try {
            backgroundTexture = Texture(Gdx.files.internal("main_bg.png"))
            bannerTexture = Texture(Gdx.files.internal("banner_12.png"))
        } catch (e: Exception) {
            println("에러가 발생했습니다. 잠시 후 시도해주세요.")
            throw e
        }
    }

    override fun drawBackground(batch: SpriteBatch) {
        backgroundTexture.let {
            batch.draw(it, 0f, 0f, screenWidth, screenHeight)
        }
        bannerTexture.run {
            val bannerW = width * 0.05f
            val bannerH = height * 0.05f
            batch.draw(this, screenWidth - bannerW, screenHeight - bannerH, bannerW, bannerH)
        }
    }

    override fun update(delta: Float) {
        super.update(delta)
        if (InputHandler.isKeyJustPressed(InputHandler.SPACE) || Gdx.input.justTouched()) {
            game.showCharacterSelectScreen()
        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        drawTextOnScreen(
            "Press Any Key",
            screenWidth / 2f - 50f,
            screenHeight * 0.33f,
            Color.WHITE,
            1.2f)
    }
}
package com.oop.game.survival

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.oop.game.GameObject
import com.badlogic.gdx.graphics.g2d.SpriteBatch

// 기본 공격
class Attack(
    x: Float,
    y: Float,
    private val attackDirect: Vector2?,
    private val worldWidth: Float,
    private val worldHeight: Float
) : GameObject(x, y, 10F, 10F) {

    companion object {
        const val SPEED: Float = 400F
        const val DAMAGE = 100
    }

    private val texture = Texture(Gdx.files.internal("tile.png"))
    private var attackAlive = true

    override fun isAlive() = attackAlive
    override fun update(delta: Float) {
        if (attackDirect != null) {
            x += attackDirect.x * SPEED * delta
            y += attackDirect.y * SPEED * delta
        }

        if (x < 0 || x > worldWidth || y < 0 || y > worldHeight ) {
            attackAlive = false
        }
    }

    override fun draw(batch: SpriteBatch) {
        batch.color = Color.YELLOW
        batch.draw(texture, x, y, width, height)
    }

    override fun dispose() {
        texture.dispose()
    }
}

// 폭발 이펙트
class Explosion(
    x: Float,
    y: Float,
    val duration: Float
) : GameObject(x, y, 100F, 100F) {

    companion object {
        const val DAMAGE = 15
    }

    private var time = 0F
    private var explosionAlive = true
    private val texture = Texture(Gdx.files.internal("tile.png"))

    override fun isAlive() = explosionAlive
    override fun update(delta: Float) {
        time += delta
        if (time >= duration) {
            explosionAlive = false
        }
    }

    override fun draw(batch: SpriteBatch) {
        batch.color = Color(1f, 1f, 1f, 1f - time / duration)
        batch.draw(texture, x, y, width, height)
    }

    override fun dispose() {
        texture.dispose()
    }
}

// 잔상 이펙트
class VoidShadow (
    x: Float,
    y: Float
) : GameObject(x, y, 200F, 200F) {

    private var time = 0F
    private val duration = 2F
    private var shadowAlive = true
    private val texture = Texture(Gdx.files.internal("tile.png"))

    override fun isAlive() = shadowAlive

    override fun update(delta: Float) {
        time += delta
        if (time >= duration) {
            shadowAlive = false
        }
    }

    override fun draw(batch: SpriteBatch) {
        batch.color = Color(0.5f, 0f, 0.5f, 0.7f * (1f - time / duration))
        batch.draw(texture, x, y, width, height)
    }

    override fun dispose() {
        texture.dispose()
    }

}
package com.oop.game.survival

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.oop.game.GameObject
import com.oop.game.InputHandler

/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *  플레이어 예제 — player.png 이미지, 화살표 키로 조종.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 *  GameObject 를 상속하는 '가장 단순한' 예제다.
 *  자기 프로젝트의 Player 를 만들 때 이 파일을 통째로 복사해서
 *  texture 의 파일명을 자기 이미지로 바꾸거나,
 *  update() 에 발사 로직·특수 능력 등을 추가하면 된다.
 *
 *  핵심 포인트:
 *   ▸ Texture 는 객체가 살아있는 동안 한 번만 만들고 재사용 (생성 비용이 큼).
 *   ▸ 객체가 사라질 때 dispose() 로 GPU 자원 해제 — 기본 GameObject.dispose()를 override.
 *   ▸ batch.draw(texture, x, y, w, h) 한 줄로 이미지를 그린다.
 *
 * @param worldWidth/Height: 월드 크기를 받아 경계 밖으로 못 나가게 제한하는 용도.
 */
class Player(
    x: Float,
    y: Float,
    private val worldWidth: Float,
    private val worldHeight: Float
) : GameObject(x, y, 30f, 30f) {

    // 이미지 로딩.
    //   Gdx.files.internal: 클래스패스(자원 폴더)에서 파일을 찾아 읽는다.
    //   Texture 는 GPU 메모리에 이미지를 올린 핸들이다.
    //   src/main/resources/player.png 에 위치.
    private val texture = Texture(Gdx.files.internal("player.png"))

    private val speed = 200f

    var MAXHP = 100
        private set

    var hp = MAXHP
        private set

    var level = 1
        private set

    var exp = 0
        private set

    private val attackCoolTime = 1F
    private var attackCycle = 0F

    private val explosionCoolTime = 3F
    private var explosionCycle = 0F

    private val shadowVeilCoolTime = 10F
    private var shadowVeilCycle = 0F
    private var shadowVeilDuration = 5F
    private var shadowVeilActive = false

    private val voidShiftCoolTime = 7F
    private var voidShiftCycle = 0F

    val invincibilityCoolTime = 1F
    var invincibilityCycle = 0F
    private var invincible = false

    private val activeSkills = mutableListOf<String>()

    fun addSkill(skill: String) {
        activeSkills.add(skill);
    }

    override fun update(delta: Float) {
        if (InputHandler.isKeyPressed(InputHandler.LEFT))  x -= speed * delta
        if (InputHandler.isKeyPressed(InputHandler.RIGHT)) x += speed * delta
        if (InputHandler.isKeyPressed(InputHandler.UP))    y += speed * delta
        if (InputHandler.isKeyPressed(InputHandler.DOWN))  y -= speed * delta

        // 월드 경계 안쪽으로 가두기.
        x = x.coerceIn(0f, worldWidth - width)
        y = y.coerceIn(0f, worldHeight - height)

        attackCycle += delta

        if (attackCycle > attackCoolTime) {
            attackCycle = 0F
        }

        explosionCycle += delta

        if (explosionCycle > explosionCoolTime) {
            explosionCycle = 0F
        }

        shadowVeilCycle += delta

        if (shadowVeilCycle > shadowVeilCoolTime) {
            shadowVeilCycle = 0F
        }

        if (isShadowVeilActive()) {
            shadowVeilDuration -= delta
        }
        if (shadowVeilDuration <= 0f) {
            shadowVeilActive = false
            shadowVeilDuration = 5f
        }

        voidShiftCycle += delta

        if (voidShiftCycle > voidShiftCoolTime) {
            voidShiftCycle = 0F
        }
    }

    /**
     * 매 프레임 호출 — 자신의 이미지를 그린다.
     *
     * batch.draw(texture, x, y, w, h):
     *   왼쪽 아래 (x, y) 지점부터 (w, h) 크기로 텍스처를 늘려서 그린다.
     *   원본 이미지가 30x30 이고 w=30, h=30 이면 1:1 그대로 그려진다.
     */
    override fun draw(batch: SpriteBatch) {
        if (shadowVeilActive) {
            batch.color = Color(1f, 1f, 1f, 0.5f)
            batch.draw(texture, x, y, width, height)
        }

        else {
            batch.color = Color.GREEN
            batch.draw(texture, x, y, width, height)
        }
    }

    /** GPU 자원 정리 — 화면이 닫힐 때 GameWorld 가 호출. */
    override fun dispose() {
        texture.dispose()
    }

    fun requiredExp(currentLevel: Int): Int {
        return when (currentLevel) {
            in 1..5 -> 10
            in 6..10 -> 20
            in 11..15 -> 30
            else -> 40
        }
    }

    fun levelUp() {
        val requiredExp = requiredExp(level)
        if (exp >= requiredExp) {
            level++
            exp = 0
        }

        when (level) {
            in 1..5 -> { MAXHP += 10; hp += 10 }
            in 6..10 -> { MAXHP += 5; hp += 5 }
            in 11..15 -> { MAXHP += 3; hp += 3 }
            else -> { MAXHP += 1; hp += 1 }
        }
    }

    fun collisionEnemy() { hp -= 10 }
    fun canAttack(): Boolean =  attackCycle <= 0F
    fun canExplosion(): Boolean =  explosionCycle <= 0F
    fun canShadowVeil(): Boolean =  shadowVeilCycle <= 0F
    fun canVoidShift(): Boolean =  voidShiftCycle <= 0F
    fun useRuinFlare(): Boolean = activeSkills.contains("Ruin Flare")
    fun useShadowVeil(): Boolean = activeSkills.contains("Shadow Veil")
    fun useVoidShift(): Boolean = activeSkills.contains("Void Shift")
    fun isInvincible(): Boolean = invincible
    fun isShadowVeilActive(): Boolean = shadowVeilActive
    fun changeInvincibility() { invincible = !invincible }
    fun changeShadowVeilActivity() { shadowVeilActive = !shadowVeilActive }
    fun addExp() { exp++ }
}

package com.oop.game.survival

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.oop.game.GameObject
import com.oop.game.survival.Attack

/**
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *  적 예제 — enemy.png 이미지, 수평으로 자동 왕복 이동.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 *
 *  GameObject 를 상속해 만든 '입력 없이 스스로 움직이는' 객체 예제.
 *
 *  핵심 포인트:
 *   ▸ update() 에 입력 처리가 없다 — AI(자율 행동)는 여기서 작성.
 *   ▸ direction 이라는 '상태 변수'를 둬서 좌/우 방향 전환을 구현.
 *
 *  응용 아이디어:
 *   ▸ 생성자에서 speed 를 받아 FastEnemy, SlowEnemy 로 다양화
 *   ▸ 체력(hp)과 takeDamage() 메서드 추가
 *   ▸ 이동 패턴을 사인파, 원운동 등으로 바꾸기
 *
 * @param minX 왕복 이동의 왼쪽 한계 (보통 0f)
 * @param maxX 왕복 이동의 오른쪽 한계 (보통 worldWidth)
 */
class Enemy(
    x: Float,
    y: Float,
    private val minX: Float,
    private val maxX: Float,
    private val player: Player
) : GameObject(x, y, 40f, 40f) {

    // 이미지 로딩 — src/main/resources/enemy.png.
    private val texture = Texture(Gdx.files.internal("enemy.png"))

    private var speed = 150f

    val MAXHP = 100
    var hp = MAXHP
        private set

    override fun update(delta: Float) {
        val direction = Vector2(player.x - x, player.y - y).nor()
        x += speed * direction.x * delta
        y += speed * direction.y * delta

        if (player.isShadowVeilActive() && player.useShadowVeil()) {
            val distance = Vector2.dst(x, y, player.x, player.y)
            if (distance < 500f) {
                speed = 725F
            }
            else {
                speed = 150f
            }
        }
    }

    /**
     * 자신의 이미지를 그린다.
     *   원본은 40x40 이고 width/height 도 40 이라 1:1 로 그려진다.
     *   더 크게 보이게 하려면 width/height 를 늘리면 자동 확대된다.
     */
    override fun draw(batch: SpriteBatch) {
        batch.color = Color.RED
        batch.draw(texture, x, y, width, height)
    }

    override fun dispose() {
        texture.dispose()
    }

    fun collisionAttack() { hp -= Attack.DAMAGE }
    fun isEnemyAlive(): Boolean = hp > 0
}

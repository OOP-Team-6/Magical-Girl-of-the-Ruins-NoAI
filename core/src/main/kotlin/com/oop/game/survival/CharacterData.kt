package com.oop.game

/**
 * 상속
 *
 * 게임에서 선택할 수 있는 캐릭터들의 공통 부모 클래스.
 * MagicalGirl, Guardian, ShadowWitch 는 서로 다른 캐릭터이다
 * 이름, 설명, 최대 체력, 공격력, 속도는 각 캐릭터마다 값은 다르지만
 * 공통적으로 가지고 있어야 하는 정보이다.
 * 예시) MagicalGirl과 Guardian은 이름은 다르지만
 * name이라는 공통 변수를 가진다.
 * 각 캐릭터 클래스는 CharacterData 를 상속하고,
 * 필요한 경우 attack() 같은 동작만 다르게 바꾼다.
 *
 *  캡슐화
 *
 *  hp 는 게임 중 변하는 값이지만, 아무 곳에서나 직접 바뀌면 안 된다.
 *  그래서 private set 을 사용해 외부에서는 읽기만 가능하게 하고,
 *  체력 감소는 takeDamage() 메서드를 통해서만 일어나게 한다.
 *
 *
 */
open class CharacterData(
    val name: String,
    val description: String,
    private val maxHp : Int,
    protected val attackPower: Int,
    val speed: Int
) {

    // 현재 체력은 외부에서 읽을 수 있지만 직접 수정할 수는 없다.
    var hp: Int = maxHp
        private set

    // 체력이 0보다 크면 살아있는 상태이다.
    val isAlive: Boolean
        get() = hp > 0

    /**
     * 캐릭터가 데미지를 받았을 때 호출한다.
     * hp를 직접 수정하지 않고 이 메서드를 통해서만 줄이는 이유는
     * 체력이 음수가 되는 상황을 막기 위해서이다.
     */
    fun takeDamage(amount: Int) {
        if (amount > 0) {
            hp -= amount

            if (hp < 0) {
                hp = 0
            }
        }
    }

    /**
     * 캐릭터의 공격 데미지를 반환한다.
     * 대부분의 캐릭터는 기본 공격력을 그대로 사용한다.
     * 특별한 공격 방식을 가진 캐릭터는 이 함수를 override 해서 바꾼다.
     */
    open fun attack(): Int {
        return attackPower
    }
}


class MagicalGirl : CharacterData(
    name = "마지막 마법소녀",
    description = "폐허 속에서 살아남은 유일한 마법소녀",
    maxHp = 100,
    attackPower = 15,
    speed = 8
)

class Guardian : CharacterData(
    name = "폐허의 수호자",
    description = "높은 체력과 안정적인 방어력을 가진 캐릭터",
    maxHp = 140,
    attackPower = 10,
    speed = 6
)


class ShadowWitch : CharacterData(
    name = "그림자 마녀",
    description = "공격력과 치명타가 강력한 고위험 캐릭터",
    maxHp = 80,
    attackPower = 22,
    speed = 9
) {

    // 치명타가 발생할 확률. 20이면 20%를 의미한다.
    private val criticalChance = 20

    /**
     * 그림자 마녀의 공격 방식.
     * 부모 클래스의 기본 attack()은 attackPower를 그대로 반환하지만,
     * 그림자 마녀는 일정 확률로 두 배의 데미지를 준다.
     */
    override fun attack(): Int {

        val randomValue = (1..100).random()

        if (randomValue <= criticalChance) {
            return attackPower * 2
        }

        return attackPower
    }
}
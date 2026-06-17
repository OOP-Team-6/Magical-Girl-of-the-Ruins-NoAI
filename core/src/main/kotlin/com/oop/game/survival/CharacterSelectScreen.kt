package com.oop.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * 게임 시작 전에 플레이할 캐릭터를 고르는 화면.
 *
 * MagicalGirl, Guardian, ShadowWitch 는 서로 다른 클래스이지만
 * 모두 CharacterData 를 상속하므로 CharacterData 타입 배열에 함께 담을 수 있음
 * 그러므러 CharacterData 타입으로 묶어서 처리.
 * 주차에서 배운 상속과 다형성 위주로 사용
 *
 *  화면에서 하는 일
 *    ① 선택 가능한 캐릭터 목록을 가지고 있음
 *    ② LEFT / RIGHT 입력으로 selectedIndex 변경
 *    ③ 현재 선택된 캐릭터의 이름과 능력치 출력
 *    ④ ENTER 입력 시 선택한 캐릭터를 OopGame 에 전달
 *
 *  실제 체력, 공격력, 속도 같은 게임 규칙 데이터는 CharacterData 가 가지고 있고,
 *  이 화면은 그 데이터를 읽어서 보여주는 역할만 한다.
 *
 * @param screenWidth  화면 너비
 * @param screenHeight 화면 높이
 * @param game         화면 전환과 게임 시작을 관리하는 OopGame 객체
 */
class CharacterSelectScreen(
    screenWidth: Float,
    screenHeight: Float,
    private val game: OopGame
) : GameWorld(screenWidth, screenHeight, screenWidth, screenHeight) {

    /**
     * 선택 가능한 캐릭터 목록.
     * 배열의 타입을 CharacterData 로 둔 이유:
     * MagicalGirl, Guardian, ShadowWitch 는 모두 CharacterData 를 상속
     * 그래서 서로 다른 자식 클래스여도 하나의 배열에 함께 넣을 수 있음
     * 이 배열의 순서가 화면에서 왼쪽에서 오른쪽 가는 방향으로 볼 수 있음
     *
     * 내부에서만  character접근하도록  private를 씀
     * 바뀌지 말야하므로 val 적용
     */
    private val characters = arrayOf<CharacterData>(
        MagicalGirl(),
        Guardian(),
        ShadowWitch()
    )

    /**
     * 현재 선택된 캐릭터의 위치.
     *
     * characters 배열의 인덱스로 사용된다.
     * 예를 들어 selectedIndex 가 0이면 MagicalGirl,
     * 1이면 Guardian, 2이면 ShadowWitch 가 선택된 상태이다.
     *
     * private var 인 이유:
     *  선택 번호는 방향키 입력에 따라 계속 바뀌므로 var 가 필요하다.
     *  하지만 이 값은 선택 화면 내부에서만 관리해야 하므로 private 으로 숨긴다.
     */
    private var selectedIndex = 0

    /**
     * Enter 입력 중복 처리를 막기 위한 값.
     *
     * update() 는 매 프레임 호출되기 때문에 Enter 키가 길게 눌리면
     * 게임 시작 코드가 여러 번 실행될 수 있다.
     * 이를 막기 위해 한 번 시작한 뒤에는 true 로 바꾼다.
     */
    private var selectionConfirmed = false

    /**
     * 매 프레임 호출되어 입력을 처리한다.
     *
     * delta 는 이전 프레임과 현재 프레임 사이의 시간이다.
     * 이 화면에서는 이동 애니메이션을 만들지 않고 키 입력만 확인하므로
     * delta 값을 직접 사용하지는 않는다.
     */
    override fun update(delta: Float) {

        // 왼쪽 방향키를 누르면 이전 캐릭터로 이동한다.
        if (InputHandler.isKeyJustPressed(InputHandler.LEFT)) {
            selectedIndex--

            // 첫 번째 캐릭터에서 왼쪽으로 가면 마지막 캐릭터로 이동한다.
            if (selectedIndex < 0) {
                selectedIndex = characters.size - 1
            }
        }

        // 오른쪽 방향키를 누르면 다음 캐릭터로 이동한다.
        if (InputHandler.isKeyJustPressed(InputHandler.RIGHT)) {
            selectedIndex++

            // 마지막 캐릭터 다음에는 다시 첫 번째 캐릭터로 돌아간다.
            if (selectedIndex >= characters.size) {
                selectedIndex = 0
            }
        }

        // Enter 를 누르면 현재 선택된 캐릭터로 게임을 시작한다.
        if (!selectionConfirmed && InputHandler.isKeyJustPressed(InputHandler.ENTER)) {
            selectionConfirmed = true

            val selectedCharacter = characters[selectedIndex]

            println("선택한 캐릭터: ${selectedCharacter.name}")

            // 선택된 캐릭터를 OopGame 에 넘긴다.
            game.startGame(selectedCharacter)
        }
    }

    /**
     * 선택 화면의 배경을 그리는 부분
     * 이 화면은 따로 배경 이미지를 사용하지 않는다.
     * GameWorld 가 화면을 검은색으로 지우기 때문에,
     * 여기서는 아무것도 그리지 않고 그대로 둔다.
     */
    override fun drawBackground(batch: SpriteBatch) {
    }

    /**
     * 캐릭터 선택 화면을 그린다.
     *
     * GameWorld 의 render() 를 먼저 호출해서
     * 화면 clear, update, drawBackground 같은 기본 흐름을 실행한 뒤,
     * 이 화면에 필요한 텍스트를 추가로 출력한다.
     */
    override fun render(delta: Float) {
        super.render(delta)

        val selectedCharacter = characters[selectedIndex]
        val centerX = screenWidth / 2f

        drawTextOnScreen(
            "SELECT CHARACTER",
            centerX - 130f,
            screenHeight - 100f,
            Color.YELLOW,
            1.8f
        )

        drawTextOnScreen(
            "< ${selectedCharacter.name} >",
            centerX - 150f,
            screenHeight - 210f,
            Color.WHITE,
            1.4f
        )

        drawTextOnScreen(
            selectedCharacter.description,
            centerX - 200f,
            screenHeight - 260f,
            Color.LIGHT_GRAY,
            1f
        )

        drawTextOnScreen(
            "HP: ${selectedCharacter.hp}",
            centerX - 90f,
            screenHeight - 350f,
            Color.WHITE,
            1.2f
        )

        drawTextOnScreen(
            "ATK: ${selectedCharacter.attack()}",
            centerX - 90f,
            screenHeight - 385f,
            Color.WHITE,
            1.2f
        )

        drawTextOnScreen(
            "SPEED: ${selectedCharacter.speed}",
            centerX - 90f,
            screenHeight - 420f,
            Color.WHITE,
            1.2f
        )

        drawTextOnScreen(
            "LEFT / RIGHT: Change",
            centerX - 100f,
            100f,
            Color.CYAN,
            1f
        )

        drawTextOnScreen(
            "ENTER: Start Game",
            centerX - 85f,
            65f,
            Color.CYAN,
            1f
        )
    }
}
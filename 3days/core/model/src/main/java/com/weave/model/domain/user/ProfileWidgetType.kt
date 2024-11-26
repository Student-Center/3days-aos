package com.weave.model.domain.user

enum class ProfileWidgetType(
    val value: String,
    val title: String,
    val titleWithoutEmoji: String,
    val text: String,
    val color: WidgetColor
) {
    HOBBY(
        "HOBBY",
        "취미\uD83C\uDFC3",
        "취미",
        "테니스랑 헬스 즐겨해요! 같이 하실 분?",
        WidgetColor.BLUE
    ),
    STYLE(
        "STYLE",
        "스타일\uD83D\uDC56",
        "스타일",
        "옷은 깔끔하게 흰 티에 청바지만 입는 게 진리입니다",
        WidgetColor.YELLOW
    ),
    MBTI(
        "MBTI",
        "MBTI\uD83D\uDCAD",
        "MBTI",
        "저는 INTP지만 연애할 때는 F 100%가 된답니다",
        WidgetColor.RED
    ),
    MUSIC(
        "MUSIC",
        "음악\uD83C\uDFA7",
        "음악",
        "Fly to me the moon이 제 인생곡이에요!",
        WidgetColor.GRAY
    ),
    BODY_TYPE(
        "BODY_TYPE",
        "키·체형\uD83D\uDCAA",
        "키·체형",
        "키는 180이구 헬스 하면서 어깨 키우고 있어요☺\uFE0F",
        WidgetColor.GREEN
    ),
    FOOD(
        "FOOD",
        "음식\uD83C\uDF54",
        "음식",
        "음식 가리는 거 없이 거의 다 잘 먹어요!",
        WidgetColor.BLUE
    ),
    MOVIE(
        "MOVIE",
        "영화\uD83C\uDFAC",
        "영화",
        "제 인생 영화는 비긴 어게인이에용",
        WidgetColor.YELLOW
    ),
    DRAMA(
        "DRAMA",
        "드라마\uD83D\uDCFA",
        "드라마",
        "하츠코이, 언내추럴 같은 일드 취향\uD83D\uDC40",
        WidgetColor.RED
    ),
    BOOK(
        "BOOK",
        "책\uD83D\uDCDA",
        "책",
        "IT 관련 서적이나 자기계발서 위주로 봐요",
        WidgetColor.GRAY
    ),
    TRAVEL(
        "TRAVEL",
        "여행✈\uFE0F",
        "여행",
        "아이슬란드처럼 대자연의 낭만이 있는 곳으로 가보고 싶어요..",
        WidgetColor.GREEN
    ),
    DRINKING(
        "DRINKING",
        "술\uD83C\uDF77",
        "술",
        "화이트 와인, 하이볼, 칵테일을 좋아하는 술찌입니다\uD83D\uDE07",
        WidgetColor.BLUE
    ),
    MARRIAGE(
        "MARRIAGE",
        "결혼관\uD83D\uDC8D",
        "결혼관",
        "마음만 맞으면 결혼 자금이나 시기는 조율할 수 있다고 생각해요!",
        WidgetColor.YELLOW
    ),
    RELIGION(
        "RELIGION",
        "종교⛪",
        "종교",
        "저와 우리 집안 모두 무교입니다!",
        WidgetColor.RED
    ),
    SMOKING(
        "SMOKING",
        "흡연\uD83D\uDEAC",
        "흡연",
        "Fly to me the moon이 제 인생곡이에요!",
        WidgetColor.GRAY
    );

    fun getExample(): String = "ex.\n$text"
}

enum class WidgetColor(val textColor: Long, val containerColor: List<Long>) {
    BLUE(0xFF15394B, listOf(0xFFEDF7FF, 0xFFCDE8FF)),
    YELLOW(0xFF4C3B1C, listOf(0xFFFAF3E5, 0xFFEEDCB9)),
    RED(0xFF6C324A, listOf(0xFFFEF4F4, 0xFFEFD6E1)),
    GRAY(0xFF454545, listOf(0xFFF9F9F9, 0xFFE7E7E7)),
    GREEN(0xFF1D5018, listOf(0xFFF2FCEB, 0xFFD7E9C8))
}
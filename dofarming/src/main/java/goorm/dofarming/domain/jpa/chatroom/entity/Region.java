package goorm.dofarming.domain.jpa.chatroom.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Region {

    SEOUL("서울특별시", ""),
    INCHEON("인천광역시", ""),
    ULSAN("울산광역시", ""),
    BUSAN("부산광역시", ""),
    GWANGJU("광주광역시", ""),
    DAEJEON("대전광역시", ""),
    GYEONGGI("경기도", ""),
    CHUNGNAM("충청남도", ""),
    CHUNGBUK("충청북도", ""),
    GYEONGNAM("경상남도", ""),
    GYEONGBUK("경상북도", ""),
    JEONNAM("전라남도", ""),
    JEONBUK("전라북도", ""),
    JEJU("제주특별자치도", ""),
    GANGWON("강원특별자치도", "");


    private final String name;
    private final String imageUrl;
}

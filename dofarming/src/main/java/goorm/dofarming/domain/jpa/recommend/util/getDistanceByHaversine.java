package goorm.dofarming.domain.jpa.recommend.util;

public class getDistanceByHaversine {

    private static final double EARTH_RADIUS = 6371; // km

    // 두 지점 간의 거리를 계산하는 함수 (미터 단위로 반환)
    public static double calculateDistance(double pingX, double pingY, double mapX, double mapY) {
        double dLat = Math.toRadians(mapY - pingY);
        double dLon = Math.toRadians(mapX - pingX);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(pingY)) * Math.cos(Math.toRadians(mapY))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c * 1000; // 거리(m)
    }

    // 두 지점 간의 거리가 3100m 이내인지 확인하는 함수
    public static boolean isWithinDistance(double pingX, double pingY, double mapX, double mapY) {
        return calculateDistance(pingX, pingY, mapX, mapY) <= RecommendConfig.firstRadius;
    }

}

package goorm.dofarming.infra.tourapi.service;

import goorm.dofarming.infra.tourapi.domain.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DownloadApiService {

    private final BasicDownloadApiService basicApiService;

    List<String> RESTAURANT_CODE_LIST_CAT3 = Arrays.asList("A05020200", "A05020300", "A05020400", "A05020700", "A05020100");
    List<String> CAFE_CODE_LIST = Arrays.asList("A05020900");
    List<String> MOUNTAIN_CODE_LIST = Arrays.asList("A01010400");
    List<String> OCEAN_CODE_LIST = Arrays.asList("A01011200");
    List<String> TOURIST_ATTRACTIONS_CODE_LIST = Arrays.asList(
            "A01010100", "A01010200", "A01010300", "A01010500", "A01010600", "A01010700", "A01010800",
            "A01010900", "A01011100", "A01011300", "A01011400", "A01011600", "A01011700", "A01011900",
            "A01020200", "A02010100", "A02010200", "A02010300", "A02010400", "A02010500", "A02010600",
            "A02010700", "A02010800", "A02010900", "A02011000", "A02020200", "A02020300", "A02020600",
            "A02030100", "A02030200", "A02030300", "A02030400", "A02030600", "A02050200", "A02050600",
            "A02060100", "A02060200", "A02060300", "A02060400", "A02060500", "A02061100", "A02070100",
            "A02070200", "A02080100", "A02080500", "A02080600");
    List<String> ACTIVITY_CODE_LIST = Arrays.asList(
            "A02020400", "A02020800", "A02030100", "A02030200", "A02030300", "A02030400", "A03010200",
            "A03010300", "A03020200", "A03020300", "A03020400", "A03020500", "A03020600", "A03020800",
            "A03020900", "A03021000", "A03021100", "A03021200", "A03021300", "A03021400", "A03021500",
            "A03021600", "A03021700", "A03021800", "A03022000", "A03022100", "A03022200", "A03022300",
            "A03022400", "A03022600", "A03022700", "A03030100", "A03030200", "A03030300", "A03030400",
            "A03030500", "A03030600", "A03030700", "A03030800", "A03040100", "A03040200", "A03040300",
            "A03040400", "A03050100", "A04010700");


    public String DownloadRestaurantListByCat3() {
        return basicApiService.fetchByCategory(RESTAURANT_CODE_LIST_CAT3, DataType.Restaurant);
    }
    public String DownloadActivityListByCat3() {
        return basicApiService.fetchByCategory(ACTIVITY_CODE_LIST, DataType.Activity);
    }
    public String DownloadCafeListByCat3() {
        return basicApiService.fetchByCategory(CAFE_CODE_LIST, DataType.Cafe);
    }
    public String DownloadOceanListByCat3() {
        return basicApiService.fetchByCategory(OCEAN_CODE_LIST, DataType.Ocean);
    }
    public String DownloadTourListByCat3() {
        return basicApiService.fetchByCategory(TOURIST_ATTRACTIONS_CODE_LIST, DataType.Tour);
    }
    public String DownloadMountainListByCat3() {
        return basicApiService.fetchByCategory(MOUNTAIN_CODE_LIST, DataType.Mountain);
    }


}

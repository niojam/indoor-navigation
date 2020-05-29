package ee.wemakesoftware.navigation;

import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.repository.BaseStationRepository;
import ee.wemakesoftware.navigation.repository.MobileStationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class NavigationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavigationApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner initStations(MobileStationRepository mobileStationRepository, BaseStationRepository baseStationRepository) {
        return (args) -> {
            mobileStationRepository.saveAll(Arrays.asList(new MobileStation(), new MobileStation(),
                    new MobileStation(),
                    new MobileStation(),
                    new MobileStation(),
                    new MobileStation(),
                    new MobileStation()));
            baseStationRepository.saveAll(Arrays.asList(new BaseStation("A", 4f, 8f, 10f),
                    new BaseStation("B", 11f, 7f, 10f),
                    new BaseStation("C", 16f, 17f, 10f),
                    new BaseStation("D", 1.5f, 2.3f, 10f)));
        };
    }

}

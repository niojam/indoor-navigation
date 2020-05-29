package ee.wemakesoftware.navigation.repository;

import ee.wemakesoftware.navigation.model.BaseStation;
import ee.wemakesoftware.navigation.model.MobileStation;
import ee.wemakesoftware.navigation.model.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

    List<Report> findTop3ByMobileStationOrderByTimeDesc(MobileStation mobileStation);

    Optional<Report> findByBaseStationAndMobileStation(BaseStation baseStation, MobileStation mobileStation);
}

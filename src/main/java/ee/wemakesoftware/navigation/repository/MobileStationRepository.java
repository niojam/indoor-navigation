package ee.wemakesoftware.navigation.repository;

import ee.wemakesoftware.navigation.model.MobileStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MobileStationRepository extends CrudRepository<MobileStation, UUID> {

}

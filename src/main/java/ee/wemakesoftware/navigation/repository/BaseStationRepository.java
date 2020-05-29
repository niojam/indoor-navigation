package ee.wemakesoftware.navigation.repository;

import ee.wemakesoftware.navigation.model.BaseStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaseStationRepository extends CrudRepository<BaseStation, UUID> {

}

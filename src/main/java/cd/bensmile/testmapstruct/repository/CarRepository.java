package cd.bensmile.testmapstruct.repository;

import cd.bensmile.testmapstruct.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}

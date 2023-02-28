package cd.bensmile.testmapstruct.services;

import cd.bensmile.testmapstruct.models.Car;
import cd.bensmile.testmapstruct.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    @Async
    public CompletableFuture<List<Car>> saveCars(final InputStream inputStream) throws Exception {
        final long start = System.currentTimeMillis();
        List<Car> cars = parseCSVFile(inputStream);
        log.info("Saving a list of cars of size {} records", cars.size());
        cars = carRepository.saveAll(cars);
        log.info("Elapsed time (milliseconds): {}", (System.currentTimeMillis() - start));
        return CompletableFuture.completedFuture(cars);
    }

    private List<Car> parseCSVFile(final InputStream inputStream) throws Exception {
        final List<Car> cars = new ArrayList<>();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] data = line.split(",");
                final Car car = new Car();
                car.setManufacture(data[0]);
                car.setModel(data[1]);
                car.setType(data[2]);
                cars.add(car);
            }
            return cars;
        } catch (final IOException e) {
            log.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

    @Async
    public CompletableFuture<List<Car>> getAllCars() throws Exception {
        log.info("Request to get a list of cars");
        final List<Car> cars = carRepository.findAll();
        Thread.sleep(1000);
        return CompletableFuture.completedFuture(cars);
    }

}

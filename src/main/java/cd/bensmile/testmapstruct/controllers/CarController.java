package cd.bensmile.testmapstruct.controllers;

import cd.bensmile.testmapstruct.models.Car;
import cd.bensmile.testmapstruct.services.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Slf4j
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam(value = "files") MultipartFile[] files) {
        try {
            for (final MultipartFile file : files) {
                carService.saveCars(file.getInputStream());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity> getAllCars() throws Exception {
        return carService.getAllCars().<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetFailure);
    }

    @GetMapping(path = "/all",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getAllCars2() {
        try {

            CompletableFuture<List<Car>> allCars1 = carService.getAllCars();
            CompletableFuture<List<Car>> allCars2 = carService.getAllCars();
            CompletableFuture<List<Car>> allCars3 = carService.getAllCars();

            CompletableFuture.allOf(allCars3, allCars1, allCars2).join();

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    private static Function<Throwable, ResponseEntity< ? extends List<Car>>> handleGetFailure = throwable -> {
        log.error("Failed to read records : {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };


}

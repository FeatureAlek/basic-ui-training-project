package lv.bootcamp.shelter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lv.bootcamp.shelter.dto.AnimalCreateRequest;
import lv.bootcamp.shelter.dto.AnimalResponse;
import lv.bootcamp.shelter.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for shelter animal endpoints.
 * Returns JSON — does not render HTML pages.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/animals")
@Tag(name = "Animals", description = "Endpoints for browsing, creating, and adopting shelter animals")
public class AnimalApiController {

    private final AnimalService animalService;

    @Operation(summary = "List all animals", description = "Returns all animals currently in the shelter.")
    @ApiResponse(responseCode = "200", description = "List of animals returned successfully")
    @GetMapping
    public List<AnimalResponse> findAll() {
        return animalService.findAll();
    }

    @Operation(summary = "Get animal by ID", description = "Returns a single animal by its ID.")
    @ApiResponse(responseCode = "200", description = "Animal found")
    @ApiResponse(responseCode = "404", description = "Animal not found")
    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> findById(@PathVariable Long id) {
        return animalService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "List adopted animals",
            description = "Restricted to ROLE_ADMIN. Read-only endpoint, safe to call repeatedly for testing role-based authorization."
    )
    @ApiResponse(responseCode = "200", description = "List of adopted animals returned successfully")
    @ApiResponse(responseCode = "403", description = "Caller does not have ROLE_ADMIN")
    @GetMapping("/adopted")
    public List<AnimalResponse> findAdopted() {
        return animalService.findAdopted();
    }

    @Operation(
            summary = "Create a new animal",
            description = "Restricted to ROLE_ADMIN."
    )
    @ApiResponse(responseCode = "201", description = "Animal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request body")
    @ApiResponse(responseCode = "403", description = "Caller does not have ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalResponse create(@RequestBody @Valid AnimalCreateRequest request) {
        return animalService.create(request);
    }

    @Operation(
            summary = "Adopt an animal",
            description = "Adopts an animal as the currently logged-in user. Restricted to ROLE_USER."
    )
    @ApiResponse(responseCode = "200", description = "Animal adopted successfully")
    @ApiResponse(responseCode = "404", description = "Animal not found")
    @ApiResponse(responseCode = "409", description = "Animal is already adopted")
    @PostMapping("/{id}/adopt")
    public ResponseEntity<AnimalResponse> adopt(@PathVariable Long id, Authentication authentication) {
        return animalService.adopt(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleAlreadyAdopted(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
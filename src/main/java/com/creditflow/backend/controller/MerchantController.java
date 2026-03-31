package com.creditflow.backend.controller;

import com.creditflow.backend.entity.Merchant;
import com.creditflow.backend.repository.MerchantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody combined
// It means: this class handles HTTP requests AND returns JSON directly
// (not HTML pages — that would be just @Controller)
@RestController

// @RequestMapping sets the base URL for all endpoints in this class
// Every method here will start with /api/merchants
@RequestMapping("/api/merchants")
public class MerchantController {

    // We declare the repository as a field
    // We DON'T use @Autowired on the field directly — that's old style
    // Constructor injection (below) is the modern, preferred way
    private final MerchantRepository merchantRepository;

    // Constructor injection — Spring sees this constructor and automatically
    // passes in the MerchantRepository instance it already created
    // This is better than @Autowired on the field because:
    // 1. The field can be "final" — meaning it can never be accidentally reassigned
    // 2. It's easier to test — you can pass a mock repository in tests
    public MerchantController(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    // @GetMapping means: when someone sends GET /api/merchants, run this method
    // ResponseEntity lets us control exactly what HTTP status code we return
    // List<Merchant> means we're returning multiple merchants as a JSON array
    @GetMapping
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        List<Merchant> merchants = merchantRepository.findAll();
        return ResponseEntity.ok(merchants); // 200 OK + the list as JSON
    }

    // @PostMapping means: when someone sends POST /api/merchants, run this method
    // @RequestBody means: take the JSON from the request body and convert it to a Merchant object
    @PostMapping
    public ResponseEntity<?> createMerchant(@RequestBody Merchant merchant) {

        // Check if a merchant with this email already exists
        // This is our first real business logic check
        if (merchantRepository.existsByEmail(merchant.getEmail())) {
            // 409 Conflict — the standard HTTP code for "this already exists"
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A merchant with this email already exists");
        }

        // Save to database — JPA handles the INSERT SQL for us
        Merchant saved = merchantRepository.save(merchant);

        // 201 Created — the correct HTTP code for a successful resource creation
        // (not 200 OK — that's for reads. 201 specifically means "new thing was made")
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
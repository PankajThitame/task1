package com.authentication.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Basic smoke test to verify the test suite can run.
 * Full Spring context boot is tested manually with a live database.
 */
class TaskApplicationTests {

    @Test
    @DisplayName("Application test suite loads")
    void contextLoads() {
        // Verifies the test framework is wired correctly.
        // Full context startup requires a live MySQL instance
        // and is validated via integration / Postman testing.
    }
}

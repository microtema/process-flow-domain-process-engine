package it.de.microtema;

import de.microtema.ProcessEngineApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProcessEngineApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProcessEngineApplicationIT {

    @Test
    void loadContext() {
        assertTrue(true);
    }
}

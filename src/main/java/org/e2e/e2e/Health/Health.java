package org.e2e.e2e.Health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Health {

        @RequestMapping("/health")
        public String health() {
            return "OK";
        }
}

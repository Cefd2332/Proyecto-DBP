package org.e2e.e2e.Health;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Health {

        @RequestMapping()
        public String health() {
            return "OK";
        }
}

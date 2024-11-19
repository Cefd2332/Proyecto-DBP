// src/main/java/org/e2e/e2e/Usuario/AutenticacionResponseDto.java
package org.e2e.e2e.Usuario;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutenticacionResponseDto {

    @JsonProperty("userId")
    private Long userId;

    // Constructor
    public AutenticacionResponseDto(Long userId) {
        this.userId = userId;
    }

    // Getter
    public Long getUserId() {
        return userId;
    }

    // Setter
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

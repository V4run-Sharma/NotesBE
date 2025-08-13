package org.vs.notesbe.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.vs.notesbe.dto.ApiResponse;

@Component
public class UserValidations {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserValidations.class);
    private final JwtUtils jwtUtils;

    public UserValidations(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public <T> ResponseEntity<ApiResponse<T>> validateUserToken(String token, String uIdString) {
        if (!jwtUtils.validateToken(token)) {
            log.error("AUTH ::: Invalid JWT token");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid JWT token", null));
        }

        if (uIdString == null) {
            log.error("AUTH ::: User ID not found in JWT token");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not found.", null));
        }
        return null;
    }
}

package com.psl.product.service.Payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRespons {
    private String message;
    private boolean success;
    private HttpStatus status;
}

package com.solenia.auth_service.infrastructure.security;

import com.solenia.auth_service.adapter.web.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityErrorWriter {

    private final JsonMapper jsonMapper;

    public void write(
            HttpServletResponse response,
            int status,
            String code,
            String message
    )throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (status == 401){response.setHeader("WWW-Authenticate", "Bearer");}

        var error = new ErrorResponse(LocalDateTime.now(),status,code,message);

        response.getWriter().write(jsonMapper.writeValueAsString(error));
    }
}

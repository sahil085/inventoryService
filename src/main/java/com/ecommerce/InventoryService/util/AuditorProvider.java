package com.ecommerce.InventoryService.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorProvider implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        String user = UserContext.getUser();

        if (user == null || user.isBlank()) {
            return Optional.of("SYSTEM"); // fallback
        }

        return Optional.of(user);
    }
}
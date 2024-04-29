package learn.task5.dto;

import jakarta.validation.constraints.NotBlank;

public record AccDto(
        Integer id,
        Integer account_pool_id,
        String account_number,
        Boolean bussy
        ) {
}

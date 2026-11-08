package com.example.GameDepotAPI.dto.item_carrito;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModificarCantidadItemDTO {
    @NotNull(message = "La cantidad es obligatoria.")
    @Max(value = 99, message = "La cantidad máxima permitida es 99.")
    private Integer cantidad;
}

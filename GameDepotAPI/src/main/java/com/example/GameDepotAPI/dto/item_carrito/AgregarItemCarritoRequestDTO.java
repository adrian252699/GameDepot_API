package com.example.GameDepotAPI.dto.item_carrito;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgregarItemCarritoRequestDTO {
    @NotNull(message = "El videojuego es obligatorio.")
    @Positive(message = "El ID del videojuego debe ser positivo.")
    private Long videojuegoId;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(
            value = 1,
            message = "La cantidad mínima es 1."
    )
    @Max(
            value = 99,
            message = "La cantidad máxima permitida es 99."
    )
    private Integer cantidad;
}

package com.example.GameDepotAPI.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearPagoRequestDTO {
    private Long ordenId;
}

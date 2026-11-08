package com.example.GameDepotAPI.exception;

import com.example.GameDepotAPI.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> manejarErrorDeValidacion(MethodArgumentNotValidException ex){
        List<String> details = new ArrayList<>();

        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getDefaultMessage());
        }

        ErrorDTO errorDTO = new ErrorDTO(
                "Error en la validacion de datos enviados",
                details,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorDTO> manejarErrorDeNegocio(
            NegocioException ex
    ) {
        List<String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ErrorDTO errorDTO = new ErrorDTO(
                "Error en la validacion de negocio",
                details,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorDTO> manejarErrorDeRecurso(
            RecursoNoEncontradoException ex
    ) {
        List<String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ErrorDTO errorDTO = new ErrorDTO(
                "Recurso no encontrado",
                details,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarErrorDeServidor(
            RecursoNoEncontradoException ex
    ) {
        List<String> details = new ArrayList<>();

        details.add(ex.getMessage());

        ErrorDTO errorDTO = new ErrorDTO(
                "Error del programador",
                details,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

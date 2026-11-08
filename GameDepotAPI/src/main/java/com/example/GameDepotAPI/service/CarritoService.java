package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.dto.carrito.CarritoResponseDTO;
import com.example.GameDepotAPI.dto.item_carrito.AgregarItemCarritoRequestDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.carrito.Carrito;
import com.example.GameDepotAPI.model.carrito.ItemCarrito;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import com.example.GameDepotAPI.repository.CarritoRepository;
import com.example.GameDepotAPI.repository.ItemCarritoRepository;
import com.example.GameDepotAPI.repository.VideojuegoRepository;
import com.example.GameDepotAPI.service.mappers.CarritoMapper;
import com.example.GameDepotAPI.service.mappers.UsuarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CarritoService {
    @Autowired
    private VideojuegoRepository  videojuegoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public CarritoResponseDTO obtenerCarrito(Long id){
        MostrarUsuarioDTO usuarioDTO = usuarioService.obtenerPorId(id);
        validarUsuarioAutenticado(usuarioDTO);

        Carrito carritoDetalle = carritoRepository.findDetalleByUsuarioId(usuarioDTO.getId())
                .orElseThrow(
                ()-> new RecursoNoEncontradoException("Carrito no encontrado")
        );

        return CarritoMapper.toResponseDTO(carritoDetalle);
    }

    @Transactional
    public CarritoResponseDTO agregarVideojuego(MostrarUsuarioDTO usuarioDTO, AgregarItemCarritoRequestDTO requestDTO){
        validarUsuarioAutenticado(usuarioDTO);

        Videojuego videojuego = videojuegoRepository.findById(requestDTO.getVideojuegoId())
                .orElseThrow(()->
                        new RecursoNoEncontradoException("El Videojuego no existe")
                );

        validarVideojuegoDisponible(videojuego, requestDTO.getCantidad());

        Carrito carrito = generarCarrito(usuarioDTO);

        ItemCarrito itemExistente = itemCarritoRepository.findByCarritoIdAndVideojuegoId(carrito.getId(), videojuego.getId())
                .orElse(null);

        if(itemExistente != null){
            //Significa que el item ya estaba en el carrito y se agregara otra cantidad al carrito
            Integer nuevaCantidad = itemExistente.getCantidad() + requestDTO.getCantidad();

            validarVideojuegoDisponible(videojuego, nuevaCantidad);

            itemExistente.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(itemExistente);
        }else{
            //El item no existe en el carrito y se va a agregar
            ItemCarrito nuevoItem = new ItemCarrito();

            nuevoItem.setCarrito(carrito);
            nuevoItem.setVideojuego(videojuego);
            nuevoItem.setCantidad(requestDTO.getCantidad());

            itemCarritoRepository.save(nuevoItem);
        }

        return obtenerCarritoActualizado(usuarioDTO.getId());
    }

    @Transactional
    public CarritoResponseDTO actualizarCantidad(MostrarUsuarioDTO usuario, Long itemId, Integer cantidad) {
        validarUsuarioAutenticado(usuario);
        validarItemId(itemId);

        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoUsuarioId(itemId, usuario.getId()).orElseThrow(() ->
                new RecursoNoEncontradoException(
                        "No se encontró el artículo en tu carrito."
                )
        );

        Videojuego videojuego = item.getVideojuego();

        validarVideojuegoDisponible(videojuego, cantidad);

        Integer nuevaCantidad = item.getCantidad() + cantidad;

        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);

        return obtenerCarritoActualizado(usuario.getId());
    }

    @Transactional
    public CarritoResponseDTO eliminarItem(MostrarUsuarioDTO usuarioDTO, Long itemId){
        validarUsuarioAutenticado(usuarioDTO);
        validarItemId(itemId);

        ItemCarrito item = itemCarritoRepository.findByIdAndCarritoUsuarioId(itemId, usuarioDTO.getId()).orElseThrow(() ->
                new RecursoNoEncontradoException(
                        "No se encontró el artículo en tu carrito."
                )
        );

        itemCarritoRepository.delete(item);

        return obtenerCarritoActualizado(usuarioDTO.getId());
    }

    /*Metodos auxiliares*/

    private void validarUsuarioAutenticado(MostrarUsuarioDTO usuarioDTO){
        if (usuarioDTO == null || usuarioDTO.getId() == null) {
            throw new NegocioException(
                    "No fue posible identificar al usuario autenticado."
            );
        }
    }

    private Carrito generarCarrito(MostrarUsuarioDTO usuarioDTO){
        // Si el usurio tiene un carrito simplemente lo retorna si no crea uno y lo retorna
        return carritoRepository.findByUsuarioId(usuarioDTO.getId()).orElseGet(() -> {
            Carrito carrito = new Carrito();

            carrito.setUsuario(UsuarioMapper.toUsuarioEntity(usuarioDTO));
            carrito.setItems(new ArrayList<>());

            return carritoRepository.save(carrito);
        });
    }

    private void validarVideojuegoDisponible(Videojuego videojuego, Integer cantidad){
        if (!videojuego.getActivo()) {
            throw new NegocioException(
                    "El videojuego no está disponible para la venta."
            );
        }

        if (videojuego.getStock() <= 0) {
            throw new NegocioException(
                    "El videojuego no tiene existencias disponibles."
            );
        }

        if (cantidad > videojuego.getStock()) {
            throw new NegocioException(
                    "La cantidad solicitada supera el stock disponible. " + "Existencias actuales: " + videojuego.getStock() + "."
            );
        }
    }

    private CarritoResponseDTO obtenerCarritoActualizado(Long usuarioId){
        Carrito carrito = carritoRepository.findDetalleByUsuarioId(usuarioId).orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el carrito del usuario."
                )
        );

        return CarritoMapper.toResponseDTO(carrito);
    }

    private void validarItemId(Long itemId) {
        if (itemId == null || itemId <= 0) {
            throw new IllegalArgumentException(
                    "El ID del item debe ser positivo."
            );
        }
    }
}

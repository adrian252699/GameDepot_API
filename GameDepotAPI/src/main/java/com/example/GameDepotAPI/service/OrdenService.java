package com.example.GameDepotAPI.service;

import com.example.GameDepotAPI.dto.PaginacionDTO;
import com.example.GameDepotAPI.dto.enums.EstadoOrden;
import com.example.GameDepotAPI.dto.enums.EstadoPago;
import com.example.GameDepotAPI.dto.orden.response.OrdenCreadaResponseDTO;
import com.example.GameDepotAPI.dto.orden.response.OrdenDetalleResponseDTO;
import com.example.GameDepotAPI.dto.orden.response.OrdenResumenResponseDTO;
import com.example.GameDepotAPI.dto.usuario.MostrarUsuarioDTO;
import com.example.GameDepotAPI.exception.NegocioException;
import com.example.GameDepotAPI.exception.RecursoNoEncontradoException;
import com.example.GameDepotAPI.model.carrito.Carrito;
import com.example.GameDepotAPI.model.carrito.ItemCarrito;
import com.example.GameDepotAPI.model.orden.ItemOrden;
import com.example.GameDepotAPI.model.orden.Orden;
import com.example.GameDepotAPI.model.videojuego.Videojuego;
import com.example.GameDepotAPI.repository.CarritoRepository;
import com.example.GameDepotAPI.repository.ItemCarritoRepository;
import com.example.GameDepotAPI.repository.OrdenRepository;
import com.example.GameDepotAPI.repository.VideojuegoRepository;
import com.example.GameDepotAPI.service.mappers.OrdenMapper;
import com.example.GameDepotAPI.service.mappers.UsuarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private VideojuegoRepository  videojuegoRepository;

    @Transactional
    public OrdenCreadaResponseDTO crearOrden(MostrarUsuarioDTO usuarioDTO) {
        validarUsuarioAutenticado(usuarioDTO);

        Carrito carrito = carritoRepository.findDetalleByUsuarioId(usuarioDTO.getId())
                .orElseThrow(()-> new NegocioException("Carrito no encontrado"));

        validarCarrito(carrito);

        Orden orden = crearOrdenBase(usuarioDTO);

        orden.setEstadoPago(EstadoPago.PENDIENTE);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (ItemCarrito itemCarrito : carrito.getItems()) {

            Videojuego videojuego = videojuegoRepository.findByIdForUpdate(
                            itemCarrito.getVideojuego().getId()
                    )
                    .orElseThrow(() ->
                            new RecursoNoEncontradoException(
                                    "Uno de los productos del carrito ya no existe."
                            )
                    );

            validarVideojuego(
                    videojuego,
                    itemCarrito.getCantidad()
            );

            ItemOrden itemOrden = new ItemOrden();

            itemOrden.setVideojuego(videojuego);
            itemOrden.setCantidad(itemCarrito.getCantidad());
            itemOrden.setPrecioUnitario(videojuego.getPrecio());
            itemOrden.setDescuento(BigDecimal.ZERO);

            orden.agregarItem(itemOrden);

            BigDecimal subtotalItem =
                    videojuego.getPrecio().multiply(
                            BigDecimal.valueOf(
                                    itemCarrito.getCantidad()
                            )
                    );

            subtotal = subtotal.add(subtotalItem);

            BigDecimal total = subtotal.add(itemOrden.getDescuento());

            orden.setTotal(total);

            descontarStock(
                    videojuego,
                    itemCarrito.getCantidad()
            );
        }





        Orden ordenGuardada = ordenRepository.save(orden);

        vaciarCarrito(carrito);

        return OrdenMapper.toCreadaResponseDTO(ordenGuardada);
    }

    public OrdenCreadaResponseDTO obtenerOrdenPendientePago(Long usuarioId) {
        Orden orden = ordenRepository.findFirstByUsuario_IdAndEstadoPagoOrderByFechaDesc(usuarioId, EstadoPago.PENDIENTE).orElseThrow(
                                () -> new RuntimeException("No tienes órdenes pendientes de pago")
        );

        return OrdenMapper.toCreadaResponseDTO(orden);
    }

    public PaginacionDTO<OrdenResumenResponseDTO> listarOrdenes(MostrarUsuarioDTO usuario, EstadoOrden estado, int pagina, int tamano) {
        validarUsuarioAutenticado(usuario);
        validarPaginacion(pagina, tamano);

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by(
                        Sort.Direction.DESC,
                        "fecha"
                )
        );

        Page<Orden> resultado;

        if (estado == null) {
            resultado = ordenRepository.findByUsuarioId(usuario.getId(), pageable);
        } else {
            resultado = ordenRepository.findByUsuarioIdAndEstado(
                    usuario.getId(),
                    estado,
                    pageable
            );
        }

        List<OrdenResumenResponseDTO> contenido =
                resultado.getContent()
                        .stream()
                        .map(
                                OrdenMapper::toResumenResponseDTO
                        )
                        .toList();

        return new PaginacionDTO<>(contenido, resultado.getNumber(), resultado.getSize(), resultado.getTotalElements(), resultado.getTotalPages(), resultado.isFirst(), resultado.isLast());
    }

    @Transactional()
    public OrdenDetalleResponseDTO obtenerDetalleOrden(MostrarUsuarioDTO usuario, Long ordenId) {
        validarUsuarioAutenticado(usuario);

        Orden orden = ordenRepository.findDetalleByIdAndUsuarioId(
                ordenId, usuario.getId())
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Orden no encontrada.")
                );

        return OrdenMapper.toDetalleResponseDTO(orden);
    }

    /*Metodos Auxiliares*/
    private void validarUsuarioAutenticado(MostrarUsuarioDTO usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new NegocioException(
                    "No fue posible identificar al usuario autenticado."
            );
        }
    }

    private void validarCarrito(Carrito carrito) {
        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new NegocioException(
                    "No puedes crear una orden con el carrito vacío."
            );
        }
    }

    private Orden crearOrdenBase(MostrarUsuarioDTO usuario) {
        Orden orden = new Orden();

        orden.setUsuario(UsuarioMapper.toUsuarioEntity(usuario));
        orden.setFolio(generarNumeroOrden());
        orden.setFecha(LocalDateTime.now());
        orden.setEstado(EstadoOrden.PENDIENTE);


        orden.setTotal(BigDecimal.ZERO);

        return orden;
    }

    private String generarNumeroOrden() {
        String folio;

        do {
            String fragmento = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            folio = "GD-" + Year.now().getValue() + "-" + fragmento;

        } while (ordenRepository.existsByFolio(folio));

        return folio;
    }

    private void validarVideojuego(Videojuego videojuego, int cantidad) {
        if (videojuego == null) {
            throw new RecursoNoEncontradoException(
                    "Uno de los videojuegos del carrito ya no existe."
            );
        }

        if (!videojuego.getActivo()) {
            throw new NegocioException(
                    "El videojuego '" + videojuego.getNombre() + "' ya no está disponible."
            );
        }

        if (cantidad <= 0) {
            throw new NegocioException(
                    "La cantidad de un videojuego del carrito no es válida."
            );
        }

        if (videojuego.getStock() < cantidad) {
            throw new NegocioException(
                    "No hay stock suficiente para el videojuego '" + videojuego.getNombre() + "'. Existencias actuales: " + videojuego.getStock() + "."
            );
        }
    }

    private void descontarStock(Videojuego videojuego, int cantidad) {
        int nuevoStock = videojuego.getStock() - cantidad;

        videojuego.setStock(nuevoStock);

        videojuegoRepository.save(videojuego);
    }

    private void vaciarCarrito(Carrito carrito) {
        List<ItemCarrito> items = List.copyOf(carrito.getItems());

        itemCarritoRepository.deleteAll(items);

        carrito.getItems().clear();
    }

    private void validarPaginacion(int pagina, int tamano) {
        if (pagina < 0) {
            throw new IllegalArgumentException(
                    "La página no puede ser negativa."
            );
        }

        if (tamano < 1 || tamano > 50) {
            throw new IllegalArgumentException(
                    "El tamaño de página debe estar entre 1 y 50."
            );
        }
    }
}

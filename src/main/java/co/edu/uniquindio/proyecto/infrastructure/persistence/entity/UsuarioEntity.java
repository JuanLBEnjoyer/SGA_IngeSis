package co.edu.uniquindio.proyecto.infrastructure.persistence.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA para persistir Usuario en H2.
 *
 * El documento (numero + tipo) actúa como identificador de negocio.
 * Usamos una clave compuesta lógica: numero_documento + tipo_documento =
 * UNIQUE.
 */
@Entity
@Table(name = "usuarios", indexes = {
        @Index(name = "idx_usuario_documento", columnList = "numero_documento, tipo_documento", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(name = "tipo_documento", nullable = false, length = 30)
    private String tipoDocumento;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private RolUsuario rol;
}

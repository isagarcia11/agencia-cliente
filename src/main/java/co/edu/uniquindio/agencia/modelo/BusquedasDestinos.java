package co.edu.uniquindio.agencia.modelo;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BusquedasDestinos implements Serializable {

    private Destino destino;

    @Override
    public String toString() {
        return "BusquedasDestinos{" +
                "destino=" + destino +
                '}';
    }
}

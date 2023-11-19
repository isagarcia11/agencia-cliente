package co.edu.uniquindio.agencia.utils;
import lombok.Getter;

import java.util.EventObject;
import java.util.Locale;

@Getter
public class CambioIdiomaEvent extends EventObject {
    private final Locale nuevoIdioma;

    public CambioIdiomaEvent(Object source, Locale nuevoIdioma) {
        super(source);
        this.nuevoIdioma = nuevoIdioma;
    }

}

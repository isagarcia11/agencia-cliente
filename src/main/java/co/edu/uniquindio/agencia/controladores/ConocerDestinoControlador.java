package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConocerDestinoControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private ImageView imageView;

    @FXML
    private Button btnAnterior, btnSiguiente, btnAtras;

    @FXML
    private TextField txtNombreDestino;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtClima;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private Destino destinoActual = agenciaCliente.getDestino();

    private ArrayList<String> rutasImagenes = new ArrayList<>();
    private final Propiedades propiedades = Propiedades.getInstance();
    private int indiceImagenActual = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mostrarDetalles();
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        txtNombreDestino.setPromptText(propiedades.getResourceBundle().getString("TextoNombreDestino"));
        txtCiudad.setPromptText(propiedades.getResourceBundle().getString("TextoCiudadDestino"));
        txtDescripcion.setPromptText(propiedades.getResourceBundle().getString("TextoDescripcionDestino"));
        txtClima.setPromptText(propiedades.getResourceBundle().getString("TextoClimaDestino"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnAnterior.setText(propiedades.getResourceBundle().getString("TextoAnterior"));
        btnSiguiente.setText(propiedades.getResourceBundle().getString("TextoSiguiente"));

    }

    public void mostrarDetalles() {
        txtNombreDestino.setText(destinoActual.getNombre());
        txtCiudad.setText(destinoActual.getCiudad());
        txtDescripcion.setText(destinoActual.getDescripcion());
        txtClima.setText(String.valueOf(destinoActual.getClima()));

        rutasImagenes = destinoActual.getImagenes();

        // Mostrar la primera imagen si hay al menos una
        if (!rutasImagenes.isEmpty()) {
            String primeraRutaImagen = rutasImagenes.get(0);
            Image imagen = new Image(primeraRutaImagen); // Cargar imagen desde la ruta
            imageView.setImage(imagen); // Mostrar la imagen en el ImageView
        }

        // Deshabilitar el botón "Anterior" si no hay imágenes anteriores
        btnAnterior.setDisable(indiceImagenActual == 0);

        // Deshabilitar el botón "Siguiente" si no hay imágenes siguientes
        btnSiguiente.setDisable(indiceImagenActual == rutasImagenes.size() - 1);
    }


    @FXML
    private void navegarAnterior() {
        if (indiceImagenActual > 0) {
            indiceImagenActual--;
            mostrarImagenActual();
        }
    }

    @FXML
    private void navegarSiguiente() {
        if (indiceImagenActual < rutasImagenes.size() - 1) {
            indiceImagenActual++;
            mostrarImagenActual();
        }
    }

    private void mostrarImagenActual() {
        String rutaImagen = rutasImagenes.get(indiceImagenActual);
        Image imagen = new Image(rutaImagen);
        imageView.setImage(imagen);

        // Actualizar estado de los botones de navegación
        btnAnterior.setDisable(indiceImagenActual == 0);
        btnSiguiente.setDisable(indiceImagenActual == rutasImagenes.size() - 1);

        System.out.println("Ruta de la imagen: " + rutaImagen);
    }
    public void atras(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicio.fxml", event);
        }
    }
}



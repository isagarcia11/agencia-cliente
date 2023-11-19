package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DetallesPaqueteControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TextField txtNombrePaquete;

    @FXML
    private TextField txtDuracion;

    @FXML
    private TextField txtServiciosAdicionales;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCupoMaximo;

    @FXML
    private ComboBox<String> cbxDestinos;

    @FXML
    private TextField txtFechaInicio;

    @FXML
    private TextField txtFechaFin;

    @FXML
    private Button btnAnterior;

    @FXML
    private Button btnSiguiente;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField txtNombreDestino;

    @FXML
    private TextField txtCiudad;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtClima;

    @FXML
    private Button btnAtras;
    @FXML
    private Button btnVer;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();
    private PaqueteTuristico paqueteTuristico;

    private ArrayList<String> rutasImagenes = new ArrayList<>();
    private int indiceImagenActual = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();

        setPaqueteTuristico(paqueteTuristico);
        ArrayList<Destino> destinos = paqueteTuristico.getDestinos();
        ObservableList<String> nombresDestinos = FXCollections.observableArrayList();

        for (Destino destino : destinos) {
            nombresDestinos.add(destino.getNombre());
        }

        cbxDestinos.setItems(nombresDestinos);
    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        txtNombrePaquete.setPromptText(propiedades.getResourceBundle().getString("TextoNombrePaquete"));
        btnVer.setText(propiedades.getResourceBundle().getString("TextoVer"));
        txtDuracion.setPromptText(propiedades.getResourceBundle().getString("TextoDuracion"));
        txtServiciosAdicionales.setPromptText(propiedades.getResourceBundle().getString("TextoServiciosAdicionales"));
        txtPrecio.setPromptText(propiedades.getResourceBundle().getString("TextoPrecio"));
        txtCupoMaximo.setPromptText(propiedades.getResourceBundle().getString("TextoCupoMaximo"));
        txtFechaInicio.setPromptText(propiedades.getResourceBundle().getString("TextoFechaInicio"));
        txtFechaFin.setPromptText(propiedades.getResourceBundle().getString("TextoFechaFin"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        txtCiudad.setPromptText(propiedades.getResourceBundle().getString("TextoCiudadDestino"));
        txtDescripcion.setPromptText(propiedades.getResourceBundle().getString("TextoDescripcionDestino"));
        txtClima.setPromptText(propiedades.getResourceBundle().getString("TextoClimaDestino"));
        btnAnterior.setText(propiedades.getResourceBundle().getString("TextoAnterior"));
        btnSiguiente.setText(propiedades.getResourceBundle().getString("TextoSiguiente"));
        txtNombreDestino.setPromptText(propiedades.getResourceBundle().getString("TextoNombreDestino"));

    }
    public void setPaqueteTuristico(PaqueteTuristico paqueteTuristico) {
        this.paqueteTuristico = agenciaCliente.getPaqueteTuristico();
        actualizarCampos(); // Llamamos a este método para llenar los campos inicialmente
    }

    private void actualizarCampos() {
        ArrayList<Destino> destinos = paqueteTuristico.getDestinos();
        String nombresDestinos = destinos.stream().map(Destino :: getNombre).collect(Collectors.joining(", "));
        if (paqueteTuristico != null) {
            txtNombrePaquete.setText(paqueteTuristico.getNombre());
            txtDuracion.setText(paqueteTuristico.getDuracion());
            txtServiciosAdicionales.setText(paqueteTuristico.getServiciosAdicionales());
            txtPrecio.setText(String.valueOf(paqueteTuristico.getPrecio()));
            txtCupoMaximo.setText(String.valueOf(paqueteTuristico.getCupoMaximo()));
            txtFechaInicio.setText(String.valueOf(paqueteTuristico.getFechaInicio()));
            txtFechaFin.setText(String.valueOf(paqueteTuristico.getFechaFin()));

        }
    }

    @FXML
    private void verDestino(){
        Destino destino = agenciaCliente.obtenerDestino(cbxDestinos.getValue());
        txtNombreDestino.setText(destino.getNombre());
        txtCiudad.setText(destino.getCiudad());
        txtDescripcion.setText(destino.getDescripcion());
        txtClima.setText(String.valueOf(destino.getClima()));

        rutasImagenes = destino.getImagenes();

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

        System.out.println(propiedades.getResourceBundle().getString("TextoDetalles") + rutaImagen);
    }

    public void volverSeleccionarPaquete(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/seleccionarPaquete.fxml", event);
        }
    }
}

package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.exceptions.InformacionRepetidaException;
import co.edu.uniquindio.agencia.modelo.*;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalificarDestinoControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private Label lblDestinos, lblComentarioDestino, lblCalificacionDestino;

    @FXML
    private TextField txtComentarioDestino;

    @FXML
    private Button btnAtras, btnGuardar;

    @FXML
    private ComboBox<String> cbxDestinos;

    @FXML
    private Button btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private final Propiedades propiedades = Propiedades.getInstance();

    Reserva reserva = agenciaCliente.getReserva();

    private final Image ESTRELLA_LLENA = new Image(getClass().getResourceAsStream("/imagenes/estrella_llena.jpg"));
    private final Image ESTRELLA_VACIA = new Image(getClass().getResourceAsStream("/imagenes/estrella_vacia.jpg"));

    private int calificacionDestino = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
        inicializarBotones();

        PaqueteTuristico paqueteTuristico = reserva.getPaqueteTuristico();

        ArrayList<Destino> destinosAsociados = paqueteTuristico.getDestinos();

        ObservableList<String> nombresDestinos = FXCollections.observableArrayList();

        for (Destino destino : destinosAsociados) {
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
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnGuardar.setText(propiedades.getResourceBundle().getString("TextoGuardar"));
        lblDestinos.setText(propiedades.getResourceBundle().getString("TextoDestinosAsociados"));
        lblCalificacionDestino.setText(propiedades.getResourceBundle().getString("TextoCalificacion"));
        lblComentarioDestino.setText(propiedades.getResourceBundle().getString("TextoComentario"));
    }

    private void inicializarBotones() {
        Button[] botonesEstrellasDestino = {btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5};

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            botonesEstrellasDestino[i].setOnAction(event -> {
                calificacionDestino = finalI + 1;
                actualizarCalificacion(finalI, botonesEstrellasDestino);
            });
        }
    }

    private void actualizarCalificacion(int indice, Button[] botonesEstrellas) {
        for (int i = 0; i < 5; i++) {
            ImageView imageView = (ImageView) botonesEstrellas[i].getGraphic();
            if (i <= indice) {
                imageView.setImage(ESTRELLA_LLENA);
            } else {
                imageView.setImage(ESTRELLA_VACIA);
            }
        }
    }

    @FXML
    public void actualizarDatos(ActionEvent actionEvent){
        Destino destino = agenciaCliente.obtenerDestino(cbxDestinos.getValue());
        ArrayList<Integer> calificacionesDestino = destino.getCalificaciones();
        calificacionesDestino.add(calificacionDestino);
        ArrayList<String> comentariosDestino = destino.getComentarios();
        comentariosDestino.add(txtComentarioDestino.getText());

        try{
            agenciaCliente.actualizarDestino(
                    destino.getNombre(),
                    destino.getCiudad(),
                    destino.getDescripcion(),
                    String.join(",", destino.getImagenes()),
                    destino.getClima(),
                    calificacionesDestino,
                    comentariosDestino
            );

            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoAlerta7"));
        } catch (AtributoVacioException | InformacionRepetidaException e){
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }

        Button[] botonesEstrellasDestino = {btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5};

        // Restablecer campos y estrellas
        txtComentarioDestino.setText("");

        calificacionDestino = 0;

        for (Button boton : botonesEstrellasDestino) {
            ImageView imageView = (ImageView) boton.getGraphic();
            imageView.setImage(ESTRELLA_VACIA);
        }

    }

    @FXML
    public void volver(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/ventanaReservasControlador.fxml", event);
        }
    }


    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}


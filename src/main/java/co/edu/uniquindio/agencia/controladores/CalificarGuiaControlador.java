package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.AtributoNegativoException;
import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.exceptions.InformacionRepetidaException;
import co.edu.uniquindio.agencia.modelo.*;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalificarGuiaControlador implements Initializable, CambioIdiomaListener {
    @FXML
    private Label lblGuia, lblComentarioGuia, lblCalificacionGuia;

    @FXML
    private TextField txtGuia, txtComentarioGuia;

    @FXML
    private Button btnAtras, btnGuardar;

    @FXML
    private Button btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private final Propiedades propiedades = Propiedades.getInstance();

    Reserva reserva = agenciaCliente.getReserva();

    private final Image ESTRELLA_LLENA = new Image(getClass().getResourceAsStream("/imagenes/estrella_llena.jpg"));
    private final Image ESTRELLA_VACIA = new Image(getClass().getResourceAsStream("/imagenes/estrella_vacia.jpg"));
    private int calificacionGuia = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
        inicializarBotones();

        txtGuia.setText(reserva.getGuiaTuristico().getNombre());
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
        lblGuia.setText(propiedades.getResourceBundle().getString("TextoGuiaTuristico"));
       lblCalificacionGuia.setText(propiedades.getResourceBundle().getString("TextoCalificacion"));
        lblComentarioGuia.setText(propiedades.getResourceBundle().getString("TextoComentario"));
    }


    private void inicializarBotones() {
        Button[] botonesEstrellasGuia = {btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5};

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            botonesEstrellasGuia[i].setOnAction(event -> {
                calificacionGuia = finalI + 1;
                actualizarCalificacion(finalI, botonesEstrellasGuia);
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
        GuiaTuristico guiaTuristico = reserva.getGuiaTuristico();
        ArrayList<Integer> calificacionesGuia = guiaTuristico.getCalificaciones();
        calificacionesGuia.add(calificacionGuia);
        ArrayList<String> comentariosGuia = guiaTuristico.getComentarios();
        comentariosGuia.add(txtComentarioGuia.getText());

        try {
            agenciaCliente.actualizarGuiaTuristico(
                    guiaTuristico.getIdentificacion(),
                    guiaTuristico.getNombre(),
                    guiaTuristico.getIdiomas(),
                    guiaTuristico.getExperiencia(),
                    calificacionesGuia,
                    comentariosGuia
            );
        } catch (AtributoVacioException | InformacionRepetidaException | AtributoNegativoException e){
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }

        Button[] botonesEstrellasGuia = {btnEstrella1, btnEstrella2, btnEstrella3, btnEstrella4, btnEstrella5};

        // Restablecer campos y estrellas
        txtComentarioGuia.setText("");

        calificacionGuia = 0;

        for (Button boton : botonesEstrellasGuia) {
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

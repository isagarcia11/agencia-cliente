package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.enums.Clima;
import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.exceptions.InformacionRepetidaException;
import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModificarDestinoControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TextField txtNombreDestino, txtCiudad, txtDescripcion, txtImagen;

    @FXML
    private ComboBox<Clima> cbxClima;

    @FXML
    private Button btnActualizar, btnAtras, btnBuscar, btnEliminar;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();

    private Destino destino;

    private final Propiedades propiedades = Propiedades.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
        cbxClima.setItems(FXCollections.observableArrayList( List.of(Clima.values() ) ) );


    }

    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        txtNombreDestino.setPromptText(propiedades.getResourceBundle().getString("TextoNombreDestino"));
        btnBuscar.setText(propiedades.getResourceBundle().getString("TextoBuscar"));
        txtCiudad.setPromptText(propiedades.getResourceBundle().getString("TextoCiudad"));
        txtDescripcion.setPromptText(propiedades.getResourceBundle().getString("TextoDescripcion"));
        txtImagen.setPromptText(propiedades.getResourceBundle().getString("TextoImagen"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
        btnActualizar.setText(propiedades.getResourceBundle().getString("TextoActualizar"));
        btnEliminar.setText(propiedades.getResourceBundle().getString("TextoEliminar"));
    }

    public void setDestino() {
        try {
            this.destino = agenciaCliente.obtenerDestino(txtNombreDestino.getText());

            if (destino != null) {
                // El destino se encontró, realiza las acciones necesarias
                actualizarCampos();
            } else {
                // El destino no se encontró, muestra un mensaje al usuario
                mostrarMensaje(Alert.AlertType.ERROR,"Destino no encontrado.");
            }
        } catch (RuntimeException e) {
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void actualizarCampos() {
        if (destino != null) {
            txtNombreDestino.setText(destino.getNombre());
            txtCiudad.setText(destino.getCiudad());
            txtDescripcion.setText(destino.getDescripcion());
            txtImagen.setText(String.join("," , destino.getImagenes()));
            cbxClima.getSelectionModel().select(destino.getClima());
        } else {
            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoDestino"));
        }
    }

    public void actualizarDestino(ActionEvent actionEvent){

        try{
            agenciaCliente.actualizarDestino(
                    txtNombreDestino.getText(),
                    txtCiudad.getText(),
                    txtDescripcion.getText(),
                    txtImagen.getText(),
                    cbxClima.getValue(),
                    destino.getCalificaciones(),
                    destino.getComentarios()
            );

            mostrarMensaje(Alert.AlertType.INFORMATION, propiedades.getResourceBundle().getString("TextoDestino1")+" "+destino.getNombre());
        } catch (AtributoVacioException | InformacionRepetidaException e){
            mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
        }
    }


    public void eliminarDestino(ActionEvent actionEvent){
        agenciaCliente.eliminarDestino(txtNombreDestino.getText());
        mostrarMensaje(Alert.AlertType.INFORMATION, "Se elimino el destino: "+txtNombreDestino.getText());
    }


    public void regresarInicio(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

}

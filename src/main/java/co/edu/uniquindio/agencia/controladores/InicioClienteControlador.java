package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.modelo.Reserva;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class InicioClienteControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private Button btnModificarDatos;

    @FXML
    private Button btnPaquetes;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnRealizarReserva;

    @FXML
    private Button btnMisReservas;

    private final AgenciaCliente agenciaCliente = new AgenciaCliente();
    private final Propiedades propiedades = Propiedades.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        btnPaquetes.setText(propiedades.getResourceBundle().getString("TextoPaquetes"));
        btnModificarDatos.setText(propiedades.getResourceBundle().getString("TextoModificarDatos"));
        btnSalir.setText(propiedades.getResourceBundle().getString("TextoSalir"));

    }
    public void modificarDatos(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnModificarDatos)){
            agenciaCliente.loadStage("/ventanas/modificarCliente.fxml", event);
        }
    }

    public void irAPaquetes(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnPaquetes)){
            agenciaCliente.loadStage("/ventanas/seleccionarPaquete.fxml", event);
        }
    }

    public void volverLogin(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnSalir)){
            agenciaCliente.loadStage("/ventanas/login.fxml", event);
        }
    }

    public void irARealizarReserva(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnRealizarReserva)){
            agenciaCliente.loadStage("/ventanas/registrarReserva.fxml", event);
        }
    }

    public void irAMisReservas(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(btnMisReservas)){
            agenciaCliente.loadStage("/ventanas/ventanaReservasControlador.fxml", event);
        }
    }
}

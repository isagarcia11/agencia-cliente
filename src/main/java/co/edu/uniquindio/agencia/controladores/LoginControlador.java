package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.exceptions.AtributoVacioException;
import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Cliente;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginControlador implements Initializable, CambioIdiomaListener {

    @FXML
    private TextField usuarioCliente;

    @FXML
    private PasswordField contrasenaCliente;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnRegistrarse;

    @FXML
    private Button btnAtras;

    @FXML
    private Label banner2;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        AgenciaCliente agenciaCliente = new AgenciaCliente(); // crea una nueva instancia


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
        usuarioCliente.setPromptText(propiedades.getResourceBundle().getString("TextoIngresaUsuario"));
        contrasenaCliente.setPromptText(propiedades.getResourceBundle().getString("TextoIngresaContrasena"));
        btnIniciar.setText(propiedades.getResourceBundle().getString("TextoIniciarSesion"));
        btnRegistrarse.setText(propiedades.getResourceBundle().getString("TextoRegistrarse"));
        banner2.setText(propiedades.getResourceBundle().getString("TextoBanner2"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));

    }

    public void iniciarSesion(ActionEvent event) {
        try {
            boolean mensaje = agenciaCliente.verificarDatos(usuarioCliente.getText(), contrasenaCliente.getText());

            if (usuarioCliente.getText().equals("admin") && contrasenaCliente.getText().equals("admin")) {
                agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
            }else if(mensaje){
                agenciaCliente.loadStage("/ventanas/inicioCliente.fxml", event);
            }else{
                // Acceso incorrecto
                mostrarMensaje(Alert.AlertType.ERROR, "Credenciales incorrectas");
            }
        } catch (AtributoVacioException e) {
            mostrarMensaje(Alert.AlertType.ERROR, "El usuario y contraseña son obligatorios");
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error durante el inicio de sesión");
        }
    }

    public void registrarUsuario(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnRegistrarse)){
            agenciaCliente.loadStage("/ventanas/registrarCliente.fxml", event);
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
    public void atras(ActionEvent event) {
        Object evt = event.getSource();
        if (evt.equals(btnAtras)) {
            agenciaCliente.loadStage("/ventanas/inicio.fxml", event);
        }

    }
}

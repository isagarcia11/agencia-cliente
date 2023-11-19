package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Cliente;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import co.edu.uniquindio.agencia.utils.CambioIdiomaEvent;
import co.edu.uniquindio.agencia.utils.CambioIdiomaListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.net.URL;
import java.util.ResourceBundle;

public class ClientesControlador implements Initializable, CambioIdiomaListener {
    public TableView<Cliente> tableView;
    public TableColumn<Cliente, String> columnIdentificacion;
    public TableColumn<Cliente, String> columnNombre;
    public TableColumn<Cliente, String> columnCorreo;
    public TableColumn<Cliente, String> columnTelefono;
    public TableColumn<Cliente, String> columnDireccion;

    @FXML
    private Button btnAtras;

    public AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización normal del controlador

        // Registra este controlador como un escuchador de cambios de idioma
        Propiedades.getInstance().addCambioIdiomaListener(this);

        // Actualiza las cadenas de texto según el idioma actual
        actualizarTextos();
        columnIdentificacion.setCellValueFactory( cellData -> new SimpleStringProperty( cellData.getValue().getIdentificacion()) );
        columnNombre.setCellValueFactory( cellData -> new SimpleStringProperty( cellData.getValue().getNombre()) );
        columnCorreo.setCellValueFactory( cellData -> new SimpleStringProperty( cellData.getValue().getCorreo()) );
        columnTelefono.setCellValueFactory( cellData -> new SimpleStringProperty( cellData.getValue().getTelefono()) );
        columnDireccion.setCellValueFactory( cellData -> new SimpleStringProperty( cellData.getValue().getDireccion()) );

        tableView.setItems(FXCollections.observableArrayList(agenciaCliente.listarClientes()));

    }
    @Override
    public void onCambioIdioma(CambioIdiomaEvent evento) {
        // Se llama cuando se cambia el idioma

        // Actualiza las cadenas de texto según el nuevo idioma
        actualizarTextos();
    }

    private void actualizarTextos() {
        columnIdentificacion.setText(propiedades.getResourceBundle().getString("TextoIdentificacion"));
        columnNombre.setText(propiedades.getResourceBundle().getString("TextoNombre"));
        columnCorreo.setText(propiedades.getResourceBundle().getString("TextoCorreo"));
        columnTelefono.setText(propiedades.getResourceBundle().getString("TextoTelefono"));
        columnDireccion.setText(propiedades.getResourceBundle().getString("TextoDireccion"));
        btnAtras.setText(propiedades.getResourceBundle().getString("TextoAtras"));
    }

    public void regresarInicio(ActionEvent event){
        Object evt = event.getSource();
        if(evt.equals(btnAtras)){
            agenciaCliente.loadStage("/ventanas/inicioAdmin.fxml", event);
        }
    }
}

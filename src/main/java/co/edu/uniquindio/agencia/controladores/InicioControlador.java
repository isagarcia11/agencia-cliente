package co.edu.uniquindio.agencia.controladores;

import co.edu.uniquindio.agencia.modelo.AgenciaCliente;
import co.edu.uniquindio.agencia.modelo.Destino;
import co.edu.uniquindio.agencia.modelo.PaqueteTuristico;
import co.edu.uniquindio.agencia.modelo.Propiedades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class InicioControlador implements Initializable {

    @FXML
    private Button iniciarSesion, cambiarIdioma;

    @FXML
    private Label banner;

    @FXML
    private ImageView imagen;

    @FXML
    private Button nombreDestino;

    @FXML
    private Button botonSiguiente;

    @FXML
    private Button botonAtras;

    @FXML
    private Button nombrePaquete;

    @FXML
    private ImageView imagenDestino;

    @FXML
    private Button btnAnteriorPaquete, btnSiguientePaquete;

    private final AgenciaCliente agenciaCliente = AgenciaCliente.getInstance();
    private final Propiedades propiedades = Propiedades.getInstance();

    private List<Destino> destinos = agenciaCliente.listarDestino();
    private int indiceActual = 0;
    private int indicePaqueteActual = 0;

    private boolean esIngles = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarTextos();
        btnSiguientePaquete.setOnAction(event -> cambiarPaquete(1));
        btnAnteriorPaquete.setOnAction(event -> cambiarPaquete(-1));

        mostrarPaquete(agenciaCliente.listarPaquetesTuristicos().get(indicePaqueteActual));
        mostrarDestino();
    }

    @FXML
    private void cambiarIdioma(ActionEvent event) {
        // Cambia el idioma
        if (esIngles) {
            Propiedades.getInstance().setLocale(new Locale("es", "ES"));
        } else {
            Propiedades.getInstance().setLocale(Locale.ENGLISH);
        }

        // Invierte el valor de esIngles para la próxima vez
        esIngles = !esIngles;

        // Actualiza la interfaz de usuario
        actualizarTextos();
    }

    @FXML
    private void siguienteDestino() {
        if (indiceActual < destinos.size() - 1) {
            indiceActual++;
            mostrarDestino();
        }
    }

    @FXML
    private void destinoAnterior() {
        if (indiceActual > 0) {
            indiceActual--;
            mostrarDestino();
        }
    }

    private void mostrarDestino() {
        Destino destinoActual = destinos.get(indiceActual);
        nombreDestino.setText(destinoActual.getNombre());

        String urlImagen = destinoActual.getImagenes().get(0);
        Image imagen = new Image(urlImagen);
        this.imagen.setImage(imagen);
    }

    private void mostrarPaquete(PaqueteTuristico paqueteTuristico) {
        if (paqueteTuristico != null) {
            nombrePaquete.setText(paqueteTuristico.getNombre());

            if (!paqueteTuristico.getDestinos().isEmpty()) {
                String primeraRutaImagen = paqueteTuristico.getDestinos().get(0).getImagenes().get(0);
                Image img = new Image(primeraRutaImagen);
                imagenDestino.setImage(img);
            } else {
                imagenDestino.setImage(null);
            }
        } else {
            nombrePaquete.setText("");
            imagenDestino.setImage(null);
        }
    }

    private void cambiarPaquete(int cambio) {
        int nuevoIndice = indicePaqueteActual + cambio;

        if (nuevoIndice >= 0 && nuevoIndice < agenciaCliente.listarPaquetesTuristicos().size()) {
            indicePaqueteActual = nuevoIndice;
            mostrarPaquete(agenciaCliente.listarPaquetesTuristicos().get(indicePaqueteActual));
        }
    }


    @FXML
    public void conocerDestino(ActionEvent event){
        agenciaCliente.setDestino(destinos.get(indiceActual));

        Object evt = event.getSource();

        if(evt.equals(nombreDestino)){
            agenciaCliente.loadStage("/ventanas/conocerDestino.fxml", event);
        }
    }



    @FXML
    public void conocerPaquete(ActionEvent event){
        agenciaCliente.setPaqueteTuristico(agenciaCliente.listarPaquetesTuristicos().get(indicePaqueteActual));

        Object evt = event.getSource();

        if(evt.equals(nombrePaquete)){
            agenciaCliente.loadStage("/ventanas/conocerPaquete.fxml", event);
        }
    }

    private void actualizarTextos() {
        iniciarSesion.setText(propiedades.getResourceBundle().getString("TextoIniciarSesion"));
        cambiarIdioma.setText(propiedades.getResourceBundle().getString("TextoCambiarIdioma"));
        banner.setText(propiedades.getResourceBundle().getString("TextoBanner"));
    }


    public void iniciarSesion(ActionEvent event){

        Object evt = event.getSource();

        if(evt.equals(iniciarSesion)){
            agenciaCliente.loadStage("/ventanas/login.fxml", event);
        }
    }

}

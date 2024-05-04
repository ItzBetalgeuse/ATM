package sample.ficheros;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class LoginController {
    @FXML private TextField CuentaField;
    @FXML private PasswordField Password;
    protected static Stage Ventana;

    @FXML private void login(){
        DatosUsuario usuario = Authenticate(CuentaField.getText(), Password.getText());
        if (usuario != null){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Cajero.fxml"));
                Parent root = fxmlLoader.load();
                ATMController atmController = fxmlLoader.getController();
                atmController.initData(usuario);

                Stage stage = (Stage) CuentaField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }catch (IOException ignored){}
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Acceso no autorizado, ingrese credenciales válidas");
            alert.showAndWait();
        }
    }

    private static File buscar(String archivoABuscar, File directorio){
        if(directorio.isDirectory()){
            File[] archivos = directorio.listFiles();
            if(archivos != null){
                for(File archivo : archivos){
                    if (archivo.isDirectory()){
                        File archivoEncontrado = buscar(archivoABuscar, archivo);
                        if (archivoEncontrado != null){
                            return archivoEncontrado;
                        }
                    } else if (archivo.getName().equals(archivoABuscar)) {
                        return archivo;
                    }
                }
            }
        }
        return null;
    }

    private DatosUsuario Authenticate(String NumCuenta, String Password){
        try {
            File archivo = buscar(CuentaField.getText()+".txt", new File("src\\main\\resources\\Cuentas"));

            if (archivo != null) {
                Scanner entrada = new Scanner(archivo);

                    String linea = entrada.nextLine();
                    String[] partes = linea.split(":");

                    if (NumCuenta.equals(partes[0]) && Password.equals(partes[1])) {
                        double saldo = ObtenerSaldo(archivo);

                        DatosUsuario usuario = new DatosUsuario();
                        usuario.setUsuario(NumCuenta);
                        usuario.setSaldo(saldo);

                        return usuario;
                    }
                entrada.close();
            }
        }catch (FileNotFoundException ignored){}
        return null;
    }

    private double ObtenerSaldo(File archivo){
        try{
            Scanner sc = new Scanner(Objects.requireNonNull(archivo));
            String datosLogin = sc.nextLine();
            String saldoLinea = sc.nextLine();
            double saldo = Double.parseDouble(saldoLinea.substring(saldoLinea.indexOf("=")+1).trim());
            sc.close();
            return saldo;
        } catch (FileNotFoundException ignored) {}
        return 0;
    }

    public static class DatosUsuario{
        private String Usuario;
        private double saldo;

        public String getUsuario(){
            return Usuario;
        }

        public void setUsuario(String Usuario){
            this.Usuario = Usuario;
        }

        public double getSaldo(){
            return saldo;
        }

        public void setSaldo(double saldo){
            this.saldo = saldo;
        }
    }
}
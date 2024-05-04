package sample.ficheros;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.util.Optional;

public class ATMController {
    @FXML private Label CuentaLabel;
    @FXML private Label SaldoLabel;

    public void initData(LoginController.DatosUsuario datosUsuario){
        String nombreUsuario = datosUsuario.getUsuario();
        String saldoCuenta = String.valueOf(datosUsuario.getSaldo());
        CuentaLabel.setText(nombreUsuario);
        SaldoLabel.setText(saldoCuenta);
    }

    @FXML private void Deposito(){
        // Mostrar un diálogo para que el usuario ingrese la cantidad a depositar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setHeaderText("Ingrese la cantidad a depositar:");
        dialog.setContentText("Cantidad:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidad -> {
            double saldoActual = Double.parseDouble(SaldoLabel.getText());
            double CantDeposito = Double.parseDouble(cantidad);
            saldoActual += CantDeposito;
            SaldoLabel.setText(String.valueOf(saldoActual));

            // Escribir el nuevo saldo en el archivo
            escribirSaldoEnArchivo(saldoActual);
        });
    }

    @FXML private void Retiro(){
        // Mostrar un diálogo para que el usuario ingrese la cantidad a retirar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Retiro");
        dialog.setHeaderText("Ingrese la cantidad a retirar:");
        dialog.setContentText("Cantidad:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidad -> {
            double saldoActual = Double.parseDouble(SaldoLabel.getText());
            double CantRetiro = Double.parseDouble(cantidad);
            if (CantRetiro > saldoActual) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                // Resto del código...
            } else {
                saldoActual -= CantRetiro;
                SaldoLabel.setText(String.valueOf(saldoActual));

                // Escribir el nuevo saldo en el archivo
                escribirSaldoEnArchivo(saldoActual);
            }
        });
    }

    private void escribirSaldoEnArchivo(double nuevoSaldo) {
        try {
            // Ruta del archivo de cuentas
            String rutaArchivo = "src/main/resources/Cuentas/" + CuentaLabel.getText() + ".txt";
            File archivo = new File(rutaArchivo);

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            String nuevoContenido = "";
            int contador = 0;

            // Iterar sobre las líneas del archivo
            while ((linea = br.readLine()) != null) {
                // Incrementar el contador para controlar la segunda línea
                contador++;
                // Si es la segunda línea, reemplazar el saldo
                if (contador == 2) {
                    nuevoContenido += "Saldo=" + nuevoSaldo + "\n";
                } else {
                    // Si no es la segunda línea, mantener la línea original
                    nuevoContenido += linea + "\n";
                }
            }
            br.close();

            // Escribir el nuevo contenido en el archivo
            FileWriter writer = new FileWriter(archivo);
            writer.write(nuevoContenido);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

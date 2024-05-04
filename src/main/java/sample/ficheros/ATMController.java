package sample.ficheros;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ATMController {
    @FXML private Label CuentaLabel;
    @FXML private Label SaldoLabel;


    @FXML private TextArea historialTextArea;

    public void initData(LoginController.DatosUsuario datosUsuario) {
        String nombreUsuario = datosUsuario.getUsuario();
        String saldoCuenta = String.valueOf(datosUsuario.getSaldo());
        CuentaLabel.setText(nombreUsuario);
        SaldoLabel.setText(saldoCuenta);
    }

    @FXML
    private void Deposito() {
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
            escribirSaldoEnArchivo(saldoActual, CantDeposito, "Depósito");
            // Actualizar el historial en el TextArea
            cargarHistorial();
        });
    }


    @FXML
    private void Retiro() {
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
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.setContentText("No se puede retirar una cantidad mayor al saldo de la cuenta.");
                alert.showAndWait();
            } else {
                saldoActual -= CantRetiro;
                SaldoLabel.setText(String.valueOf(saldoActual));

                // Escribir el nuevo saldo en el archivo
                escribirSaldoEnArchivo(saldoActual, CantRetiro, "Retiro");
                // Actualizar el historial en el TextArea
                cargarHistorial();
            }
        });
    }
    private void escribirSaldoEnArchivo(double nuevoSaldo, double cantidad, String tipoTransaccion) {
        try {
            // Ruta del archivo de cuentas
            String rutaArchivo = "src/main/resources/Cuentas/" + CuentaLabel.getText() + ".txt";
            File archivo = new File(rutaArchivo);

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            StringBuilder nuevoContenido = new StringBuilder();
            int contador = 0;

            // Iterar sobre las líneas del archivo
            while ((linea = br.readLine()) != null) {
                contador++;
                // Si es la segunda línea, reemplazar el saldo
                if (contador == 2) {
                    nuevoContenido.append("Saldo=").append(nuevoSaldo).append("\n");
                } else {
                    nuevoContenido.append(linea).append("\n");
                }
            }
            br.close();

            // Agregar la nueva transacción al historial
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            nuevoContenido.append(formattedDateTime).append(" - ").append(tipoTransaccion).append(": ").append(cantidad).append("\n");

            // Escribir el nuevo contenido en el archivo
            FileWriter writer = new FileWriter(archivo);
            writer.write(nuevoContenido.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarHistorial() {
        try {
            String rutaArchivo = "src/main/resources/Cuentas/" + CuentaLabel.getText() + ".txt";
            File archivo = new File(rutaArchivo);

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            StringBuilder historial = new StringBuilder();
            boolean inicioHistorial = false;
            int lineCount = 0;

            // Iterar sobre las líneas del archivo
            while ((linea = br.readLine()) != null) {
                lineCount++;
                if (lineCount > 2) {
                    historial.append(linea).append("\n");
                }
                if (!inicioHistorial && linea.contains("Historial de transacciones")) {
                    inicioHistorial = true;
                }
            }

            // Cerrar el BufferedReader después de haber terminado de leer el archivo
            br.close();

            // Establecer el contenido del TextArea
            historialTextArea.setText(historial.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
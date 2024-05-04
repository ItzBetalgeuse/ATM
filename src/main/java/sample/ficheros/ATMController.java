package sample.ficheros;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        double saldoActual = Double.parseDouble(SaldoLabel.getText());
        double CantDeposito = 100.0;
        saldoActual += CantDeposito;

        SaldoLabel.setText(String.valueOf(saldoActual));
    }

    @FXML private void Retiro(){
        double saldoActual = Double.parseDouble(SaldoLabel.getText());
        double CantRetiro = 100.0;
        if (CantRetiro > saldoActual){
            System.out.println("No se puede retirar");
        }else {
            saldoActual -= CantRetiro;
        }

        SaldoLabel.setText(String.valueOf(saldoActual));
    }
}

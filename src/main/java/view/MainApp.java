package view;

import controller.UsuarioController; // Importa o controller
import javax.swing.SwingUtilities;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Cria a View
                LoginView loginView = new LoginView();

                // Cria o Controller e passa a View para ele.
                new UsuarioController(loginView);

                // Torna a View visível
                loginView.setVisible(true);
            }
        });
    }
}
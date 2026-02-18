package co.unicauca.usermanagement.view;

import co.unicauca.usermanagement.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartFrame extends JFrame {

    private final UserService service;
    
    public StartFrame(UserService service) {
        this.service = service;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Usuarios");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("Bienvenido", JLabel.CENTER);
        title.setFont(UIStyles.FONT_TITLE);

        JLabel subtitle = new JLabel("Seleccione una opción:", JLabel.CENTER);

        JButton btnLogin = new JButton("Iniciar sesión");
        JButton btnRegister = new JButton("Registrarse");

        UIStyles.makePrimaryButton(btnLogin);
        UIStyles.makePrimaryButton(btnRegister);

        btnLogin.addActionListener(e -> {
            new LoginFrame(service, this).setVisible(true);
            setVisible(false);
        });

        btnRegister.addActionListener(e -> {
            new RegisterFrame(service, this).setVisible(true);
            setVisible(false);
        });

        JPanel center = new JPanel(new GridLayout(0, 1, 0, 20));
        center.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        center.add(subtitle);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.add(btnLogin);
        buttons.add(btnRegister);

        center.add(buttons);

        add(title, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        UIStyles.applyFont(getContentPane());
    }
}

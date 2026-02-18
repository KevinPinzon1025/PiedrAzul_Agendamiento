package co.unicauca.usermanagement.view;

import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginFrame extends JFrame {

    private final UserService service;
    private final JFrame previous;

    public LoginFrame(UserService service, JFrame previous) {
        this.service = service;
        this.previous = previous;
        initComponents();
    }

    private void initComponents() {
        setTitle("Iniciar sesión");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Iniciar sesión", JLabel.CENTER);
        title.setFont(UIStyles.FONT_TITLE);

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(30, 60, 10, 60));

        form.add(new JLabel("Usuario:"));
        form.add(txtUser);

        form.add(new JLabel("Contraseña:"));
        form.add(txtPass);

        JButton btnBack = new JButton("Volver");
        JButton btnLogin = new JButton("Entrar");

        UIStyles.makeSecondaryButton(btnBack);
        UIStyles.makePrimaryButton(btnLogin);

        btnBack.addActionListener(e -> {
            dispose();
            previous.setVisible(true);
        });

        btnLogin.addActionListener(e -> {
            String login = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            User user = service.login(login, pass);
            if (user != null) {
                new MenuFrame(user, service).setVisible(true);
                dispose();
                previous.dispose(); // cierra StartFrame para que no quede abierto atrás
            } else {
                JOptionPane.showMessageDialog(this,
                    "Credenciales incorrectas o usuario inactivo.",
                    "No se pudo iniciar sesión",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottom.add(btnBack);
        bottom.add(btnLogin);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        UIStyles.applyFont(getContentPane());
    }
}



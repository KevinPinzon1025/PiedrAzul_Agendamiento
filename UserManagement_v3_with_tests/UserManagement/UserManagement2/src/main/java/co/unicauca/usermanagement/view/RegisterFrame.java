package co.unicauca.usermanagement.view;

import co.unicauca.usermanagement.service.PasswordPolicy;
import co.unicauca.usermanagement.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterFrame extends JFrame {
    private final UserService service;
    private final JFrame previous;

    public RegisterFrame(UserService service, JFrame previous) {
        this.service = service;
        this.previous = previous;
        initComponents();
    }

    private void initComponents() {
        setTitle("Registro");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Registro de usuario", JLabel.CENTER);
        title.setFont(UIStyles.FONT_TITLE);

        JTextField txtLogin = new JTextField();
        JTextField txtFullName = new JTextField();

        JComboBox<String> cbRole = new JComboBox<>(new String[] {
            "Médico/Terapista", "Agendador de citas", "Administrador"
        });

        JCheckBox chkActive = new JCheckBox("Activo", true);

        JPasswordField txtPass = new JPasswordField();
        JPasswordField txtPass2 = new JPasswordField();

        JPanel form = new JPanel(new GridLayout(0, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(25, 60, 10, 60));

        form.add(new JLabel("Nombre de usuario (login):"));
        form.add(txtLogin);

        form.add(new JLabel("Nombre completo:"));
        form.add(txtFullName);

        form.add(new JLabel("Rol:"));
        form.add(cbRole);

        form.add(new JLabel("Estado:"));
        form.add(chkActive);

        form.add(new JLabel("Contraseña:"));
        form.add(txtPass);

        form.add(new JLabel("Confirmar contraseña:"));
        form.add(txtPass2);

        JLabel hint = new JLabel("<html><b>Requisitos:</b> mínimo 6 caracteres, 1 mayúscula, 1 dígito y 1 especial.</html>");
        hint.setBorder(BorderFactory.createEmptyBorder(0, 60, 10, 60));

        JButton btnBack = new JButton("Volver");
        JButton btnSave = new JButton("Crear usuario");

        UIStyles.makeSecondaryButton(btnBack);
        UIStyles.makePrimaryButton(btnSave);

        btnBack.addActionListener(e -> {
            dispose();
            previous.setVisible(true);
        });

        btnSave.addActionListener(e -> {
            String login = txtLogin.getText().trim();
            String fullName = txtFullName.getText().trim();
            String role = (String) cbRole.getSelectedItem();
            boolean active = chkActive.isSelected();

            String p1 = new String(txtPass.getPassword());
            String p2 = new String(txtPass2.getPassword());

            if (login.isBlank() || fullName.isBlank()) {
                JOptionPane.showMessageDialog(this, "Complete usuario y nombre completo.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!PasswordPolicy.isValid(p1)) {
                JOptionPane.showMessageDialog(this, PasswordPolicy.errorMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = service.register(login, fullName, role, active, p1);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                previous.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo registrar. Verifique que el login no exista.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottom.add(btnBack);
        bottom.add(btnSave);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(hint, BorderLayout.SOUTH);
        add(bottom, BorderLayout.PAGE_END); // si tu NetBeans se queja, elimina esta línea y deja solo bottom en SOUTH

        // Nota: Si PAGE_END te genera conflicto, usa solo SOUTH:
        // add(bottom, BorderLayout.SOUTH);

        UIStyles.applyFont(getContentPane());
    }
}

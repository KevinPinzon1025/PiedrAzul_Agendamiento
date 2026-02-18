package co.unicauca.usermanagement.view;

import co.unicauca.usermanagement.User;
import co.unicauca.usermanagement.service.UserService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuFrame extends JFrame {

    public MenuFrame(User user, UserService service) {

        setTitle("Menú Principal");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ===== TÍTULO =====
        JLabel lblTitle = new JLabel("Menú Principal", SwingConstants.CENTER);
        lblTitle.setFont(UIStyles.FONT_TITLE);

        JLabel lblWelcome = new JLabel("Bienvenido: " + user.getFullName(), SwingConstants.CENTER);
        JLabel lblRole = new JLabel("Rol: " + user.getRole(), SwingConstants.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(0,1,0,10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.add(lblTitle);
        topPanel.add(lblWelcome);
        topPanel.add(lblRole);

        // ===== BOTONES DE ACCIÓN =====
        JPanel actionsPanel = new JPanel(new GridLayout(0,1,20,20));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        switch (user.getRole()) {

            case "Administrador" -> {
                JButton btnManage = new JButton("Gestionar usuarios");
                JButton btnReports = new JButton("Ver reportes");

                UIStyles.makePrimaryButton(btnManage);
                UIStyles.makePrimaryButton(btnReports);

                actionsPanel.add(btnManage);
                actionsPanel.add(btnReports);
            }

            case "Agendador de citas" -> {
                JButton btnSchedule = new JButton("Agendar cita");
                JButton btnAgenda = new JButton("Ver agenda");

                UIStyles.makePrimaryButton(btnSchedule);
                UIStyles.makePrimaryButton(btnAgenda);

                actionsPanel.add(btnSchedule);
                actionsPanel.add(btnAgenda);
            }

            default -> { // Médico/Terapista
                JButton btnView = new JButton("Ver citas");
                JButton btnRegister = new JButton("Registrar atención");

                UIStyles.makePrimaryButton(btnView);
                UIStyles.makePrimaryButton(btnRegister);

                actionsPanel.add(btnView);
                actionsPanel.add(btnRegister);
            }
        }

        // ===== BOTÓN CERRAR SESIÓN =====
        JButton btnLogout = new JButton("Cerrar sesión");
        UIStyles.makeSecondaryButton(btnLogout);

        btnLogout.addActionListener(e -> {
            new StartFrame(service).setVisible(true);
            dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.add(btnLogout);

        // ===== AGREGAR TODO =====
        add(topPanel, BorderLayout.NORTH);
        add(actionsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        UIStyles.applyFont(getContentPane());
    }
}

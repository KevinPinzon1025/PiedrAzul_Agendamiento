package co.unicauca.usermanagement.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UIStyles {

    // 🔽 Fuentes más pequeñas
    public static final Font FONT_NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 18);

    public static void applyFont(Container root) {
        for (Component c : root.getComponents()) {

            if (c instanceof JLabel lbl) lbl.setFont(FONT_NORMAL);
            if (c instanceof JButton btn) btn.setFont(FONT_NORMAL);
            if (c instanceof JTextField tf) tf.setFont(FONT_NORMAL);

            if (c instanceof JComponent jc) jc.setFont(FONT_NORMAL);
            if (c instanceof Container child) applyFont(child);
        }
    }

    public static void makePrimaryButton(JButton btn) {
        btn.setFont(FONT_NORMAL);
        btn.setFocusPainted(false);

        // 🔽 Botones más pequeños
        btn.setPreferredSize(new java.awt.Dimension(160, 40));
        btn.setMargin(new Insets(5, 12, 5, 12));
    }

    public static void makeSecondaryButton(JButton btn) {
        btn.setFont(FONT_NORMAL);
        btn.setFocusPainted(false);

        btn.setPreferredSize(new java.awt.Dimension(140, 38));
        btn.setMargin(new Insets(5, 10, 5, 10));
    }
}

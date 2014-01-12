
package javaim.client.view.gui.login;

import javaim.client.view.event.LoginDialogListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Asks use for her credentials.
 */
public class LoginDialog extends JDialog {

    private static final Dimension PANEL_DIMENSION = new Dimension(330, 60);
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JLabel lbErrorEmptyField;
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnCancel;
    private JButton btnLogin;

    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Username: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);

        lbErrorEmptyField = new JLabel("Username or password is invalid");
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(lbErrorEmptyField, cs);
        lbErrorEmptyField.setVisible(false);

        btnLogin = new JButton("Login");
        btnCancel = new JButton("Quit");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
                System.exit(0);
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnLogin);
        bp.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        panel.setPreferredSize(PANEL_DIMENSION);
        panel.setMinimumSize(PANEL_DIMENSION);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    /**
     * Defines action with obtained credentials.
     * @param listener Object implementing {@link javaim.client.view.event.LoginDialogListener}
     */
    public void setListener(final LoginDialogListener listener) {

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                if (tfUsername.getText().isEmpty() ||
                        !isUsernameValid(tfUsername.getText()) ||
                        pfPassword.getPassword().length == 0) {
                    lbErrorEmptyField.setVisible(true);
                    return;
                }
                lbErrorEmptyField.setVisible(false);

                listener.onOk(tfUsername.getText(), pfPassword.getPassword());
                dispose();
            }
        });
    }

    private boolean isUsernameValid(String username) {
        return username.matches("[A-Za-z0-9.]+");
    }
}

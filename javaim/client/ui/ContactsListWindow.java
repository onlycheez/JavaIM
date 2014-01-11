
package javaim.client.ui;

import javaim.client.ui.MessageSentListener;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class ContactsListWindow extends JFrame {

    private static final String APPLICATION_NAME = "JavaIM client";
    private static final Dimension DEFAULT_DIMENSION = new Dimension(200, 600);
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit()
            .getScreenSize();

    private ContactsListPane contentPane;

    public ContactsListWindow(View view) {
        setTitle(APPLICATION_NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(DEFAULT_DIMENSION);
        setLocation((SCREEN_SIZE.width / 2) - (getWidth() / 2),
                (SCREEN_SIZE.height / 2) - (getHeight() / 2));

        contentPane = new ContactsListPane(this, view);
        setContentPane(contentPane);

        pack();
        setVisible(true);
    }

    public void updateContactList(String[] contacts) {
        contentPane.updateContactList(contacts);
    }

    public void openConversationWindow(String contact) {
        contentPane.openConversationWindow(contact);
    }

    public void setMessageSentListener(MessageSentListener listener) {
        contentPane.setMessageSentListener(listener);
    }
}


package javaim.client.ui;

import javaim.client.ui.View;
import javaim.client.ui.ContactsList;
import javaim.client.ui.ConversationWindowCreation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;

/**
 * Represents content of contacts list window.
 */
public class ContactsListPane extends JPanel {

    ContactsListWindow parentWindow;
    ContactsList contactsList;

    public ContactsListPane(ContactsListWindow parentWindow, View view) {
        this.parentWindow = parentWindow;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(this.parentWindow.getWidth(),
                this.parentWindow.getHeight()));

        contactsList = new ContactsList(view);

        add(contactsList, BorderLayout.CENTER);
    }

    public void setMessageSentListener(ActionListener listener) {
        contactsList.setMessageSentListener(listener);
    }
}

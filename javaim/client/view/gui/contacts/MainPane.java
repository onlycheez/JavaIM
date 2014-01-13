
package javaim.client.view.gui.contacts;

import javaim.client.view.View;
import javaim.client.view.gui.contacts.ContactsList;
import javaim.client.view.event.MessageSentListener;

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
class MainPane extends JPanel {

    ContactsListWindow parentWindow;
    ContactsList contactsList;

    public MainPane(ContactsListWindow parentWindow, View view) {
        this.parentWindow = parentWindow;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(this.parentWindow.getWidth(),
                this.parentWindow.getHeight()));

        contactsList = new ContactsList(view);

        add(contactsList, BorderLayout.CENTER);
    }

    public void updateContactList(String[] contacts) {
        contactsList.setContacts(contacts);
    }

    public void openConversationWindow(String contact) {
        contactsList.openConversationWindow(contact);
    }

    public void setMessageSentListener(MessageSentListener listener) {
        contactsList.setMessageSentListener(listener);
    }
}


package javaim.client.view;

import javaim.client.view.gui.login.LoginDialog;
import javaim.client.view.gui.contacts.ContactsListWindow;
import javaim.client.view.gui.conversation.ConversationWindow;
import javaim.client.view.event.MessageSentListener;
import javaim.client.view.event.LoginDialogListener;

import java.util.HashMap;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class View {
    private ContactsListWindow contactsListWindow;
    private HashMap<String, ConversationWindow> conversationWindows;
    private String username;

    public View() {
        contactsListWindow = new ContactsListWindow(this);
        conversationWindows = new HashMap<>();
    }

    public String[] showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(contactsListWindow);

        final String[] credentials = new String[] { null, null };

        loginDialog.setListener(new LoginDialogListener() {
            public void onOk(String username, char[] password) {
                credentials[0] = username;
                credentials[1] = new String(password);
            }
        });

        loginDialog.setVisible(true);

        return credentials;
    }

    public void showMessage(String from, String message) {
        ConversationWindow conversationWindow = conversationWindows.get(from);
        if (conversationWindow == null) {
            contactsListWindow.openConversationWindow(from);
            while ((conversationWindow = conversationWindows.get(from)) == null) {
                // TODO: This isn't very nice solution.
                ;
            }
        }

        conversationWindow.showMessage(message);
    }

    public void updateContactsList(String[] contacts) {
        if (contacts.length <= 1) {
            return;
        }

        System.out.print("Prdel: ");

        String[] contactsWithoutMe = new String[contacts.length - 1];

        int i = 0;
        for (String contact : contacts) {
            System.out.print(contact);
            if (!contact.equals(username)) {
                System.out.print(" (adding)");
                contactsWithoutMe[i] = contact;
                i += 1;
            }
            System.out.print(", ");
        }
        System.out.println();

        contactsListWindow.updateContactList(contactsWithoutMe);
    }

    public void setMessageSentListener(MessageSentListener listener) {
        contactsListWindow.setMessageSentListener(listener);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void onConversationWindowCreate(String contact,
            ConversationWindow conversationWindow) {
        conversationWindows.put(contact, conversationWindow);
    }

    public void removeConversationWindow(String contact) {
        conversationWindows.remove(contact);
    }
}

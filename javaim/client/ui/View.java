
package javaim.client.ui;

import javaim.client.ui.ContactsListWindow;
import javaim.client.ui.ConversationWindow;
import javaim.client.ui.MessageSentListener;

import java.util.HashMap;
import java.awt.event.ActionListener;

public class View {
    private ContactsListWindow contactsListWindow;
    private HashMap<String, ConversationWindow> conversationWindows;

    public View() {
        contactsListWindow = new ContactsListWindow(this);
        conversationWindows = new HashMap<>();
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
        contactsListWindow.updateContactList(contacts);
    }

    public void setMessageSentListener(MessageSentListener listener) {
        contactsListWindow.setMessageSentListener(listener);
    }

    public void onConversationWindowCreate(String contact,
            ConversationWindow conversationWindow) {
        conversationWindows.put(contact, conversationWindow);
    }

    public void removeConversationWindow(String contact) {
        conversationWindows.remove(contact);
    }
}

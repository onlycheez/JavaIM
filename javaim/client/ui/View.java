
package javaim.client.ui;

import javaim.client.ui.ContactsListWindow;
import javaim.client.ui.ConversationWindow;

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
        conversationWindows.get(from).showMessage(message);
    }

    public void setMessageSentListener(ActionListener listener) {
        contactsListWindow.setMessageSentListener(listener);
    }

    public void onConversationWindowCreate(String contact,
            ConversationWindow conversationWindow) {
        conversationWindows.put(contact, conversationWindow);
    }
}


package javaim.client.ui;

import javaim.client.ui.View;
import javaim.client.ui.MessageSentListener;

import java.io.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.SwingUtilities;

public class ContactsList extends JList<String> {

    private MessageSentListener messageSentListener;
    private View view;

    public ContactsList(final View view) {
        this.view = view;
        final String[] contacts = new String[] { "Alice", "Bob", "Carl", "Dave" };
        setListData(contacts);

        final JList<String> list = this;

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() != 2) {
                    return;
                }

                final String contact = contacts[list.getSelectedIndex()];

                SwingUtilities.invokeLater(new Runnable() {
                    private ConversationWindow conversationWindow;

                    public void run() {
                        conversationWindow = new ConversationWindow(view,
                                contact);
                        if (messageSentListener != null) {
                            conversationWindow.setMessageSentListener(
                                    messageSentListener);
                        }
                        view.onConversationWindowCreate(contact,
                                conversationWindow);
                    }
                });
            }
        });
    }

    public void openConversationWindow(final String contact) {
        SwingUtilities.invokeLater(new Runnable() {
            private ConversationWindow conversationWindow;

            public void run() {
                conversationWindow = new ConversationWindow(view, contact);
                if (messageSentListener != null) {
                    conversationWindow.setMessageSentListener(
                            messageSentListener);
                }
                view.onConversationWindowCreate(contact, conversationWindow);
            }
        });
    }

    public void setMessageSentListener(MessageSentListener listener) {
        messageSentListener = listener;
    }
}


package javaim.client.ui;

import javaim.client.ui.View;

import java.io.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.SwingUtilities;

public class ContactsList extends JList<String> {

    private ActionListener messageSentListener;

    public ContactsList(final View view) {
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
                        conversationWindow = new ConversationWindow(contact);
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

    public void setMessageSentListener(ActionListener listener) {
        messageSentListener = listener;
    }
}

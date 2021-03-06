
package javaim.client.view.gui.conversation;

import javaim.client.view.View;
import javaim.client.view.gui.conversation.MainPane;
import javaim.client.view.event.MessageSentListener;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class ConversationWindow extends JFrame {

    private static final String WINDOW_TITLE_PREFIX = "Conversation with ";
    private static final Dimension DEFAULT_DIMENSION = new Dimension(400, 200);
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit()
            .getScreenSize();
    private MainPane contentPane;
    private View view;
    private String contact;

    public ConversationWindow(final View view, final String contact) {
        this.view = view;
        this.contact = contact;
        setTitle(WINDOW_TITLE_PREFIX + contact);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(DEFAULT_DIMENSION);
        setLocation((SCREEN_SIZE.width / 2) - (getWidth() / 2),
                (SCREEN_SIZE.height / 2) - (getHeight() / 2));

        contentPane = new MainPane(this, this.contact);
        setContentPane(contentPane);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                view.removeConversationWindow(contact);
            }
        });

        pack();
        setVisible(true);
    }

    public String getContact() {
        return contact;
    }

    public void showMessage(String message) {
        contentPane.showMessage(message);
    }

    public void setMessageSentListener(MessageSentListener listener) {
        contentPane.setMessageSentListener(listener);
    }
}

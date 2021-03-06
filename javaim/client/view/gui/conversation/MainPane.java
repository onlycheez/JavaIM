
package javaim.client.view.gui.conversation;

import javaim.client.view.View;
import javaim.client.view.event.MessageSentListener;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class MainPane extends JPanel {

    private JTextArea textArea;
    private JTextField textField;
    private String contact;

    public MainPane(ConversationWindow parent, final String contact) {
        this.contact = contact;
        setLayout(new BorderLayout());

        JPanel centerPane = new JPanel();
        centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.PAGE_AXIS));

        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(parent.getWidth(), 200));
        textArea.setEditable(false);
        centerPane.add(textArea, BorderLayout.CENTER);

        textField = new JTextField();
        textField.setPreferredSize(new Dimension(parent.getWidth(), 20));
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                textArea.append("Me: " + event.getActionCommand() + "\n");
                textField.setText("");
            }
        });
        centerPane.add(textField, BorderLayout.CENTER);

        add(centerPane, BorderLayout.CENTER);
    }

    public void showMessage(String message) {
        textArea.append(contact + ": " + message + "\n");
    }

    public void setMessageSentListener(MessageSentListener listener) {
        listener.setContact(contact);
        textField.addActionListener(listener);
    }
}

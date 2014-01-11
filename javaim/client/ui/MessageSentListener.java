
package javaim.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implement this object when setting action for message sent on view.
 * Class extends ActionListener to conviniently access addressee and message
 * content.
 *
 * view.setMessageSent(new MessageSentListener() {
 *     public void messageSent(String to, Strin message) {
 *         request.postMessage(to, message);
 *     }
 * });
 */
public class MessageSentListener implements ActionListener {

    private String contact;

    /**
     * This method must not be overwritten by implementator.
     * Implementation goes to messageSent().
     */
    public void actionPerformed(ActionEvent event) {
        messageSent(contact, event.getActionCommand());
    }

    /**
     * Set the contact.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * This method is overwritten by implementator.
     */
    public void messageSent(String to, String message) {
        ;
    }
}

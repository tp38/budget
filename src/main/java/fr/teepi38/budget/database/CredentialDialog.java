package fr.teepi38.budget.database;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * CredentialDialog provides a dialog box to define the login and password
 * 
 * @author : <a href="mailto:thierry.probst@free.fr">Thierry Probst</a>
 * @version 0.0 08/06/2026
 */
public class CredentialDialog extends JDialog {
    private JTextField login_text;
    private JPasswordField pwd_text;

    private JButton valid_btn;
    private JButton cancel_btn;
    
    private Credential cred;
    private boolean valid = false;

    /**
     * constructor 
     * 
     * @param parent the parent frame
     */
    public CredentialDialog( Frame parent ) {
        super( parent, "Login", true );
        setName( "credential_dlg");

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();

        setLayout( gridbag );

        Dimension std_dim = new Dimension( 150, 25);

        gc.insets = new Insets(5,5,5,5);
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0; gc.gridy = 0;
        JLabel login_lbl = new JLabel( "Login : " );
        login_lbl.setName( "login_lbl" );
        add( login_lbl, gc );

        gc.gridx = 1;
        login_text = new JTextField();
        login_text.setName( "login_field" );
        login_text.setPreferredSize(std_dim);
        add( login_text, gc );

        gc.gridx = 0; gc.gridy = 1 ;
        JLabel pwd_lbl = new JLabel( "Password : " );
        pwd_lbl.setName( "pwd_lbl" );
        add( pwd_lbl, gc );

        gc.gridx = 1;
        pwd_text = new JPasswordField();
        pwd_text.setName( "pwd_field" );
        pwd_text.setPreferredSize(std_dim);
        add( pwd_text, gc );

        gc.gridx = 0; gc.gridy = 2 ;
        cancel_btn = new JButton( "Annuler" );
        cancel_btn.setName( "cancel_btn" );
        cancel_btn.setPreferredSize(std_dim);
        cancel_btn.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e ) {
                    dispose();
                }
            }
        );
        add( cancel_btn, gc );

        gc.gridx = 1;
        valid_btn = new JButton( "Valider" );
        valid_btn.setName( "valid_btn");
        valid_btn.addActionListener( new CredentialDialogListener() );
        add( valid_btn, gc ); 

        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * 
     * @return
     */
    public Credential getCredential() {
        if( valid ) {
            return cred;
        } 
        return null;
    }

    /**
     * test if a valid credential object has been defined by the user
     * @return a boolean
     */
    public boolean isCredentialValid() {
        return valid;
    }
    
    /**
     * a listener for this credential dialog
     * @author thierry.probst@free.fr
     * @see java.awt.event.ActionListener
     */
    class CredentialDialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if( Credential.isLoginValid( login_text.getText()  ) && 
                Credential.isPasswordValid( new String( pwd_text.getPassword() ) ) ) {
                cred = new Credential( login_text.getText(), new String( pwd_text.getPassword() ) );
                valid = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(CredentialDialog.this, 
                    "Login et/ou Password invalide",
                    "Login erreur",
                    JOptionPane.ERROR_MESSAGE );
                login_text.setText("");
                pwd_text.setText("");
                valid = false;
            }
        }
    }

}

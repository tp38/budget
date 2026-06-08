package fr.teepi38.budget.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CredentialDialogTests {
    private DialogFixture df;

    class InnerClassFrame extends JFrame {
        public InnerClassFrame() {
            // add( new CredentialDialog( this ) );
            pack();
        }
    }    

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        JDialog dlg = GuiActionRunner.execute( () -> { 
            JFrame frame = new InnerClassFrame();
            return new CredentialDialog( frame );
        } );
        df = new DialogFixture(dlg);
        df.show();
    }

    @Test
    public void should_have_credentialDlg_name() 
    throws Exception
    { 
        df.dialog("credential_dlg");
        Thread.sleep( 3000 );
    }

    @Test
    public void should_have_labels() { 
        df.dialog("credential_dlg").label("login_lbl").requireVisible();
        df.dialog("credential_dlg").label("pwd_lbl").requireVisible();
    }

    @Test
    public void should_be_invalid_when_login_and_password_are_empty() { 
        CredentialDialog dlg = (CredentialDialog)df.dialog("credential_dlg").target();
        df.dialog("credential_dlg").textBox("login_field").requireVisible();
        df.dialog("credential_dlg").textBox("pwd_field").requireVisible();
        df.dialog("credential_dlg").button("valid_btn").requireText("Valider").requireVisible().click();
        // credential isn't valid
        assertFalse( dlg.isCredentialValid() );
    }

    @Test
    public void should_be_valid_when_login_and_password_are_correct() { 
        CredentialDialog dlg = (CredentialDialog)df.dialog("credential_dlg").target();
        df.dialog("credential_dlg").textBox("login_field").requireVisible().setText("th");
        df.dialog("credential_dlg").textBox("pwd_field").requireVisible().setText("thierry");
        df.dialog("credential_dlg").button("valid_btn").requireText("Valider").requireVisible().click();
        // credential is valid
        assertTrue( dlg.isCredentialValid() );

        // credential is correct
        Credential expected_cred = new Credential("th", "thierry");
        assertEquals(expected_cred, dlg.getCredential() ); 
    }

    @Test
    public void should_have_a_cancel_button() { 
        CredentialDialog dlg = (CredentialDialog)df.dialog("credential_dlg").target();
        df.dialog("credential_dlg").button("cancel_btn").requireText("Annuler").requireVisible().click();
        assertFalse( dlg.isVisible() );
    }

    @AfterEach
    public void tearDown() {
        df.cleanUp();
    }    
}

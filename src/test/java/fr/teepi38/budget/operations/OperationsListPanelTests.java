package fr.teepi38.budget.operations;

import javax.swing.JFrame;

import java.net.URI;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import fr.teepi38.budget.database.RelaxDb;
import  fr.teepi38.budget.database.Credential;


class OperationsListPanelTests {
    private FrameFixture ff;
    // mockserver host
    private String ms_host = "localhost";
    // mockserver port
    private String ms_port = "1080";
    private RelaxDb db;

    class InnerClassFrame extends JFrame {
        public InnerClassFrame() {
            try {
                db = RelaxDb.builder()
                    .url(new URI("http://"+ms_host+":"+ms_port+"/budget"))
                    .credential(Credential
                        .builder()
                        .login("couchdb")
                        .pwd("couchdb")
                        .build()
                    )
                    .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

            add( new OperationsListPanel(db, null) );
            pack();
        }
    }    

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        JFrame frame = GuiActionRunner.execute( () -> new InnerClassFrame() );
        ff = new FrameFixture(frame);
        ff.show();
    }


    @Test
    @Disabled 
    public void show() 
        throws InterruptedException 
    {
        ff.list("list_without_name").requireVisible();
        Thread.sleep(3000);
    }


    @AfterEach
    public void tearDown() {
        ff.cleanUp();
    }    
}




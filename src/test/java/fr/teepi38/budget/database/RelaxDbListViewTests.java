package fr.teepi38.budget.database;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RelaxDbListViewTests {
    private FrameFixture ff;
    private Vector<RelaxDb> dbs;

    class DbChooserFrame extends JFrame {
        public DbChooserFrame() {
            RelaxDbListModel model = new RelaxDbListModel(dbs);
            add( new RelaxDbListView( this, model ) );
            pack();
        }
    }    

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // we must read data from databases.json
        try {
            InputStream json = new FileInputStream( "src/test/resources/databases.json" );
            dbs = mapper.readValue( json, mapper.getTypeFactory()
                    .constructCollectionType(Vector.class, RelaxDb.class) );
            json.close();
        } catch( Exception e) {
            e.printStackTrace();
        }

        JFrame frame = GuiActionRunner.execute( () -> new DbChooserFrame() );
        ff = new FrameFixture(frame);
        ff.show();
    }

  
    @Test
    public void should_read_data_from_databases_json_file() {
        ArrayList<String> expected_strings = new ArrayList<>();
        // see onSetup method
        dbs.forEach( db -> expected_strings.add( db.summary() ) );
               
        String[] values = ff.list("db_list").contents();
        assertArrayEquals( expected_strings.toArray(), values);
    }

    @Test
    public void list_should_be_in_single_selection_mode() 
    // throws InterruptedException 
    {
        ff.label("databases_lbl").requireVisible();
        ff.list("db_list").requireVisible();
        // Thread.sleep(3000);
        assertEquals( ListSelectionModel.SINGLE_SELECTION, ff.list("db_list").target().getSelectionMode() );
    }

    @Test
    public void should_have_a_connect_button() {
        ff.button("connect_btn").requireText("Connexion").requireVisible();
    }

    @Test
    public void should_open_a_credential_dialog_when_clicking_on_item() {
        ff.list("db_list").item(0).click();
        ff.dialog("credential_dlg").requireVisible();
    }

    @AfterEach
    public void tearDown() {
        ff.cleanUp();
    }
}

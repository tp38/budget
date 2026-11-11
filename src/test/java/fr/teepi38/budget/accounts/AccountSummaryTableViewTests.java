package fr.teepi38.budget.accounts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class AccountSummaryTableViewTests {
    private FrameFixture ff;
    private Vector<AccountSummary> accounts;

    class AccountSummaryFrame extends JFrame {
        public AccountSummaryFrame() {
            AccountSummaryTableModel model = new AccountSummaryTableModel(accounts);
            add( new AccountSummaryTableView( this, model ) );
            pack();
        }
    }    

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        accounts = new Vector<>();

        String[] k1 = { "BSA_CC_Nat" };
        Float[] v1 = { Float.valueOf( 690.69f ), Float.valueOf( 158.23f ), Float.valueOf( 784.23f )  };
        AccountSummary as1 = AccountSummary.builder().key(k1).value(v1).build();
        accounts.add( as1 );

        String[] k2 = { "BSA_CC_Th" };
        Float[] v2 = { Float.valueOf( 27.42f ), Float.valueOf( 498.13f ), Float.valueOf( 45.18f )  };
        AccountSummary as2 = AccountSummary.builder().key(k2).value(v2).build();
        accounts.add( as2 );

        String[] k3 = { "LaBP_CC_Joint" };
        Float[] v3 = { Float.valueOf( 37.43f ), Float.valueOf( 1568.10f ), Float.valueOf( 655.14f )  };
        AccountSummary as3 = AccountSummary.builder().key(k3).value(v3).build();
        accounts.add( as3 );

        JFrame frame = GuiActionRunner.execute( () -> new AccountSummaryFrame() );
        ff = new FrameFixture(frame);
        ff.show();
    }

    @Test
    public void Table_should_not_be_editable() {
        for( Integer i=0; i < accounts.size(); i++ ) {
            for( Integer j=0; j < 4; j++) {
                assertFalse( ff.table("account_table").requireVisible().target().isCellEditable(i, j) );
            }
        }
    }
    
    @Test
    public void Table_should_be_in_row_selection_mode_only() 
    {
        ff.table("account_table").requireVisible();
        assertTrue( ff.table("account_table").target().getRowSelectionAllowed() );
        assertFalse( ff.table("account_table").target().getColumnSelectionAllowed() );
    }

    @Test
    public void should_have_headers() {
        assertEquals( "", ff.table("account_table").target().getColumnName(0) );
        assertEquals( "Pointés", ff.table("account_table").target().getColumnName(1) );
        assertEquals( "Réalisés", ff.table("account_table").target().getColumnName(2) );
        assertEquals( "A venir", ff.table("account_table").target().getColumnName(3) );
    }

    @Test
    public void can_retrieve_account_name() {
        assertEquals( "BSA_CC_Nat", ff.table("account_table").target().getValueAt(0, 0).toString() );
        assertEquals( "BSA_CC_Th", ff.table("account_table").target().getValueAt(1, 0).toString() );
        assertEquals( "LaBP_CC_Joint", ff.table("account_table").target().getValueAt(2, 0).toString() );
    }

    @Test
    @Disabled 
    public void show() 
        throws InterruptedException 
    {
        ff.table("account_table").requireVisible();
        Thread.sleep(3000);
    }

    @Test
    public void shouldOpenTheRegisteredListener() {
        JTable table = ff.table("account_table").target();
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if( ! e.getValueIsAdjusting() ) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    String account = table.getValueAt(row, 0).toString();
                    JOptionPane.showMessageDialog( null, row + "," + col + " : " + account );
                }
            }
        } );
        ff.table("account_table").selectRows(1);
        ff.optionPane().requireMessage("1,0 : BSA_CC_Th");
    }

    @AfterEach
    public void tearDown() {
        ff.cleanUp();
    }
}

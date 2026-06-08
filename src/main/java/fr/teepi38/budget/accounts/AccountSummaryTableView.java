package fr.teepi38.budget.accounts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

//import fr.teepi38.budget.accounts.CallBack;

public class AccountSummaryTableView extends JPanel {

    AccountSummaryTableModel model;
    JFrame parent;
    JTable account_table;
    AccountPanelCallBack cb;

    public AccountSummaryTableView( JFrame parent, AccountSummaryTableModel model, AccountPanelCallBack cb ) {
        super();
        this.parent = parent;
        this.model = model;
        this.cb = cb;

        setName( "accounts_pnl" );


        account_table = new JTable(this.model);
        account_table.setName( "account_table" );
        account_table.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        account_table.setRowSelectionAllowed(true);
        account_table.setRowHeight(35);
        account_table.setColumnSelectionAllowed(false);
        account_table.getColumnModel().getColumn(0).setPreferredWidth(200);
        account_table.getColumnModel().getColumn(1).setPreferredWidth(100);
        account_table.getColumnModel().getColumn(2).setPreferredWidth(100);
        account_table.getColumnModel().getColumn(3).setPreferredWidth(100);
        account_table.setShowGrid(false);
        account_table.getSelectionModel().addListSelectionListener( new ASTVListener() );
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        AccountSummaryTableRenderer renderer = new AccountSummaryTableRenderer();
        account_table.setDefaultRenderer( Float.class, renderer );
        account_table.setDefaultRenderer( String.class, renderer );

        account_table.setPreferredScrollableViewportSize(account_table.getPreferredSize());
        JScrollPane pane = new JScrollPane( account_table );
        add(pane);
    }
    

    public void setUpTableData( AccountSummaryTableModel newModel ) {
        this.model = newModel;
        account_table.setModel(this.model);
    }

    class ASTVListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if( ! e.getValueIsAdjusting() ) {
                int row = account_table.getSelectedRow();
                int col = account_table.getSelectedColumn();
                String account = account_table.getValueAt(row, 0).toString();
                if( cb != null ) {
                    cb.onCall( row, col, account );
                }
            }
        }
    }
    
    class AccountSummaryTableRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // foreground Color
            if( column == 1 ) { setForeground( Color.BLUE );  }
            else if( column == 2 ) { setForeground( Color.ORANGE); }
            else if( column == 3 ) { setForeground( Color.DARK_GRAY ); }
            else { setForeground( Color.BLACK ); }

            // background Color
            if( isSelected ) { setBackground( new Color( 15, 249, 255 ) ); }
            else { setBackground( new Color( 255, 255, 255 ) ); }

            // text
            String text = null;
            if( column == 0 ) { text = (String)value; setHorizontalAlignment(SwingConstants.LEFT); }
            else { text = ((Float)value).toString(); setHorizontalAlignment( SwingConstants.RIGHT ); }
            setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10));
            if( isSelected ) {
                setFont( new Font("C59", Font.BOLD, 14) );
            } else {
                setFont( new Font("C59", Font.PLAIN, 14) );
            }
            setText(text);

            return this;
        }

    }
}

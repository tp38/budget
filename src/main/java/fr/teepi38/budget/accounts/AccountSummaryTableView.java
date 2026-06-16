package fr.teepi38.budget.accounts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class AccountSummaryTableView extends JPanel {

    AccountSummaryTableModel model;
    JFrame parent;
    JTable account_table;

    public AccountSummaryTableView( JFrame parent, AccountSummaryTableModel model ) {
        super( new BorderLayout() );
        this.parent = parent;
        this.model = model;

        setName( "accounts_pnl" );

        account_table = new JTable(model);
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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        AccountSummaryTableRenderer renderer = new AccountSummaryTableRenderer();
        account_table.setDefaultRenderer( Float.class, renderer );
        account_table.setDefaultRenderer( String.class, renderer );

        account_table.setPreferredScrollableViewportSize(account_table.getPreferredSize());
        JScrollPane pane = new JScrollPane( account_table );
        add( pane, BorderLayout.CENTER );
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

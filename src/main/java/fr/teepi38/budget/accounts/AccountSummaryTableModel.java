package fr.teepi38.budget.accounts;


import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class AccountSummaryTableModel extends AbstractTableModel {
    private String[] columnNames = { "", "Pointés", "Réalisés", "A venir" };
    private Object[][] data;


    public AccountSummaryTableModel( Vector<AccountSummary> init_data ) {

        data = new Object[ init_data.size()][4];
        for( Integer i = 0; i < init_data.size() ; i ++ ) {
            AccountSummary input = init_data.get(i); 
            data[i][0] = input.key()[0];
            data[i][1] = input.value()[0];
            data[i][2] = input.value()[1];
            data[i][3] = input.value()[2];
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName( int col ) {
        return columnNames[col];
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }   
    
    public Class<?> getColumnClass( int c ) {
        return getValueAt(0, c).getClass();
    }
}
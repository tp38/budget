package fr.teepi38.budget;

import static org.junit.Assert.assertEquals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateSelectorTests {
    private FrameFixture ff;
    private DateSelector dp;

    class InnerClassFrame extends JFrame {
        public InnerClassFrame() {
            dp = new DateSelector();
            add( dp );
            pack();
        }
    }    

    class btnActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Go for it!");
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
    public void should_register_a_listener() {
        dp.addActionListener( new btnActionListener() );
        ff.button("date_valid_btn").click();
        ff.optionPane().requireMessage("Go for it!" );
    }

    @Test
    public void should_have_a_getDate_method() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        assertEquals(sdf.format(today), dp.getDate());
    }

    @Test
    public void should_have_a_getLastDayOfMonth_method() {
        Calendar calendar = Calendar.getInstance();
        int dd = calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
        int mm = calendar.get( Calendar.MONTH ) + 1;
        int yy = calendar.get( Calendar.YEAR );
        String lastday = String.format("%02d/%02d/%4d", dd, mm, yy);

        assertEquals(lastday, dp.getLastDayOfMonth());
    }


    @Test
    public void show() 
    throws InterruptedException 
    {
        ff.spinner("date_spinner").requireVisible();
        Thread.sleep(3000);
    }

    @AfterEach
    public void tearDown() {
        ff.cleanUp();
    }    
}

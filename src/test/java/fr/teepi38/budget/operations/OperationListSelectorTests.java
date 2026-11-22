package fr.teepi38.budget.operations;

import javax.swing.JFrame;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationsListSelectorTests {
    private FrameFixture ff;

    class InnerClassFrame extends JFrame {
        public InnerClassFrame() {
            add( new OperationsListSelector() );
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
    public void test() {

    }
    
    @AfterEach
    public void tearDown() {
        ff.cleanUp();
    }    
}


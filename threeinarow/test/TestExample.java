import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import controller.ThreeInARowGame;
import model.ThreeInARowBlock;
import view.ThreeInARowView;

/**
 * An example test class, which merely shows how to write JUnit tests.
 */
public class TestExample {
    private ThreeInARowGame game;

    @Before
    public void setUp() {
	    game = new ThreeInARowGame();
    }

    @After
    public void tearDown() {
	    game = null;
    }

    @Test
    public void testNewGame() {
        assertEquals ("X", game.player);
        assertEquals (9, game.movesLeft);
    }

    @Test
    public void testFirstMove() {
        game.move(2, 0);
        assertEquals("O", game.player);
        assertEquals(8, game.movesLeft);
    }

    @Test
    public void testReset() {
        game.move(2, 0);
        assertEquals("O", game.player);
        assertEquals(8, game.movesLeft);
        game.resetGame();
        assertEquals("X", game.player);
        assertEquals(9, game.movesLeft);
    }
    @Test
    public void testNewView() {
        for (int i = 0; i < game.N; i ++) {
            for (int j = 0; j < game.N; j ++) {
                assertEquals(0, game.getView().getButtonText(i, j).length());
            }
        }
    }

    public static final String GAME_END_NOWINNER = "Game ends in a draw";

    @Test
    public void testDraw() {
        game.move(2, 1);
        game.move(2, 0);
        game.move(1, 1);
        game.move(0, 1);
        game.move(1, 2);
        game.move(1, 0);
        game.move(0, 2);
        game.move(2, 2);
        game.move(0, 0);
        assertEquals(0, game.movesLeft);
        assertEquals(GAME_END_NOWINNER, game.getView().getTopText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewBlockViolatesPrecondition() {
	    ThreeInARowBlock block = new ThreeInARowBlock(null);
    }

    @Test
    public void testBlockSetContents() {
	    ThreeInARowBlock block = new ThreeInARowBlock(game);
        block.setContents("O");
        assertEquals("O", block.getContents());
    }

    @Test
    public void testBlockSetLegalMove() {
	    ThreeInARowBlock block = new ThreeInARowBlock(game);
        block.setIsLegalMove(true);
        assertEquals(true, block.getIsLegalMove());
        block.setIsLegalMove(false);
        assertEquals(false, block.getIsLegalMove());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNewViewPrecondition() {
	    ThreeInARowView view = new ThreeInARowView(null);
    }
}

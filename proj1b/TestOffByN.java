import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(5);

    @Test
    public void testOffByN1() {
        assertTrue(offByN.equalChars('a', 'f'));
    }

    @Test
    public void testOffByN2() {
        assertTrue(offByN.equalChars('f', 'a'));
    }

    @Test
    public void testOffByN3() {
        assertFalse(offByN.equalChars('h', 'f'));
    }
}

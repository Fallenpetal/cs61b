import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testOffByOne1() {
        assertTrue(offByOne.equalChars('a', 'b'));
    }

    @Test
    public void testOffByOne2() {
        assertFalse(offByOne.equalChars('a', 'z'));
    }

    @Test
    public void testOffByOne3() {
        assertTrue(offByOne.equalChars('f', 'e'));
    }

    @Test
    public void testOffByOne4() {
        assertTrue(offByOne.equalChars('%', '&'));
    }

    @Test
    public void testOffByOne5() {
        assertFalse(offByOne.equalChars('x', 'Y'));
    }
}

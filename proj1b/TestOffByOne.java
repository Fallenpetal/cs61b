import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void TestOffByOne1(){
        assertTrue(offByOne.equalChars('a','b'));
    }

    @Test
    public void TestOffByOne2(){
        assertFalse(offByOne.equalChars('a','z'));
    }

    @Test
    public void TestOffByOne3(){
        assertTrue(offByOne.equalChars('f','e'));
    }

    @Test
    public void TestOffByOne4(){
        assertTrue(offByOne.equalChars('%','&'));
    }
}

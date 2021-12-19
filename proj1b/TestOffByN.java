import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(5);

    @Test
    public void TestOffByN1(){
        assertTrue(offByN.equalChars('a','f'));
    }

    @Test
    public void TestOffByN2(){
        assertTrue(offByN.equalChars('f','a'));
    }

    @Test
    public void TestOffByN3(){
        assertFalse(offByN.equalChars('h','f'));
    }
}

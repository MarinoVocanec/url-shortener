package vocanec.marino.urlshortener.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlphanumericEncoderTests {

    @Autowired
    private AlphanumericEncoder alphanumericEncoder;

    private void equals(String expected, int value) {
        Assert.assertEquals(expected, alphanumericEncoder.encode(value, 0));
    }

    @Test
    public void oneCharacterEncoding() {
        equals("b", 1);
        equals("c", 2);
        equals("d", 3);
        equals("e", 4);

        equals("A", 26);
        equals("B", 27);
        equals("C", 28);
        equals("D", 29);

        equals("0", 52);
        equals("1", 53);
        equals("2", 54);
        equals("3", 55);
    }

    @Test
    public void twoCharactersEncoding() {
        equals("ab", 62);
        equals("bb", 63);
        equals("ac", 124);
        equals("cc", 126);
    }

    @Test
    public void preambleLength() {
        Assert.assertEquals(3, alphanumericEncoder.encode(1, 2).length());
        Assert.assertEquals(5, alphanumericEncoder.encode(2, 4).length());
        Assert.assertEquals(11, alphanumericEncoder.encode(3, 10).length());

        Assert.assertEquals(2, alphanumericEncoder.encode(62, 0).length());
        Assert.assertEquals(3, alphanumericEncoder.encode(62, 1).length());
        Assert.assertEquals(4, alphanumericEncoder.encode(62, 2).length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroValue() {
        alphanumericEncoder.encode(0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeValue() {
        alphanumericEncoder.encode(-1, 0);
    }
}

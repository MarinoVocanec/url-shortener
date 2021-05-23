package vocanec.marino.urlshortener.utility;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomAlphanumericGeneratorTests {

    @Autowired
    private RandomAlphanumericGenerator randomAlphanumericGenerator;

    @Test
    public void alphanumericLength() {
        for(int length = 1; length <= 100; length++) {
            String result = randomAlphanumericGenerator.generateAlphanumericString(length);
            Assert.assertEquals(result.length(), length);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroLength() {
        randomAlphanumericGenerator.generateAlphanumericString(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() {
        randomAlphanumericGenerator.generateAlphanumericString(-1);
    }
}

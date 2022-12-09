import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashTest {

    HashTable quotes = new HashTable();

    @BeforeEach
    public void initialize() {

        quotes.put("Henry Ford", "Thinking is the hardest work there is, which is probably the reason why so few engage in it.");
        quotes.put("Albert Camus", "Don't walk behind me; I may not lead. Don't walk in front of me; I may not follow. Just walk beside me and be my friend.");
        quotes.put("Clint Eastwood", "I have a very strict gun control policy: if there's a gun around, I want to be in control of it.");
        quotes.put("Napoleon Bonaparte", "You must not fight too often with one enemy, or you will teach him all your art of war.");
        quotes.put("Helen Keller", "Walking with a friend in the dark is better than walking alone in the light.");
        quotes.put("Voltaire", "It is dangerous to be right in matters on which the established authorities are wrong.");
        quotes.put("Malcolm X", "Education is the passport to the future, for tomorrow belongs to those who prepare for it today.");
    }


    @Test
    public void containsTest1() {
        Assertions.assertTrue(quotes.containsKey("Malcolm X"));
    }

    @Test
    public void containsTest2() {
        Assertions.assertFalse(quotes.containsKey("Malcolm Gladwell"));
    }

    @Test
    public void getTest1() {
        Assertions.assertEquals("Walking with a friend in the dark is better than walking alone in the light.",
                quotes.get("Helen Keller"));
    }

    @Test
    public void getTest2() {
        Assertions.assertNull(quotes.get("Aristotle"));
    }

    @Test
    public void putTest1() {
        Assertions.assertTrue(quotes.put("Socrates", "The only true wisdom is in knowing you know nothing."));
    }

    @Test
    public void putTest2() {
        Assertions.assertFalse(quotes.put("Malcolm X", "If you have no critics you'll likely have no success."));
    }
    @Test
    public void putTest3() {
        for (int i=0;i<81;i++){
            String num=""+i;
         quotes.put(num,num);
            Assertions.assertTrue(quotes.containsKey(""+i));
        }
        System.out.println(quotes.containsKey("80"));
    }
}

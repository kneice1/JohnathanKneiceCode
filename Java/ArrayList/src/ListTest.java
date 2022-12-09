
    import org.junit.jupiter.api.*;

    public class ListTest {

        private NewList list = new NewList();

        @BeforeEach
        public void addElements() {
            list.add("Every");
            list.add("Good");
            list.add("Boy");
            list.add("Deserves");
            list.add("hi");
            list.add("hil");
            list.add("hiq");
            list.add("hiw");
            list.add("s");
            list.add("huhu");
            list.add("kkkdkd");
            list.add("hi");
            list.add("hir");
        }

        @Test
        public void testGet() {
            Assertions.assertEquals("huhu", list.get(10));
        }

        @Test
        public void testGetException() {
            try {
                String value = list.get(16);
                Assertions.fail("An exception should have been thrown");
            }
            catch (IndexOutOfBoundsException e) {}
        }

        @Test
        public void testAddAll() {
            NewList list2 = new NewList();
            list2.add("I");
            list2.add("Believe");

            list2.addAll(list);

            Assertions.assertEquals("Boy", list2.get(4));
        }

        @Test
        public void testEmpty() {
            Assertions.assertFalse(list.isEmpty());

            list.clear();

            Assertions.assertTrue(list.isEmpty());
        }

        @Test
        public void testSize() {
            Assertions.assertEquals(4, list.size());
        }


        @Test
        public void testIndexOf() {
            list.remove(1);

            //noinspection StringOperationCanBeSimplified
            Assertions.assertEquals(2, list.indexOf(new String("Deserves")));
        }
        @Test
        public void testAdds(){
            list.add(2,"hiii");
            Assertions.assertEquals(3, list.indexOf(new String("hiii")));
        }
        @Test
        public void testContains(){
            Assertions.assertTrue(list.contains("hir"));
            System.out.print(list.contains("hi"));
        }
        @Test
        public void testLastIndex(){
            list.lastIndexOf("hi");
            Assertions.assertEquals(8, list.lastIndexOf(new String("hi")));
        }
        @Test
        public void testSet(){
            list.set(12,"hin");
            Assertions.assertTrue(list.contains("hin"));
        }
        @Test
        public void testRemove(){
            Assertions.assertTrue(list.contains("hir"));
            list.remove(12);
            Assertions.assertEquals(12,list.size());
            Assertions.assertFalse(list.contains("hir"));
        }
    }


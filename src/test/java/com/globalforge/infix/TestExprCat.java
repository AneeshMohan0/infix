package com.globalforge.infix;

import org.junit.Assert;
import org.junit.Test;
import com.globalforge.infix.api.InfixActions;
import com.google.common.collect.ListMultimap;

public class TestExprCat {
    static final String sampleMessage1 = "8=FIX.4.4" + '\u0001' + "9=10"
        + '\u0001' + "35=8" + '\u0001' + "43=-1" + '\u0001' + "-43=-1"
        + '\u0001' + "-44=1" + '\u0001' + "44=3.142" + '\u0001'
        + "60=20130412-19:30:00.686" + '\u0001' + "75=20130412" + '\u0001'
        + "45=0" + '\u0001' + "382=2" + '\u0001' + "375=1.5" + '\u0001'
        + "337=eb8cd" + '\u0001' + "375=3" + '\u0001' + "337=8dhosb" + '\u0001'
        + "10=004";
    static StaticTestingUtils msgStore = null;
    InfixActions rules = null;
    String sampleRule = null;
    String result = null;
    ListMultimap<Integer, String> resultStore = null;

    @Test
    public void testc1() {
        try {
            sampleRule = "&45=&45|1";
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(45).get(0), "01");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc2() {
        try {
            sampleRule = "&45=&45|0";
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(45).get(0), "00");
        } catch (Exception e) {
        }
    }

    @Test
    public void testc3() {
        try {
            sampleRule = "&382=&9|\"FOO\"";
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(382).get(0), "10FOO");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc4() {
        try {
            sampleRule = "&375=12|2|4";
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(375).get(2), "1224");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc5() {
        try {
            sampleRule = "&382[0]->&375=&382[1]->&375|&382[0]->&375"; // 3 / 1.5
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(375).get(0), "31.5");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc6() {
        try {
            sampleRule = "&382[1]->&375=&382[1]->&375|&382[0]->&375"; // 3 / 1.5
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(375).get(1), "31.5");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc7() {
        try {
            sampleRule =
                "&382[1]->&375=&44|&382[0]->&375|&9|&382[1]->&375|2|\"FOO\""; // 3.142
                                                                              // /
            // 1.5 /
            // 10 /
            // 3 / 2
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(375).get(1), "3.1421.51032FOO");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testc8() {
        try {
            sampleRule = "&382[1]->&375=\"BAR\"|\"FOO\"|\"ME\"|&9";
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals(resultStore.get(375).get(1), "BARFOOME10");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    // /////////
    @Test
    public void testd10() {
        try {
            sampleRule = "&382=&382 | &43"; // 2 | -1
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals("2-1", resultStore.get(382).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testd11() {
        try {
            sampleRule = "&382=&382 | &-44"; // 2 | 1
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals("21", resultStore.get(382).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testd12() {
        try {
            sampleRule = "&382=&43 | &-43"; // -1 | -1
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals("-1-1", resultStore.get(382).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testd13() {
        try {
            sampleRule = "&-15=&43 | &-44"; // -1 | 1
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals("-11", resultStore.get(-15).get(0));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void test13() {
        try {
            sampleRule = "&382[1]->&375=&43 | &-44"; // -1 | 1
            rules = new InfixActions(sampleRule);
            result = rules.transformFIXMsg(TestExprCat.sampleMessage1);
            resultStore = StaticTestingUtils.parseMessage(result);
            Assert.assertEquals("-11", resultStore.get(375).get(1));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}

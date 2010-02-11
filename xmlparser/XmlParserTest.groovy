import org.junit.*

class XmlParserTest {

    static XmlParserPool parserPool = new XmlParserPool(1000)

    static int iterations = 1000
    static int threadCount = 5

    static {
        // warm up the environment
        new XmlSlurper()
    }

    def xml = """
       <root>
         <node1 aName='aValue'>
           <node1.1 aName='aValue'>1.1</node1.1>
           <node1.2 aName='aValue'>1.2</node1.2>
           <node1.3 aName='aValue'>1.3</node1.3>
         </node1>
         <node2 aName='aValue'>
           <node2.1 aName='aValue'>2.1</node2.1>
           <node2.2 aName='aValue'>2.2</node2.2>
           <node2.3 aName='aValue'>2.3</node2.3>
         </node2>
         <nodeN aName='aValue'>
           <nodeN.1 aName='aValue'>N.1</nodeN.1>
           <nodeN.2 aName='aValue'>N.2</nodeN.2>
           <nodeN.3 aName='aValue'>N.3</nodeN.3>
         </nodeN>
       </root>
    """

    def parseSequential() {
        iterations.times {
            def parser = parserPool.borrowObject()
            def root = new XmlSlurper(parser).parseText(xml)
            parserPool.returnObject(parser)
            assert 'aValue' == root.node1.@aName.toString()
        }
    }

    @Test void testSequentialXmlParsing() {
        long start = System.currentTimeMillis()
        parseSequential()
        long stop = System.currentTimeMillis()
        println "${iterations} XML documents parsed sequentially in ${stop-start} ms"
    }

    @Test void testParallelXmlParsing() {
        def threads = []
        long start = System.currentTimeMillis()
        threadCount.times {
            threads << Thread.start { parseSequential() }
        }
        threads.each { it.join() }
        long stop = System.currentTimeMillis()
        println "${threadCount*iterations} XML documents parsed parallelly by ${threadCount} threads in ${stop-start} ms"
    }
}

package protocol

internal class EncoderKtTest {
    @org.junit.jupiter.api.Test
    fun testDSLParse() {
        println("ORDER (author) FILTER (author=.*,year<=1900)".asQuery())
    }

}
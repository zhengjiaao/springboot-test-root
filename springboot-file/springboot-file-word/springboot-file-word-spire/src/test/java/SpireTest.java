import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.junit.jupiter.api.Test;

/**
 * @author: zhengja
 * @since: 2024/01/19 15:18
 */
public class SpireTest {

    @Test
    public void test() {
        // 实例化Document类的对象
        Document doc = new Document();

        // 加载Word
        doc.loadFromFile("D:\\temp\\word\\test.docx");

        // 保存为PDF格式
        doc.saveToFile("D:\\temp\\word\\test.docx.pdf", FileFormat.PDF);
    }

}

import com.dtt.anms.data.batch.processing.DataBatchProcessing;
import com.dtt.anms.data.batch.processing.dao.KafkaBatchClickDao;
import com.dtt.anms.data.batch.processing.entity.KafkaBatchClick;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author wushiqiang
 * @date Created in 下午1:51 2024/12/20
 * @description
 */
@SpringBootTest(classes = DataBatchProcessing.class)
public class test {

    @Resource
    KafkaBatchClickDao kafkaBatchClickDao;

    @Test
    public void test11(){
//        List<KafkaBatchClick> batchClicks = kafkaBatchClickDao.getKfBachCli(2);
//        System.out.println(batchClicks);
        KafkaBatchClick batchClick = kafkaBatchClickDao.getKfBachCliSingle(2000002);
        System.out.println(batchClick);
    }
}

package zp.cdnu.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import zp.cdnu.server.service.IJobService;
import zp.cdnu.server.service.impl.JobServiceImpl;

import javax.annotation.Resource;

@SpringBootTest
class ServerApplicationTests {

    @Resource
    private IJobService jobService;

    @Test
    void contextLoads() {
        jobService.pageKeyword("java", 1);
    }

}

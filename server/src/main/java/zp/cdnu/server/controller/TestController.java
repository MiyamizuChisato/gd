package zp.cdnu.server.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zp.cdnu.server.common.annotation.UseToken;
import zp.cdnu.server.common.result.ServerException;
import zp.cdnu.server.common.result.Status;

@CrossOrigin
@RestController
@RequestMapping("/api/test")
public class TestController {
    @UseToken
    @GetMapping("/0")
    public String test() {
        return "错误的";
    }

    @GetMapping("/1")
    public String test1() {
        int b = 2 / 0;
        return String.valueOf(b);
    }

    @GetMapping("/2")
    public String test2() {
        throw new ServerException(Status.USER_ERROR).message("错误的");
    }
}

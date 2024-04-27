package com.leyunone.disk.controll;

import com.leyunone.disk.model.DataResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * :)
 *
 * @Author LeYunone
 * @Date 2024/4/25 15:58
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/getEnvironment")
    public DataResponse<String> getEnvironment() {
        return DataResponse.of("oss");
    }
}

package cn.byteswalk.eaglemqconsole.controller;

import cn.byteswalk.eaglemqconsole.service.INameServerService;
import cn.byteswalk.eaglemqconsole.vo.BaseResponseVO;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-03 18:09
 * @Description:
 * @Version: 1.0
 */
public class ConsoleController {
    @Resource
    private INameServerService nameServerService;

    //获取topic列表
    @GetMapping("/getTopicList")
    public BaseResponseVO<List> getTopicList() {
        return BaseResponseVO.success(nameServerService.getInfo());
    }
}


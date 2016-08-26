package com.iworker.bigdata.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iworker.bigdata.service.SaleTopRankService;
import com.iworker.bigdata.vo.ReturnObject;
import com.iworker.bigdata.vo.TopRankVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

import static com.iworker.bigdata.common.Constants.*;
/**
 *
 * 销售排行榜控制器
 *
 */


@RestController
@RequestMapping(value = "/report/sale")
public class SaleTopRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleTopRankController.class);

    @Autowired
    SaleTopRankService saleTopRankService;


    @RequestMapping(value = "/rank", method = RequestMethod.POST)
    public String getReport(@RequestBody String data){

        ReturnObject ret = new ReturnObject();

        try {



        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
            ret.setMessage(e.getMessage());
        }

        return null;
    }
}

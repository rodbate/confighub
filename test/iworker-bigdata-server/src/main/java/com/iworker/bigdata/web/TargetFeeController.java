package com.iworker.bigdata.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iworker.bigdata.service.TargetFeeService;
import com.iworker.bigdata.vo.TargetFeeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


/**
 *
 * 目标值设置控制器
 *
 */

@RestController
@RequestMapping(value = "/report")
public class TargetFeeController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(TargetFeeController.class);

    @Autowired
    TargetFeeService targetFeeService;


    /**
     * 添加设置指标目标值
     *
     * @param data json数据
     * @return ret
     */
    @RequestMapping(value = "/targetfee", method = RequestMethod.POST)
    public String addTargetFee(@RequestBody String data){

        JSONObject jsonObject = JSON.parseObject(data);
        String content = jsonObject.getString("data");

        JSONObject contentObj = JSON.parseObject(content);

        int companyId = contentObj.getInteger("companyId");
        int year = contentObj.getInteger("year");
        String dataList = contentObj.getString("dataList");
        String users = contentObj.getString("users");

        TargetFeeVO vo = new TargetFeeVO(companyId, year,
                JSON.parseArray(dataList, TargetFeeVO.MonthToAmount.class),
                JSON.parseObject(users, TargetFeeVO.Users.class));

        try {
            boolean retValue = targetFeeService.add(vo);
            if (retValue) {
                ret.setRet(1);
                ret.setMsg("指标设置成功");
            } else {
                ret.setRet(1000);
                ret.setMsg("指标设置失败");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ret.setRet(-1);
            ret.setMsg(e.getMessage());
        }

        return JSON.toJSONString(ret);
    }



    /**
     * 更新设置指标目标值
     *
     * @param data json数据
     * @return ret
     */
    @RequestMapping(value = "/targetfee", method = RequestMethod.PUT)
    public String updateTargetFee(@RequestBody String data){

        JSONObject jsonObject = JSON.parseObject(data);
        String content = jsonObject.getString("data");

        JSONObject contentObj = JSON.parseObject(content);

        int companyId = contentObj.getInteger("companyId");
        int year = contentObj.getInteger("year");
        String dataList = contentObj.getString("dataList");
        String users = contentObj.getString("users");



        TargetFeeVO vo = new TargetFeeVO(companyId, year,
                JSON.parseArray(dataList, TargetFeeVO.MonthToAmount.class),
                JSON.parseObject(users, TargetFeeVO.Users.class));

        try {
            boolean retValue = targetFeeService.modify(vo);
            if (retValue) {
                ret.setRet(1);
                ret.setMsg("指标更新成功");
            } else {
                ret.setRet(1000);
                ret.setMsg("指标更新失败");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ret.setRet(-1);
            ret.setMsg(e.getMessage());
        }

        return JSON.toJSONString(ret);
    }




}

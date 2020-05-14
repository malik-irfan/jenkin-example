package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.Controller;

import com.ef.clients.ptcl.UpgradedOneWindow.CustomLogs.EFLogger;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.ResponseModel.RebateResponse;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.Service.RebateService;
import com.ef.clients.ptcl.UpgradedOneWindow.Rebate.WSDLConsume.WSDLConsumer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;



@RestController
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/OneWindow/Rebate")

public class RebateController {

        private static final Logger logger = LogManager.getLogger(RebateController.class);

        @ApiOperation( value = "Web Service for Testing")
        @RequestMapping(value = "/Test",method = {RequestMethod.POST})
        @ApiResponses(value = {
                @ApiResponse(code = 404, message = "Not found"),
                @ApiResponse(code = 500, message = "Could not fetched")
        })
        //@RequestMapping(value="/prismTest",method = RequestMethod.GET)
        public String testing(){
            return "hello Rebate";
        }


    @ApiOperation(httpMethod = "GET",value = "Micro Service to load Rebate")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/LoadRebate")
    public String loadRebate(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,

            @RequestParam(value="Product",defaultValue ="") String product,
            @RequestParam(value="ProductId",defaultValue ="") String productId,

            @RequestParam(value="SROpenDate",defaultValue ="") String srOpenDate,
            @RequestParam(value="SRCloseDate",defaultValue ="") String srCloseDate,

            @RequestParam(value="Region",defaultValue ="") String region,
            @RequestParam(value="PricePlan",defaultValue ="") String pricePlan,
            @RequestParam(value="PricePlanId",defaultValue ="") String pricePlanId,

            @RequestParam(value="StateDate",defaultValue ="") String stateDate,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" loadRebate called with PSTN:"+pstn+" Product:"+product+" ProductId:"+productId
                +" SROpenDate:"+srOpenDate+" SRCloseDate:"+srCloseDate+" Region:"+region
                +" PricePlan:"+pricePlan+" PricePlanId:"+pricePlanId+" StateDate:"+stateDate
                +" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" <<============================================================================>>");
        agentLogger.info(time1+" loadRebate called with PSTN:"+pstn+" Product:"+product+" ProductId:"+productId
                +" SROpenDate:"+srOpenDate+" SRCloseDate:"+srCloseDate+" Region:"+region
                +" PricePlan:"+pricePlan+" PricePlanId:"+pricePlanId+" StateDate:"+stateDate
                +" AgentId:"+agentId);

        if (pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                product.equals("undefined") || StringUtils.isEmpty(product) ||
                productId.equals("undefined") || StringUtils.isEmpty(productId) ||
                srOpenDate.equals("undefined") || StringUtils.isEmpty(srOpenDate) ||
                srCloseDate.equals("undefined") || StringUtils.isEmpty(srCloseDate) ||
                region.equals("undefined") || StringUtils.isEmpty(region) ||
                pricePlan.equals("undefined") || StringUtils.isEmpty(pricePlan) ||
                pricePlanId.equals("undefined") || StringUtils.isEmpty(pricePlanId) ||
                stateDate.equals("undefined") || StringUtils.isEmpty(stateDate)
        ){
            logger.info(time1+" Wrong Input parameters PSTN:"+pstn+" Product:"+product+" ProductId:"+productId
                    +" SROpenDate:"+srOpenDate+" SRCloseDate:"+srCloseDate+" Region:"+region
                    +" PricePlan:"+pricePlan+" PricePlanId:"+pricePlanId+" StateDate:"+stateDate);

            agentLogger.info(time1+" Wrong Input parameters PSTN:"+pstn+" Product:"+product+" ProductId:"+productId
                    +" SROpenDate:"+srOpenDate+" SRCloseDate:"+srCloseDate+" Region:"+region
                    +" PricePlan:"+pricePlan+" PricePlanId:"+pricePlanId+" StateDate:"+stateDate);

            RebateResponse response = new RebateResponse();
            response.setErrorCode(4);
            response.setErrorDetail("Wrong Input parameters");
            returnResponse = WSDLConsumer.javaToJson(response);

        }else{

            returnResponse =  RebateService.loadRebate(pstn,product,productId,srOpenDate,srCloseDate,region,
                    pricePlanId,pricePlan,stateDate,agentId,agentLogger,time1);
        }

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;
    }



    @ApiOperation(httpMethod = "GET",value = "Micro Service for Finesse Calculator")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/FinesseCalculator")
    public String finesseCalculator(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" finesseCalculator called with PSTN:"+pstn+" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.info(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.info(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" <<============================================================================>>");
        agentLogger.info(time1+" finesseCalculator called with PSTN:"+pstn+" AgentId:"+agentId);

        if (pstn.equals("undefined") || StringUtils.isEmpty(pstn)){

            logger.info(time1+" Wrong Input parameters PSTN:"+pstn);
            agentLogger.info(time1+" Wrong Input parameters PSTN:"+pstn);

            RebateResponse response = new RebateResponse();
            response.setErrorCode(4);
            response.setErrorDetail("Wrong Input parameters");
            returnResponse = WSDLConsumer.javaToJson(response);

        }else{

            returnResponse =  RebateService.finesseCalculator(pstn,agentId,agentLogger,time1);
        }

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;
    }


    @ApiOperation(httpMethod = "GET",value = "Micro Service to Send Rebate SMS")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/SendRebateSMS")
    public String sendRebateSms(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="AreaCode",defaultValue ="") String areaCode,

            @RequestParam(value="Mobile",defaultValue ="") String mobile,
            @RequestParam(value="AccountId",defaultValue ="") String accountId,
            @RequestParam(value="Amount",defaultValue ="") String amount,

            @RequestParam(value="CustomerName",defaultValue ="") String customerName,
            @RequestParam(value="ComplaintNo",defaultValue ="") String complaintNo,
            @RequestParam(value="ComplaintType",defaultValue ="") String complaintType,

            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" Proceeding to Send Rebate SMS, Rebate of PKR:"+amount+"  to:"+mobile);
        logger.debug(time1+" Send Rebate SMS params, PSTN:"+pstn+" AreaCode:"+areaCode+" Mobile:"+mobile+" AccountId:"+accountId
                +" Amount:"+amount+" CustomerName:"+customerName+" ComplaintNo:"+complaintNo+" ComplaintType:"+complaintType+" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.debug(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.debug(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" <<============================================================================>>");
        agentLogger.info(time1+" Proceeding to Send Rebate SMS, Rebate of PKR:"+amount+"  to:"+mobile);
        agentLogger.debug(time1+" Send Rebate SMS params, PSTN:"+pstn+" AreaCode:"+areaCode+" Mobile:"+mobile+" AccountId:"+accountId
                +" Amount:"+amount+" CustomerName:"+customerName+" ComplaintNo:"+complaintNo+" ComplaintType:"+complaintType+" AgentId:"+agentId);

        if (    pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                areaCode.equals("undefined") || StringUtils.isEmpty(areaCode) ||
                mobile.equals("undefined") || StringUtils.isEmpty(mobile) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId) ||
                amount.equals("undefined") || StringUtils.isEmpty(amount) ||
                customerName.equals("undefined") || StringUtils.isEmpty(customerName) ||
                complaintNo.equals("undefined") || StringUtils.isEmpty(complaintNo) ||
                complaintType.equals("undefined") || StringUtils.isEmpty(complaintType)
        ){

            logger.info(time1+" Send Rebate SMS Status, Wrong Input parameters");
            agentLogger.info(time1+" Send Rebate SMS Status, Wrong Input parameters");

            RebateResponse response = new RebateResponse();
            response.setErrorCode(4);
            response.setErrorDetail("Wrong Input parameters");
            returnResponse = WSDLConsumer.javaToJson(response);

        }else{

            returnResponse =  RebateService.sendRebateSms(pstn,areaCode,mobile,accountId,amount,customerName,complaintNo,complaintType,agentId,agentLogger,time1);
        }

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.debug(time1+" Send Rebate SMS Status, Total Execution time: "+executionTime+" ms");
        logger.info(time1+" Send Rebate SMS Status, exit with  "+returnResponse);
        agentLogger.debug(time1+" Send Rebate SMS Status, Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Send Rebate SMS Status, exit with  "+returnResponse);
        return returnResponse;

    }


    @ApiOperation(httpMethod = "GET",value = "Micro Service to Send E-Bill Link SMS")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/SendEBillSMS")
    public String sendEBillLinkSMS(
            @RequestParam(value="PSTN",defaultValue ="") String pstn,
            @RequestParam(value="AreaCode",defaultValue ="") String areaCode,

            @RequestParam(value="Mobile",defaultValue ="") String mobile,
            @RequestParam(value="AccountId",defaultValue ="") String accountId,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){
        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" Proceeding to Send EB=-Bill Link SMS, to:"+mobile);
        logger.debug(time1+" Send E-Bill Link SMS params, PSTN:"+pstn+" AreaCode:"+areaCode+" Mobile:"+mobile+" AccountId:"+accountId +" AgentId:"+agentId);
        if (agentId.equals("undefined") || StringUtils.isEmpty(agentId)){
            logger.debug(time1+" AgentId not available going to put this logs in default");
            agentId = "default";
        }
        Logger agentLogger = Logger.getLogger(agentId);
        if (agentLogger.getAppender(agentId) == null) {
            logger.debug(time1+" New agent comes for logging, going to create logs file for "+agentId);
            Appender fileAppender = EFLogger.getAgentFileLogAppender(agentId);
            agentLogger.addAppender(fileAppender);
            agentLogger.setAdditivity(false);
        }
        agentLogger.info(time1+" <<============================================================================>>");
        agentLogger.info(time1+" Proceeding to Send EB=-Bill Link SMS, to:"+mobile);
        agentLogger.debug(time1+" Send E-Bill Link SMS params, PSTN:"+pstn+" AreaCode:"+areaCode+" Mobile:"+mobile+" AccountId:"+accountId +" AgentId:"+agentId);

        if (    pstn.equals("undefined") || StringUtils.isEmpty(pstn) ||
                areaCode.equals("undefined") || StringUtils.isEmpty(areaCode) ||
                mobile.equals("undefined") || StringUtils.isEmpty(mobile) ||
                accountId.equals("undefined") || StringUtils.isEmpty(accountId)
        ){

            logger.info(time1+" Send E-Bill Link SMS Status, Wrong Input parameters");
            agentLogger.info(time1+" Send E-Bill Link SMS Status, Wrong Input parameters");

            RebateResponse response = new RebateResponse();
            response.setErrorCode(4);
            response.setErrorDetail("Wrong Input parameters");
            returnResponse = WSDLConsumer.javaToJson(response);

        }else{

            returnResponse =  RebateService.sendEBillSms(pstn,areaCode,mobile,accountId,agentId,agentLogger,time1);
        }

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.debug(time1+" Send E-Bill Link SMS Status, Total Execution time: "+executionTime+" ms");
        logger.info(time1+" Send E-Bill Link SMS Status, exit with  "+returnResponse);
        agentLogger.debug(time1+" Send E-Bill Link SMS Status, Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Send E-Bill Link SMS Status, exit with  "+returnResponse);
        return returnResponse;

    }
}

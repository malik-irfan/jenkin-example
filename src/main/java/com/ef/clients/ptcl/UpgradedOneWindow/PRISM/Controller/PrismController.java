package com.ef.clients.ptcl.UpgradedOneWindow.PRISM.Controller;


import com.ef.clients.ptcl.UpgradedOneWindow.CustomLogs.EFLogger;
import com.ef.clients.ptcl.UpgradedOneWindow.PRISM.Service.PrismServiceAccess;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.ef.clients.ptcl.UpgradedOneWindow.PRISM.RestConsumer.*;

import java.util.ResourceBundle;

@RestController
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "*", allowedHeaders = "*")

@RequestMapping("/OneWindow/Prism")
public class PrismController {

    private static final Logger logger = LogManager.getLogger(PrismController.class);

    @ApiOperation( value = "Web Service for Testing")
    @RequestMapping(value = "/prismTest",method = {RequestMethod.POST})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Could not fetched")
    })
    //@RequestMapping(value="/prismTest",method = RequestMethod.GET)
    public String testing(){
        return "hello prism";
    }



    @ApiOperation(httpMethod = "GET",value = "Micro Service for Category Listing")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/CategoryList")
    public String getCategoryList(
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){



       /* String returnResponse = null;
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("rescode", "5");
        json.put("message","Prism Down");
        json.put("data",array);
        returnResponse = json.toString();
        return returnResponse;*/

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" GetCategoryList called with agent id:"+agentId);
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
        agentLogger.info(time1+" GetCategoryList called with agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.getCategoryList(agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }

    @ApiOperation(httpMethod = "GET",value = "Micro Service for Sub Categories Listing")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/SubCategoryList")
    public String getSubCategoryList(
            @RequestParam(value="CategoryId") String categoryId,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" GetSubCategoryList called with CategoryId:"+categoryId+" agent id:"+agentId);
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
        agentLogger.info(time1+" GetSubCategoryList called with CategoryId:"+categoryId+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.getSubCategoryList(categoryId,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }


    @ApiOperation(httpMethod = "GET", value = "Micro Service for Message Title Listing")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/MessageTitleList")
    public String getMessageTitleList(
            @RequestParam(value="SubCategoryId") String subCategoryId,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" getMessageTitleList called with SubCategoryId:"+subCategoryId+" agent id:"+agentId);
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
        agentLogger.info(time1+" getMessageTitleList called with SubCategoryId:"+subCategoryId+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.getMessageTitleList(subCategoryId,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }


    @ApiOperation(httpMethod = "GET", value = "Micro Service for Messages Listing")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/MessageList")
    public String getMessageList(
            @ApiParam(value = "Pass the multiple Message ID with Comma separate e.g. (1,2,3)",required = true)
            @RequestParam(value="MessageId") String messageId,
            @ApiParam(value = "1: For English Language 2: For Urdu Language 3: For Roman",required = true)
            @RequestParam(value="LangType") String languageType,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" getMessageList called with MessageId:"+messageId+" LanguageType:"+languageType+" agent id:"+agentId);
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
        agentLogger.info(time1+" getMessageList called with MessageId:"+messageId+" LanguageType:"+languageType+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.getMessageList(messageId,languageType,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }



    @ApiOperation(value = "Micro Service for Sending Messages to Customer")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })
    @RequestMapping(value = "/SendMessages", method = RequestMethod.POST)
    public String sendMessagesToCustomer(
            @ApiParam(value = "Pass the multiple Message ID with Comma separate e.g. (1,2,3)",required = true)
            @RequestParam(value="MessageId") String messageId,
            @ApiParam(value = "1: For English Language 2: For Urdu Language 3: For Roman",required = true)
            @RequestParam(value="LangType") String languageType,
            @RequestParam(value="Mobile") String mobile,
            @RequestParam(value="AgentName") String agentName,
            @RequestParam(value="AgentId",defaultValue ="") String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" sendMessagesToCustomer called with MessageId:"+messageId+" LanguageType:"+languageType+" Mobile:"+mobile+" AgentName:"+agentName+" agent id:"+agentId);
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
        agentLogger.info(time1+" sendMessagesToCustomer called with MessageId:"+messageId+" LanguageType:"+languageType+" Mobile:"+mobile+" AgentName:"+agentName+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.sendMessagesToCustomer(messageId,languageType,mobile,agentName,agentId,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }

    @ApiOperation(httpMethod = "GET", value = "Micro Service to Search Message")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/SearchMessage")
    public String searchMessage(
            @ApiParam(value = "Search keyword e.g. “Expired” ",required = true)
            @RequestParam(value="MessageTitle") String messageTitle,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" searchMessage called with MessageTitle:"+messageTitle+" agent id:"+agentId);
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
        agentLogger.info(time1+" searchMessage called with MessageTitle:"+messageTitle+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.searchMessage(messageTitle,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }


    @ApiOperation(value = "Micro Service to Block/Unblock Number")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })
    @RequestMapping(value = "/BlockUnBlockNumber", method = RequestMethod.POST)
    public String blockUnBlockNumber(
            @RequestParam(value="PSTN") String pstn,
            @RequestParam(value="Mobile") String mobile,
            @RequestParam(value="Email") String email,
           // @ApiParam(value = "Pass the selected tags", required = true)
            //@RequestParam(value="Tag" ) String tag,
            @ApiParam(value = "(Mandatory) for blocking (Optional) for Unblocking")
            @RequestParam(value="Reason") String reason,
            @ApiParam(value = "Pass 1: For Block Number Pass 2: For Unblock Number",required = true)
            @RequestParam(value="Action") String action,
            @RequestParam(value="AgentName") String agentName,
            @RequestParam(value="AgentId") String agentId

    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" blockUnBlockNumber called with PSTN:"+pstn+" Mobile:"+mobile+" Email:"+email
                +" Reason:"+reason+" Action:"+action+" AgentName:"+agentName+" agent id:"+agentId);
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
        agentLogger.info(time1+" blockUnBlockNumber called with PSTN:"+pstn+" Mobile:"+mobile+" Email:"+email
                +" Reason:"+reason+" Action:"+action+" AgentName:"+agentName+" agent id:"+agentId);

        /*JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.put("rescode", "1");
        json.put("message","Blocked successfully");
        JSONObject dataObj = new JSONObject();
        dataObj.put("msisdn","923125441944");
        dataObj.put("agent_id","123456");
        dataObj.put("reason","as per customer request");
        dataObj.put("pstn","0511234567");
        dataObj.put("agent_name","ABCD");
        dataObj.put("email","abc@gmail.com");
        dataObj.put("tags","SMS IVR");
        dataObj.put("department","Call Center");
        dataObj.put("status","Block");
        array.put(dataObj);
        json.put("data",array);
        returnResponse = json.toString();
        return returnResponse;*/





        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.blockUnBlockNumber(pstn,mobile,email,reason,action,agentName,agentId,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }



    @ApiOperation(httpMethod = "GET", value = "Micro Service for Search DNCR Status(Block/Unblock)")
    @ApiResponses(value = {
            @ApiResponse(code = 0, message = "For Failed"),
            @ApiResponse(code = 1, message = "For Succeed"),
            @ApiResponse(code = 2, message = "Authentication Failed")
    })  @GetMapping("/SearchDNCRStatus")
    public String searchDNCRStatus(
            @RequestParam(value="Mobile") String mobile,
            @RequestParam(value="AgentId",defaultValue ="",required = false) String agentId
    ){

        final long time1 = System.currentTimeMillis();
        String returnResponse = null;
        logger.info(time1+" searchDNCRStatus called with Mobile:"+mobile+" agent id:"+agentId);
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
        agentLogger.info(time1+" searchDNCRStatus called with Mobile:"+mobile+" agent id:"+agentId);

        PrismServiceAccess pService = new PrismServiceAccess();
        returnResponse = pService.searchDNCRStatus(mobile,agentLogger,time1);

        long time2 = System.currentTimeMillis();
        long executionTime = time2 - time1;
        logger.info(time1+" Total Execution time: "+executionTime+" ms");
        agentLogger.info(time1+" Total Execution time: "+executionTime+" ms");
        logger.info(time1+" exit with  "+returnResponse);
        agentLogger.info(time1+" exit with  "+returnResponse);
        agentLogger.info("<<============================================================================>>");
        return returnResponse;

    }


}

package com.ef.clients.ptcl.UpgradedOneWindow.Rebate.RequestModel;

public class SOAPRequestModel {

    public SOAPRequestModel() {
    }

    public static String getTestRequest(String userName,String password,String systemIP,
                                        String pstn,String product,String productId,String srOpenDate,
            String srClosedDate,String region,String pricePlanId,String pricePlan,String stateDate
            ,String agentId,String channel){

        /** Date formate for all mentioned below is
         * yyyy-mm-dd (2019-10-29) */
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:load=\"http://LoadRebate\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <load:LoadRebate>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <Product>"+product+"</Product>\n" +
                        "         <Prodcutid>"+productId+"</Prodcutid>\n" +
                        "         <SROpendate>"+srOpenDate+"</SROpendate>\n" +
                        "         <SRClosedate>"+srClosedDate+"</SRClosedate>\n" +
                        "         <PSTN>"+pstn+"</PSTN>\n" +
                        "         <Region>"+region+"</Region>\n" +
                        "         <PricePlanId>"+pricePlanId+"</PricePlanId>\n" +
                        "         <PricePlan>"+pricePlan+"</PricePlan>\n" +
                        "         <StateDate>"+stateDate+"</StateDate>\n" +
                        "      </load:LoadRebate>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String finesseCalculator(String userName,String password,String systemIP,String pstn,String agentId,String channel){

        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fin=\"http://FinesseCalculator\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <fin:FinesseCalculator>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <PSTN>"+pstn+"</PSTN>\n" +
                        "         <Channel>"+channel+"</Channel>\n" +
                        "         <Agentid>"+agentId+"</Agentid>\n" +
                        "      </fin:FinesseCalculator>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }
}

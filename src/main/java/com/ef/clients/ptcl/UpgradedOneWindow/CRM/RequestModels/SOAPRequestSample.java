package com.ef.clients.ptcl.UpgradedOneWindow.CRM.RequestModels;

public class SOAPRequestSample {

    public SOAPRequestSample() {
    }

    public static String packageReSubscriptionHistoryRequest(String userName,String password,String systemIP,String mdn,String agentId,String channel){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetInfoWirelessPackResubHistory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetInfoWirelessPackResubHistory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetInfoWirelessPackResubHistory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String bucketReSubscriptionHistoryRequest(String userName,String password,String systemIP,String mdn,String agentId,String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetInfoWirelessBuckResubHistory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetInfoWirelessBuckResubHistory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetInfoWirelessBuckResubHistory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String complaintCodeRequest(String userName,String password,String systemIP,String srCategory,String srType){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetComplaintCode\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetComplaintCode>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <SRCategory>"+srCategory+"</SRCategory>\n" +
                        "            <SRType>"+srType+"</SRType>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetComplaintCode>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String getSRTypeRequest(String userName,String password,String systemIP,String srCategory){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetSRType\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetSRType>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <SRCategory>"+srCategory+"</SRCategory>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetSRType>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;

    }

    public static String getSRCategoriesRequest(String userName,String password,String systemIP){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:src=\"http://SRCategory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <src:GetSRCategory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "      </src:GetSRCategory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String getSRHistoryRequest(String userName,String password,String systemIP,String serviceIdentifier,String serviceIdentifierType,String startDate,String endDate){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetSRHistory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetSRHistory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <ServiceId>"+serviceIdentifier+"</ServiceId>\n" +
                        "            <ServiceType>"+serviceIdentifierType+"</ServiceType>\n" +
                        "            <StartDate>"+startDate+"</StartDate>\n" +
                        "            <EndDate>"+endDate+"</EndDate>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetSRHistory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String createCustomerRequest(String userName,String password,String systemIP,String channel,String nearestLandMarkService,String firstName,String storyFloorBilling,
                                               String storyFloor,String nearestLandMark,String lastName,String accountNameBilling,String preferredContactMethod,
                                               String mobile,String houseFlatNoService,String cityBilling,String city,String accountNameService,String storyFloorService,
                                               String salutation,String cityService,String streetMohallaBilling,String sectorAreaHousingSociety,String sectorAreaHousingSocietyBilling,
                                               String streetMohallaService,String sectorAreaHousingSocietyService,String houseFlatNoBilling,String houseFlatNo,String nearestLandMarkBilling,
                                               String middleName,String streetMohalla,String cnic,String email,String customerName,String nadraPresentAddress,String nadraPermanentAddress,
                                               String nadraTransactionId,String agentId){
        String xmlInput =
                "<soapenv:Envelope\n" +
                        "\txmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                        "\txmlns:cre=\"http://CreateCustomerFin\">\n" +
                        "\t<soapenv:Header/>\n" +
                        "\t<soapenv:Body>\n" +
                        "\t\t<cre:CreateCustomerFin>\n" +
                        "\t\t\t<SecurityParam>\n" +
                        "\t\t\t\t<UserName>"+userName+"</UserName>\n" +
                        "\t\t\t\t<Password>"+password+"</Password>\n" +
                        "\t\t\t\t<IP>"+systemIP+"</IP>\n" +
                        "\t\t\t</SecurityParam>\n" +
                        "\t\t\t<Channel>"+channel+"</Channel>\n" +
                        "\t\t\t<Nearest_spcLand_spcMark-Service>"+nearestLandMarkService+"</Nearest_spcLand_spcMark-Service>\n" +
                        "\t\t\t<First_spcName>"+firstName+"</First_spcName>\n" +
                        "\t\t\t<Storey_spc_slh_spcFloor-Billing>"+storyFloorBilling+"</Storey_spc_slh_spcFloor-Billing>\n" +
                        "\t\t\t<Storey_spc_slh_spcFloor>"+storyFloor+"</Storey_spc_slh_spcFloor>\n" +
                        "\t\t\t<Nearest_spcLand_spcMark>"+nearestLandMark+"</Nearest_spcLand_spcMark>\n" +
                        "\t\t\t<Last_spcName>"+lastName+"</Last_spcName>\n" +
                        "\t\t\t<Account_spcName-Billing>"+accountNameBilling+"</Account_spcName-Billing>\n" +
                        "\t\t\t<Preferred_spcContact_spcMethod>"+preferredContactMethod+"</Preferred_spcContact_spcMethod>\n" +
                        "\t\t\t<Mobile_spcNo>"+mobile+"</Mobile_spcNo>\n" +
                        "\t\t\t<House_slhFlat_spcNo-Service>"+houseFlatNoService+"</House_slhFlat_spcNo-Service>\n" +
                        "\t\t\t<City-Billing>"+cityBilling+"</City-Billing>\n" +
                        "\t\t\t<City>"+city+"</City>\n" +
                        "\t\t\t<Account_spcName-Service>"+accountNameService+"</Account_spcName-Service>\n" +
                        "\t\t\t<Storey_spc_slh_spcFloor-Service>"+storyFloorService+"</Storey_spc_slh_spcFloor-Service>\n" +
                        "\t\t\t<Salutation>"+salutation+"</Salutation>\n" +
                        "\t\t\t<City-Service>"+cityService+"</City-Service>\n" +
                        "\t\t\t<Street_spc_slh_spcMohalla-Billing>"+streetMohallaBilling+"</Street_spc_slh_spcMohalla-Billing>\n" +
                        "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety>"+sectorAreaHousingSociety+"</Sector_slh_spcArea_slh_spcHousing_spcSociety>\n" +
                        "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety-Billing>"+sectorAreaHousingSocietyBilling+"</Sector_slh_spcArea_slh_spcHousing_spcSociety-Billing>\n" +
                        "\t\t\t<Street_spc_slh_spcMohalla-Service>"+streetMohallaService+"</Street_spc_slh_spcMohalla-Service>\n" +
                        "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety-Service>"+sectorAreaHousingSocietyService+"</Sector_slh_spcArea_slh_spcHousing_spcSociety-Service>\n" +
                        "\t\t\t<House_slhFlat_spcNo-Billing>"+houseFlatNoBilling+"</House_slhFlat_spcNo-Billing>\n" +
                        "\t\t\t<House_slhFlat_spcNo>"+houseFlatNo+"</House_slhFlat_spcNo>\n" +
                        "\t\t\t<Nearest_spcLand_spcMark-Billing>"+nearestLandMarkBilling+"</Nearest_spcLand_spcMark-Billing>\n" +
                        "\t\t\t<Middle_spcName>"+middleName+"</Middle_spcName>\n" +
                        "\t\t\t<Street_spc_slh_spcMohalla>"+streetMohalla+"</Street_spc_slh_spcMohalla>\n" +
                        "\t\t\t<CNIC_spcNumber>"+cnic+"</CNIC_spcNumber>\n" +
                        "\t\t\t<Email>"+email+"</Email>\n" +
                        "\t\t\t<CustomerName>"+customerName+"</CustomerName>\n" +
                        "\t\t\t<NadraPresentAddress>"+nadraPresentAddress+"</NadraPresentAddress>\n" +
                        "\t\t\t<NadraPermanentAddress>"+nadraPermanentAddress+"</NadraPermanentAddress>\n" +
                        "\t\t\t<NadraTransactionID>"+nadraTransactionId+"</NadraTransactionID>\n" +
                        "\t\t\t<AgentID>"+agentId+"</AgentID>\n" +
                        "\t\t</cre:CreateCustomerFin>\n" +
                        "\t</soapenv:Body>\n" +
                        "</soapenv:Envelope>";

    /*   String xmlInput = "<soapenv:Envelope\n" +
               "\txmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
               "\txmlns:cre=\"http://CreateCustomerFin\">\n" +
               "\t<soapenv:Header/>\n" +
               "\t<soapenv:Body>\n" +
               "\t\t<cre:CreateCustomerFin>\n" +
               "\t\t\t<SecurityParam>\n" +
               "\t\t\t\t<UserName>CRM</UserName>\n" +
               "\t\t\t\t<Password>123</Password>\n" +
               "\t\t\t\t<IP>?</IP>\n" +
               "\t\t\t</SecurityParam>\n" +
               "\t\t\t<Channel>finesse</Channel>\n" +
               "\t\t\t<Nearest_spcLand_spcMark-Service></Nearest_spcLand_spcMark-Service>\n" +
               "\t\t\t<First_spcName>test</First_spcName>\n" +
               "\t\t\t<Storey_spc_slh_spcFloor-Billing></Storey_spc_slh_spcFloor-Billing>\n" +
               "\t\t\t<Storey_spc_slh_spcFloor></Storey_spc_slh_spcFloor>\n" +
               "\t\t\t<Nearest_spcLand_spcMark></Nearest_spcLand_spcMark>\n" +
               "\t\t\t<Last_spcName>test</Last_spcName>\n" +
               "\t\t\t<Account_spcName-Billing>EOrder</Account_spcName-Billing>\n" +
               "\t\t\t<Preferred_spcContact_spcMethod>Mobile</Preferred_spcContact_spcMethod>\n" +
               "\t\t\t<Mobile_spcNo>03008705555</Mobile_spcNo>\n" +
               "\t\t\t<House_slhFlat_spcNo-Service>abc</House_slhFlat_spcNo-Service>\n" +
               "\t\t\t<City-Billing>Islamabad</City-Billing>\n" +
               "\t\t\t<City>Islamabad</City>\n" +
               "\t\t\t<Account_spcName-Service>EOrder</Account_spcName-Service>\n" +
               "\t\t\t<Storey_spc_slh_spcFloor-Service></Storey_spc_slh_spcFloor-Service>\n" +
               "\t\t\t<Salutation>Mr.</Salutation>\n" +
               "\t\t\t<City-Service>Islamabad</City-Service>\n" +
               "\t\t\t<Street_spc_slh_spcMohalla-Billing>abc</Street_spc_slh_spcMohalla-Billing>\n" +
               "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety>abc</Sector_slh_spcArea_slh_spcHousing_spcSociety>\n" +
               "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety-Billing>abc</Sector_slh_spcArea_slh_spcHousing_spcSociety-Billing>\n" +
               "\t\t\t<Street_spc_slh_spcMohalla-Service>abc</Street_spc_slh_spcMohalla-Service>\n" +
               "\t\t\t<Sector_slh_spcArea_slh_spcHousing_spcSociety-Service>abc</Sector_slh_spcArea_slh_spcHousing_spcSociety-Service>\n" +
               "\t\t\t<House_slhFlat_spcNo-Billing>abc</House_slhFlat_spcNo-Billing>\n" +
               "\t\t\t<House_slhFlat_spcNo>abc</House_slhFlat_spcNo>\n" +
               "\t\t\t<Nearest_spcLand_spcMark-Billing></Nearest_spcLand_spcMark-Billing>\n" +
               "\t\t\t<Middle_spcName></Middle_spcName>\n" +
               "\t\t\t<Street_spc_slh_spcMohalla>abc</Street_spc_slh_spcMohalla>\n" +
               "\t\t\t<CNIC_spcNumber>3820145211007</CNIC_spcNumber>\n" +
               "\t\t\t<Email>test@ptcl.com.pk</Email>\n" +
               "\t\t\t<CustomerName>abc</CustomerName>\n" +
               "\t\t\t<NadraPresentAddress>abc</NadraPresentAddress>\n" +
               "\t\t\t<NadraPermanentAddress>abc</NadraPermanentAddress>\n" +
               "\t\t\t<NadraTransactionID></NadraTransactionID>\n" +
               "\t\t\t<AgentID>finesse</AgentID>\n" +
               "\t\t</cre:CreateCustomerFin>\n" +
               "\t</soapenv:Body>\n" +
               "</soapenv:Envelope>";*/
        return xmlInput;
    }

    public static String newOrderRequest(String userName,String password,String systemIP,String altNumber,String srCategory,String serviceId,
                                         String type,String customerAccountId,String subType,String referralId,String sourceName,
                                         String description,String productId,String packageId,String pricePlanId,String itemQuantity){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:new=\"http://NewOrderFin\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <new:NewOrderFin>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <Alternate_spcNumber>"+altNumber+"</Alternate_spcNumber>\n" +
                        "         <SRCategory>"+srCategory+"</SRCategory>\n" +
                        "         <ServiceId>"+serviceId+"</ServiceId>\n" +
                        "         <Type>"+type+"</Type>\n" +
                        "         <AccountId>"+customerAccountId+"</AccountId>\n" +
                        "         <SubType>"+subType+"</SubType>\n" +
                        "         <Referral_spcId>"+referralId+"</Referral_spcId>\n" +
                        "         <Source_spcName>"+sourceName+"</Source_spcName>\n" +
                        "         <Description>"+description+"</Description>\n" +
                        "         <LineItems>\n" +
                        "            <ProductID>"+productId+"</ProductID>\n" +
                        "            <PackageID>"+packageId+"</PackageID>\n" +
                        "            <PricePlanID>"+pricePlanId+"</PricePlanID>\n" +
                        "            <ItemQuantity>"+itemQuantity+"</ItemQuantity>\n" +
                        "         </LineItems>\n" +
                        "      </new:NewOrderFin>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String createSRRequest(String userName,String password,String systemIP,String altNumber,String serviceId,
                                         String srCode,String email,String source,String description,String channel,String agentId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wir=\"http://WirelineComplaintFin\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <wir:WirelineComplaintFin>\n" +
                        "          <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <Alternate_spcNumber>"+altNumber+"</Alternate_spcNumber>\n" +
                        "         <Service_spcId>"+serviceId+"</Service_spcId>\n" +
                        "         <SR_spcCode>"+srCode+"</SR_spcCode>\n" +
                        "         <Email_spcId>"+email+"</Email_spcId>\n" +
                        "         <Source>"+source+"</Source>\n" +
                        "         <Description>"+description+"</Description>\n" +
                        "         <Channel>"+channel+"</Channel>\n" +
                        "         <AgentID>"+agentId+"</AgentID>\n" +
                        "      </wir:WirelineComplaintFin>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String duplicateBillLinkRequest(String userName,String password,String systemIP,String requesterIP,String channel,
                                                  String accountId,String pstnWithOutAreaCode,String billType){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetDupBillLink\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetDupBillLink>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <RequesterIP>"+requesterIP+"</RequesterIP>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <AccountID>"+accountId+"</AccountID>\n" +
                        "            <SubscriberNumber>"+pstnWithOutAreaCode+"</SubscriberNumber>\n" +
                        "            <BillType>"+billType+"</BillType>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetDupBillLink>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String nadraServiceRequest(String cnic){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:nad=\"http://www.ptcl.com/esb/services/NadraService\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <nad:NadraRequest>\n" +
                        "         <nad:cnic>"+cnic+"</nad:cnic>\n" +
                        "      </nad:NadraRequest>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getCustomerInfoRequest(String userName,String password,String systemIP,String serviceIdentifierType,String serviceIdentifier){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetInfoFin\">\n" +
                        " <soapenv:Header/>\n" +
                        " <soapenv:Body>\n" +
                        "<get:GetInfoFin>\n" +
                        "<SecurityParam>\n" +
                        "<UserName>"+ userName +"</UserName>\n" +
                        "<Password>"+ password +"</Password>\n" +
                        "<IP>"+ systemIP +"</IP>\n" +
                        "</SecurityParam>\n" +
                        "<RequestParam>\n" +
                        "<ServiceIdentifierType>"+serviceIdentifierType+"</ServiceIdentifierType>\n" +
                        "<ServiceIdentifier>"+serviceIdentifier+"</ServiceIdentifier>\n" +
                        "</RequestParam>\n" +
                        "</get:GetInfoFin>\n" +
                        " </soapenv:Body>\n" +
                        " </soapenv:Envelope>";
        return xmlInput;
    }

    public static String getDupBill(String userName,String password,String systemIP,String subscriberNo,String subscriberId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetDupBill\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetDupBill>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <SubscriberNo>"+subscriberNo+"</SubscriberNo>\n" +
                        "            <SubscriberId>"+subscriberId+"</SubscriberId>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetDupBill>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String updateCustomerAddressRequest(String userName,String password,String systemIP,String pstn, String customerAccountId
    ,String billingAccountID,String channel,String addressHouseFlate,String addressStoreyFloor,String addressStreetMohalla,String addressSectorArea,
                                                      String addressCity,String addressNearestLandMark,String agentId,String addressType){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:upd=\"http://UpdateCustomerAddress\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <upd:UpdateCustomerAddress>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+ userName +"</UserName>\n" +
                        "            <Password>"+ password +"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <PSTN>"+pstn+"</PSTN>\n" +
                        "            <CustAccountID>"+customerAccountId+"</CustAccountID>\n" +
                        "            <BillAccountID>"+billingAccountID+"</BillAccountID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <Address_HouseFlat>"+addressHouseFlate+"</Address_HouseFlat>\n" +
                        "            <Address_StoreyFloor>"+addressStoreyFloor+"</Address_StoreyFloor>\n" +
                        "            <Address_StreetMohalla>"+addressStreetMohalla+"</Address_StreetMohalla>\n" +
                        "            <Address_SectorArea>"+addressSectorArea+"</Address_SectorArea>\n" +
                        "            <Address_City>"+addressCity+"</Address_City>\n" +
                        "            <Address_NearestLandMark>"+addressNearestLandMark+"</Address_NearestLandMark>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <AddressType>"+addressType+"</AddressType>\n" +
                        "         </RequestParam>\n" +
                        "      </upd:UpdateCustomerAddress>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getProductWirelineRequest(String userName,String password,String systemIP){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetProductWireLine\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetProductWireLine>\n" +
                        "         <SecurityPararm>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityPararm>\n" +
                        "      </get:GetProductWireLine>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String getPackageWirelineRequest(String userName,String password,String systemIP,String product){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetPackagelistWireLine\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetPackagelistWireLine>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <Product>"+product+"</Product>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetPackagelistWireLine>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getEmailVerificationLinkRequest(String userName,String password,String systemIP,
                                                         String pstn,String accountId,String billingAccountId,String channel,
                                                         String email,String agentId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetEmailURL\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetEmailURL>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <PSTN>"+pstn+"</PSTN>\n" +
                        "            <CustAccountID>"+accountId+"</CustAccountID>\n" +
                        "            <BillAccountID>"+billingAccountId+"</BillAccountID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <Email>"+email+"</Email>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetEmailURL>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String updateCNICNumberRequest(String userName,String password,String systemIP,String channel,
                                          String cnic,String pstn,String accountId,String billingAccountId,
                                          String agentId,String customerName,String fatherName,String nPresentAddr,
                                          String nPermanentAddr,String nTransactionId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:upd=\"http://UpdateCNICNumber\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <upd:UpdateCNICNumber>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <CNIC>"+cnic+"</CNIC>\n" +
                        "            <PSTN>"+pstn+"</PSTN>\n" +
                        "            <CustAccountID>"+accountId+"</CustAccountID>\n" +
                        "            <BillAccountID>"+billingAccountId+"</BillAccountID>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <CustomerName>"+customerName+"</CustomerName>\n" +
                        "            <NadraFatherName>"+fatherName+"</NadraFatherName>\n" +
                        "            <NadraPresentAddress>"+nPresentAddr+"</NadraPresentAddress>\n" +
                        "            <NadraPermanentAddress>"+nPermanentAddr+"</NadraPermanentAddress>\n" +
                        "            <NadraTransactionID>"+nTransactionId+"</NadraTransactionID>\n" +
                        "         </RequestParam>\n" +
                        "      </upd:UpdateCNICNumber>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";

        return xmlInput;
    }

    public static String updateMobileNumberRequest(String userName,String password,String systemIP,
                                                   String pstn,String customerAccountId,String billingAccountID,
                                                   String channel,String mobile,String agentId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:upd=\"http://UpdateMobileNumber\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <upd:UpdateMobileNumber>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <PSTN>"+pstn+"</PSTN>\n" +
                        "            <CustAccountID>"+customerAccountId+"</CustAccountID>\n" +
                        "            <BillAccountID>"+billingAccountID+"</BillAccountID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <Mobile>"+mobile+"</Mobile>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "         </RequestParam>\n" +
                        "      </upd:UpdateMobileNumber>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String updateEmailIdRequest(String userName,String password,String systemIP,String pstn,
                                              String customerAccountId,String billingAccountID,String channel,
                                              String email,String agentId){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:upd=\"http://UpdateCustomerEmailId\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <upd:UpdateCustomerEmailId>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <PSTN>"+pstn+"</PSTN>\n" +
                        "            <CustAccountID>"+customerAccountId+"</CustAccountID>\n" +
                        "            <BillAccountID>"+billingAccountID+"</BillAccountID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "            <EmailID>"+email+"</EmailID>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "         </RequestParam>\n" +
                        "      </upd:UpdateCustomerEmailId>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getBucketListWirelessRequest(String userName,String password,String systemIP,
                                                      String mdn,String agentId,String channel){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wir=\"http://WirelessBucketList\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <wir:WirelessBucketList>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </wir:WirelessBucketList>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String bucketSubscriptionWirelessRequest(String userName,String password,String systemIP,
                                                           String mdn,String userId,String optionalOfferId,String agentId,
                                                           String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wir=\"http://WirelessBucketSubscription\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <wir:WirelessBucketSubscription>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <UserID>"+userId+"</UserID>\n" +
                        "            <OptionalOfferID>"+optionalOfferId+"</OptionalOfferID>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </wir:WirelessBucketSubscription>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getPackageListWirelessReqest(String userName,String password,String systemIP,
                                                      String mdn,String agentId,String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetPkgListForPkgChange\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetPkgListForPkgChange>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetPkgListForPkgChange>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }
    public static String getPackageHistoryWireless(String userName,String password,String systemIP,String mdn,String agentId,
                                                   String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetInfoWirelessPackageHistory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetInfoWirelessPackageHistory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetInfoWirelessPackageHistory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String packageChangeWirelessRequest(String userName,String password,String systemIP,String mdn,
                                                      String user_id,String newPackageId,String agentId,String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wir=\"http://WirelessPackageChange\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <wir:WirelessPackageChange>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <UserID>"+user_id+"</UserID>\n" +
                        "            <NewPackageID>"+newPackageId+"</NewPackageID>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </wir:WirelessPackageChange>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String packageResubscriptionWirelessRequest(String userName,String password,String systemIP,String mdn,String user_id,
                                                              String agentId,String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wir=\"http://WirelessPackageResubscription\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <wir:WirelessPackageResubscription>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <UserID>"+user_id+"</UserID>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </wir:WirelessPackageResubscription>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String getBalanceHistoryWireless(String userName,String password,String systemIP,
                                                   String mdn,String agentId,String channel){
        String xmlInput =
                " <soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:get=\"http://GetInfoWirelessBalanceHistory\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <get:GetInfoWirelessBalanceHistory>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </get:GetInfoWirelessBalanceHistory>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String ebillActivationRequest(String userName,String password,String systemIP,String mdn,String email,
                                                String agentId,String channel){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ebil=\"http://EbillActivationWireless\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <ebil:EbillActivationWireless>\n" +
                        "         <SecurityParam>\n" +
                        "            <UserName>"+userName+"</UserName>\n" +
                        "            <Password>"+password+"</Password>\n" +
                        "            <IP>"+systemIP+"</IP>\n" +
                        "         </SecurityParam>\n" +
                        "         <RequestParam>\n" +
                        "            <MDN>"+mdn+"</MDN>\n" +
                        "            <Email>"+email+"</Email>\n" +
                        "            <AgentID>"+agentId+"</AgentID>\n" +
                        "            <Channel>"+channel+"</Channel>\n" +
                        "         </RequestParam>\n" +
                        "      </ebil:EbillActivationWireless>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }

    public static String sendSMSRequest(String userName,String password,String number,String msg){
        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                        "   <soapenv:Header/>\n" +
                        "   <soapenv:Body>\n" +
                        "      <tem:SendSms>\n" +
                        "         <!--Optional:-->\n" +
                        "         <tem:webuser>"+userName+"</tem:webuser>\n" +
                        "         <!--Optional:-->\n" +
                        "         <tem:webpass>"+password+"</tem:webpass>\n" +
                        "         <!--Optional:-->\n" +
                        "         <tem:sendTo>"+number+"</tem:sendTo>\n" +
                        "         <!--Optional:-->\n" +
                        "         <tem:Message>"+msg+"</tem:Message>\n" +
                        "      </tem:SendSms>\n" +
                        "   </soapenv:Body>\n" +
                        "</soapenv:Envelope>";
        return xmlInput;
    }
}

package com.duzon.dbp.apimonitoring.swagger_api_docs.controller;

import java.io.IOException;
import java.net.URL;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.SwaggerData;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.check.UrlRequest;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.check.UrlResponse;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap.CheckOverlapApi;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save.OverLapAndSaveRequest;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save.OverLapCommonResponse;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperApi;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperApiCategory;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperOverlap;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperSave;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperService;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperServiceCategory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.Tags;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.Definitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.DynamicDefinitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.DynamicPath;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.Paths;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.httpmethod.ApiJson;
import com.google.gson.Gson;

@Slf4j
@Api(tags = { "SwaggerApiDocs" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/swagger-api-docs-json")
public class CheckSwaggerDataController {
    @Autowired
    MapperServiceCategory mapperServiceCategory;
    @Autowired
    MapperService mapperService;
    @Autowired
    MapperApiCategory mapperApiCategory;
    @Autowired
    MapperApi mapperApi;
    @Autowired
    MapperOverlap mapperOverlap;
    @Autowired
    MapperSave mapperSave;

    /* -------------- CHECK from request "SWAGGER API DOC JSON URL" ------------- */
    @ApiOperation(value = "Check URL(Swagger Api Docs)")
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public UrlResponse CheckSwaggerApiDocsUrl(@RequestBody UrlRequest urlRequest) {

        UrlResponse urlResponse = new UrlResponse();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        boolean isGroupNameSame = false;
        boolean isCategoryNameSame = false;

        SwaggerData swaggerData = new SwaggerData();
        JsonNode isSwaggerJson = null;

        try {
            if (urlRequest.getImportType().equals("url")) {
                swaggerData = mapper.readValue(new URL(urlRequest.getSwaggerUrl()), SwaggerData.class);
                isSwaggerJson = mapper.readTree(new URL(urlRequest.getSwaggerUrl()));
            }
            if (urlRequest.getImportType().equals("file")) {
                swaggerData = mapper.convertValue(urlRequest.getSwaggerFile(), SwaggerData.class);
                isSwaggerJson = urlRequest.getSwaggerFile().deepCopy();
            }

            if ((isSwaggerJson.has("swagger")) && (isSwaggerJson.has("info")) && (isSwaggerJson.has("paths"))
                    && (isSwaggerJson.get("info").has("title")) && (isSwaggerJson.get("info").has("version"))) {
                if (isSwaggerJson.get("swagger").toString().equals("\"2.0\"")) {
                    String serviceUrl = "http://" + swaggerData.getHost() + swaggerData.getBasePath(); // Service URL

                    if (mapperSave.isExistService(serviceUrl)) {
                        if (swaggerData.getSwagger() != null) {
                            urlResponse.setSwaggerCheck(true);
                            urlResponse.setSwaggerData(swaggerData);
                        } else {
                            urlResponse.setSwaggerCheck(false);
                            urlResponse.setSwaggerData(null);
                        }

                        if (mapperService.getGroupNoFromService(serviceUrl).equals(urlRequest.getGroupNo())) {
                            urlResponse.setProperGroupName(
                                    mapperService.getGroupName(mapperService.getGroupNoFromService(serviceUrl)));
                            isGroupNameSame = true;
                        } else {
                            urlResponse.setProperGroupName(
                                    mapperService.getGroupName(mapperService.getGroupNoFromService(serviceUrl)));
                            isGroupNameSame = false;
                        }
                        if (mapperService.getServiceCategoryNoFromService(serviceUrl)
                                .equals(urlRequest.getServiceCategoryNo())) {
                            urlResponse.setProperServiceCategoryName(mapperService
                                    .getServiceCategoryName(mapperService.getServiceCategoryNoFromService(serviceUrl)));
                            isCategoryNameSame = true;
                        } else {
                            urlResponse.setProperServiceCategoryName(mapperService
                                    .getServiceCategoryName(mapperService.getServiceCategoryNoFromService(serviceUrl)));
                            isCategoryNameSame = false;
                        }
                        if ((isCategoryNameSame == true) && (isGroupNameSame == true)) {
                            urlResponse.setSameCheck(true);
                        }
                    } else {
                        if (swaggerData.getSwagger() != null) {
                            urlResponse.setSwaggerCheck(true);
                            urlResponse.setSwaggerData(swaggerData);
                        } else {
                            urlResponse.setSwaggerCheck(false);
                            urlResponse.setSwaggerData(null);
                        }

                        urlResponse.setProperGroupName(null);
                        urlResponse.setProperServiceCategoryName(null);
                        urlResponse.setSameCheck(true);
                    }
                    return urlResponse;
                } else {
                    urlResponse.setSwaggerCheck(false);
                    return urlResponse;
                }

            } else {
                urlResponse.setSwaggerCheck(false);
                return urlResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return urlResponse;
        } catch (NullPointerException n) {
            n.printStackTrace();
            return urlResponse;
        }

    }

    /* -------------- CHECK 'OVERLAP' from url and Saved datas ------------- */
    @ApiOperation(value = "Check overlap from database& new json data(Swagger Api Docs)")
    @RequestMapping(value = "/overlap", method = RequestMethod.POST)
    @ResponseBody
    public OverLapCommonResponse CheckOverlap(@RequestBody OverLapAndSaveRequest req) {

        OverLapCommonResponse overLapCommonResponse = new OverLapCommonResponse();

        try {
            log.info("1 # # # # # # # # TRY --> MAPPING from url # # # # # # # # ");

            Gson gson = new Gson();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SwaggerData swaggerData = new SwaggerData();
            if (req.getImportType().equals("url")) {
                swaggerData = mapper.readValue(new URL(req.getSwaggerUrl()), SwaggerData.class);
            } else if (req.getImportType().equals("file")) {
                swaggerData = mapper.convertValue(req.getSwaggerFile(), SwaggerData.class);
            }
            // Info info = mapper.treeToValue(swaggerData.getInfo(), Info.class);
            Tags[] tags = mapper.treeToValue(swaggerData.getTags(), Tags[].class);
            Paths paths = mapper.treeToValue(swaggerData.getPaths(), Paths.class);
            ArrayList<String> getDynamicPathKeys = new ArrayList<String>(paths.getDynamicPath().keySet());
            ArrayList<Object> getDynamicPathValues = new ArrayList<Object>(paths.getDynamicPath().values());
            String jsonPath = gson.toJson(getDynamicPathValues, ArrayList.class);
            DynamicPath[] dynamicPath = mapper.readValue(jsonPath, DynamicPath[].class);

            DynamicDefinitions dynamicDefinitions = mapper.treeToValue(swaggerData.getDefinitions(),
                    DynamicDefinitions.class);
            ArrayList<String> getDynamicDefinitionsKeyList = new ArrayList<String>(
                    dynamicDefinitions.getDynamicDefinitions().keySet());
            ArrayList<Object> getDynamicDefinitionsValueList = new ArrayList<Object>(
                    dynamicDefinitions.getDynamicDefinitions().values());
            Definitions definitions;
            ArrayList<Definitions> definitionsList = new ArrayList<Definitions>();
            HashMap<String, Object> definitionsMap = new HashMap<String, Object>();
            for (int j = 0; j < getDynamicDefinitionsKeyList.size(); j++) {
                definitions = new Definitions();
                definitions.setType(getDynamicDefinitionsKeyList.get(j));
                definitions.setProperties(getDynamicDefinitionsValueList.get(j));
                definitionsList.add(definitions);
                definitionsMap.put(definitions.getType(), definitions.getProperties());
            }
            // ArrayList<CustomDefinitions> customDefinitions;
            String service_url = "http://" + swaggerData.getHost() + swaggerData.getBasePath();
            log.info("1 # # # # # # # # # # # # # # # # # # # # # # # # # # # # ");
            log.info("1 # # # # # # # # # # # ADDVECTOR # # # # # # # # # # # # ");
            log.info("1 # # # # # # # # # # category(Vec) # # # # # # # # # # # ");
            Vector<String> categoryFromJson = new Vector<String>();
            for (int i = 0; i < tags.length; i++) {
                categoryFromJson.add(tags[i].getName());
            }
            Vector<String> categoryWillInsert = new Vector<String>();
            Vector<String> categoryWillUpdate = new Vector<String>();
            Vector<String> categoryWillDelete = mapperOverlap.getExpectedUpdateAll(service_url);

            log.info("1 # # # # # # # # # # # api(Vec) # # # # # # # # # # # # ");
            Vector<CheckOverlapApi> apiFromJson = new Vector<CheckOverlapApi>();
            Vector<CheckOverlapApi> apiFromDb = new Vector<CheckOverlapApi>();

            Vector<CheckOverlapApi> apiWillInsert = new Vector<CheckOverlapApi>();
            Vector<CheckOverlapApi> apiWillUpdate = new Vector<CheckOverlapApi>();
            Vector<CheckOverlapApi> apiWillDelete = new Vector<CheckOverlapApi>();

            log.info("1 # # # # # # # # # # # # # # # # # # # # # # # # # # # # ");

            if ((req.getGroupNo() != null) && (req.getServiceCategoryNo() != null)
                    && ((req.getSwaggerUrl() != null) || (req.getSwaggerFile() != null))
                    && (req.getApiCategoryName() == null) && (req.getEmployee() == null)
                    && (req.getEmployeeSub() == null) && (req.getSelectedApiList() == null)) {
                log.info("___2_서비스________________________________");
                if (mapperOverlap.isOverlapService(service_url)) {
                    log.info("___2_서비스___서비스있다______________________");
                    apiFromDb = mapperSave.totalApi(service_url);
                    for (int i = 0; i < categoryFromJson.size(); i++) {
                        if (mapperOverlap.isOverlapApiCategory(categoryFromJson.get(i)) == true) {
                            log.info("___3_서비스___서비스있다___카테고리있다___________");

                            categoryWillUpdate
                                    .add(mapperOverlap.getExpectedUpdate(categoryFromJson.get(i), service_url));

                        } else {
                            log.info("___3_서비스___서비스있다___카테고리없다___________");
                            categoryWillInsert.add(categoryFromJson.get(i));
                        }
                    }

                    for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                        String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                        String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                        String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                        ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

                        for (int j2 = 0; j2 < apiJson.length; j2++) {
                            CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                            apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                            apiUrlAndMethod.setMethod(apiMethod[j2]);
                            apiUrlAndMethod.setApi_category_no(
                                    mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                            apiUrlAndMethod.setIs_deleted("F");
                            apiFromJson.add(apiUrlAndMethod);
                        }
                    }
                    if (!categoryWillUpdate.isEmpty()) {
                        log.info("___2_서비스___서비스있다___카테고리삭제___________");
                        Vector<String> tempVector = categoryWillUpdate;
                        for (int j = 0; j < tempVector.size(); j++) {
                            categoryWillDelete.remove(tempVector.get(j));
                        }
                    }
                    for (int i = 0; i < apiFromJson.size(); i++) {
                        if (!apiFromDb.contains(apiFromJson.get(i))) {
                            log.info("___4_서비스___서비스있다___카테고리있다___API없다__");
                            apiWillInsert.add(apiFromJson.get(i));
                        }
                    }
                    for (int i = 0; i < apiFromDb.size(); i++) {
                        if (!apiFromJson.contains(apiFromDb.get(i))) {
                            log.info("___4_서비스___서비스있다___카테고리있다___API삭제__");
                            apiWillDelete.add(apiFromDb.get(i));
                        }

                        if (apiFromJson.contains(apiFromDb.get(i))) {
                            log.info("___4_서비스___서비스있다___카테고리있다___API있다__");
                            apiWillUpdate.add(apiFromDb.get(i));
                        }
                    }
                } else {
                    log.info("___2_서비스___서비스없다______________________");
                    log.info("___3_서비스___서비스없다___카테고리없다___________");
                    log.info("___4_서비스___서비스없다___카테고리없다___API없다__");
                    for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                        String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                        String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                        String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                        ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);
                        for (int j2 = 0; j2 < apiJson.length; j2++) {
                            CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                            apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                            apiUrlAndMethod.setMethod(apiMethod[j2]);
                            apiUrlAndMethod.setApi_category_no(
                                    mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                            apiUrlAndMethod.setIs_deleted("F");
                            apiFromJson.add(apiUrlAndMethod);
                        }
                    }
                    apiWillInsert = apiFromJson;
                }
                log.info("__________________________________________");
                overLapCommonResponse.setIsExpectedCreate(apiWillInsert);
                overLapCommonResponse.setIsExpectedUpdate(apiWillUpdate);
                overLapCommonResponse.setIsExpectedDelete(apiWillDelete);

                overLapCommonResponse.setOverlap(true);
            } else if ((req.getGroupNo() != null) && (req.getServiceCategoryNo() != null)
                    && ((req.getSwaggerUrl() != null) || (req.getSwaggerFile() != null))
                    && (req.getApiCategoryName() != null) && (req.getEmployee() == null)
                    && (req.getEmployeeSub() == null) && (req.getSelectedApiList() == null)) {
                log.info("___2_카테고리_______________________________");
                if (mapperOverlap.isOverlapService(service_url)) {
                    log.info("___2_카테고리___서비스있다______________________");
                    if (mapperOverlap.isOverlapApiCategory(req.getApiCategoryName())
                            && mapperOverlap.isOverlapApiCategory(
                                    categoryFromJson.get(categoryFromJson.indexOf(req.getApiCategoryName())))) {
                        log.info("___3_카테고리___서비스있다___카테고리있다___________");
                        for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                            String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                            String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                            String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                            ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);
                            for (int j2 = 0; j2 < apiJson.length; j2++) {
                                CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                                apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                                apiUrlAndMethod.setMethod(apiMethod[j2]);
                                apiUrlAndMethod.setApi_category_no(
                                        mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                                apiUrlAndMethod.setIs_deleted("F");
                                if (req.getApiCategoryName().equals(apiJson[j2].getTags()[0])) {
                                    apiFromJson.add(apiUrlAndMethod);
                                }
                            }
                        }

                        apiFromDb.removeAllElements();
                        apiFromDb = mapperSave.onlyApiOfCategory(service_url, req.getApiCategoryName());

                        for (int i = 0; i < apiFromJson.size(); i++) {
                            if (!apiFromDb.contains(apiFromJson.get(i))) {
                                log.info("___4_서비스___서비스있다___카테고리있다___API없다__");
                                apiWillInsert.add(apiFromJson.get(i));
                            }
                        }
                        for (int i = 0; i < apiFromDb.size(); i++) {
                            if (!apiFromJson.contains(apiFromDb.get(i))) {
                                log.info("___4_서비스___서비스있다___카테고리있다___API삭제__");
                                apiWillDelete.add(apiFromDb.get(i));
                            }

                            if (apiFromJson.contains(apiFromDb.get(i))) {
                                log.info("___4_서비스___서비스있다___카테고리있다___API있다__");
                                apiWillUpdate.add(apiFromDb.get(i));
                            }
                        }

                    } else {
                        log.info("___3_카테고리___서비스있다___카테고리없다___________");
                        log.info("___4_카테고리___서비스있다___카테고리없다___API없다__");
                        for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                            String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                            String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                            String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                            ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);
                            for (int j2 = 0; j2 < apiJson.length; j2++) {
                                CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                                apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                                apiUrlAndMethod.setMethod(apiMethod[j2]);
                                apiUrlAndMethod.setApi_category_no(
                                        mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                                apiUrlAndMethod.setIs_deleted("F");
                                if (req.getApiCategoryName().equals(apiJson[j2].getTags()[0])) {
                                    log.info("!req.카테고리 == json.카테고리!");
                                    apiFromJson.add(apiUrlAndMethod);
                                }
                            }
                        }
                        apiWillInsert = apiFromJson;
                    }
                } else {
                    log.info("___2_카테고리___서비스없다______________________");
                    log.info("___3_카테고리___서비스없다___카테고리없다___________");
                    log.info("___4_카테고리___서비스없다___카테고리없다___API없다__");
                    for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                        String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                        String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                        String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                        ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);
                        for (int j2 = 0; j2 < apiJson.length; j2++) {
                            CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                            apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                            apiUrlAndMethod.setMethod(apiMethod[j2]);
                            apiUrlAndMethod.setApi_category_no(
                                    mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                            apiUrlAndMethod.setIs_deleted("F");
                            if (req.getApiCategoryName().equals(apiJson[j2].getTags()[0])) {
                                log.info("!req.카테고리 == json.카테고리!");
                                apiFromJson.add(apiUrlAndMethod);
                            }
                        }
                    }
                    apiWillInsert = apiFromJson;
                }

                log.info("__________________________________________");
                overLapCommonResponse.setIsExpectedCreate(apiWillInsert);
                overLapCommonResponse.setIsExpectedUpdate(apiWillUpdate);
                overLapCommonResponse.setIsExpectedDelete(apiWillDelete);
                overLapCommonResponse.setOverlap(true);
            } else if ((req.getGroupNo() != null) && (req.getServiceCategoryNo() != null)
                    && ((req.getSwaggerUrl() != null) || (req.getSwaggerFile() != null))
                    && (req.getApiCategoryName() != null) && (req.getEmployee() != null)
                    && (req.getEmployeeSub() != null) && (req.getSelectedApiList() != null)) {
                log.info("___2_API__________________________________");

                if (mapperOverlap.isOverlapService(service_url)) {
                    log.info("___2_API___서비스있다______________________");
                    if (mapperOverlap.isOverlapApiCategory(req.getApiCategoryName())
                            && mapperOverlap.isOverlapApiCategory(
                                    categoryFromJson.get(categoryFromJson.indexOf(req.getApiCategoryName())))) {
                        log.info("___2_API___서비스있다___카테고리있다___________");

                        for (int j = 0; j < getDynamicPathKeys.size(); j++) {

                            String apiJsonMethod = mapper.writeValueAsString(dynamicPath[j].getMethods().keySet());
                            String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
                            String apiJsonDetail = mapper.writeValueAsString(dynamicPath[j].getMethods().values());
                            ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);
                            for (int j2 = 0; j2 < apiJson.length; j2++) {
                                CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                                apiUrlAndMethod.setApi_url(getDynamicPathKeys.get(j));
                                apiUrlAndMethod.setMethod(apiMethod[j2]);
                                apiUrlAndMethod.setApi_category_no(
                                        mapperApi.getApiCategoryNo(apiJson[j2].getTags()[0], service_url));
                                apiUrlAndMethod.setIs_deleted("F");

                                Vector<String> tempPath = new Vector<String>();
                                Vector<String> tempMethod = new Vector<String>();
                                for (int j3 = 0; j3 < req.getSelectedApiList().length; j3++) {
                                    tempPath.add(req.getSelectedApiList()[j3].getPath());
                                    tempMethod.add(req.getSelectedApiList()[j3].getMethod());
                                }

                                if (req.getApiCategoryName().equals(apiJson[j2].getTags()[0])) {
                                    if (tempPath.contains(getDynamicPathKeys.get(j))
                                            && tempMethod.contains(apiMethod[j2])) {
                                        apiFromJson.add(apiUrlAndMethod);
                                    }
                                }

                            }
                        }
                        apiFromDb.removeAllElements();
                        apiFromDb = mapperSave.onlyApiOfCategory(service_url, req.getApiCategoryName());

                        for (int i = 0; i < apiFromJson.size(); i++) {
                            if (!apiFromDb.contains(apiFromJson.get(i))) {
                                log.info("___4_서비스___서비스있다___카테고리있다___API없다__");
                                apiWillInsert.add(apiFromJson.get(i));
                            }
                        }
                        for (int i = 0; i < apiFromDb.size(); i++) {

                            if (apiFromJson.contains(apiFromDb.get(i))) {
                                log.info("___4_서비스___서비스있다___카테고리있다___API있다__");
                                apiWillUpdate.add(apiFromDb.get(i));
                            }
                        }

                    } else {
                        log.info("___2_API___서비스있다___카테고리없다___________");
                        log.info("___2_API___서비스있다___카테고리없다___API없다__");
                        for (int j3 = 0; j3 < req.getSelectedApiList().length; j3++) {

                            CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                            apiUrlAndMethod.setApi_url(req.getSelectedApiList()[j3].getPath());
                            apiUrlAndMethod.setMethod(req.getSelectedApiList()[j3].getMethod());
                            apiUrlAndMethod.setApi_category_no(
                                    mapperApi.getApiCategoryNo(req.getApiCategoryName(), service_url));
                            apiUrlAndMethod.setIs_deleted("F");

                            apiFromJson.add(apiUrlAndMethod);
                        }

                        apiWillInsert = apiFromJson;
                    }

                } else {
                    log.info("___2_API___서비스없다______________________");
                    log.info("___2_API___서비스없다___카테고리없다___________");
                    log.info("___2_API___서비스없다___카테고리없다___API없다__");
                    for (int j3 = 0; j3 < req.getSelectedApiList().length; j3++) {

                        CheckOverlapApi apiUrlAndMethod = new CheckOverlapApi();
                        apiUrlAndMethod.setApi_url(req.getSelectedApiList()[j3].getPath());
                        apiUrlAndMethod.setMethod(req.getSelectedApiList()[j3].getMethod());
                        apiUrlAndMethod
                                .setApi_category_no(mapperApi.getApiCategoryNo(req.getApiCategoryName(), service_url));
                        apiUrlAndMethod.setIs_deleted("F");
                        apiFromJson.add(apiUrlAndMethod);
                    }
                    apiWillInsert = apiFromJson;
                }
                overLapCommonResponse.setIsExpectedCreate(apiWillInsert);
                overLapCommonResponse.setIsExpectedUpdate(apiWillUpdate);
                overLapCommonResponse.setIsExpectedDelete(apiWillDelete);
                overLapCommonResponse.setOverlap(true);
            } else {
                overLapCommonResponse.setOverlap(false);
            }

            return overLapCommonResponse;
        } catch (IOException e) {
            e.printStackTrace();
            return overLapCommonResponse;
        }
    }
}
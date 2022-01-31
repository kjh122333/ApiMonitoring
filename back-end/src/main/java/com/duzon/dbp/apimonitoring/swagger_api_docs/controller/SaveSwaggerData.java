/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-04 16:02:32 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-12 15:31:25
 */

package com.duzon.dbp.apimonitoring.swagger_api_docs.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import com.duzon.dbp.apimonitoring.dto.ApiCategoryDto;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.ApiDtoSwagger;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.ServiceDtoSwagger;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.SwaggerData;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.overlap.CheckOverlapApi;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save.OverLapAndSaveRequest;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.save.SaveCommonResponse;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.Tags;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.CustomDefinitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.Definitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.DynamicDefinitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.info.Info;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.DynamicPath;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.Paths;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.paths.httpmethod.ApiJson;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperApi;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperApiCategory;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperOverlap;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperSave;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperService;
import com.duzon.dbp.apimonitoring.swagger_api_docs.service.MapperServiceCategory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "SwaggerApiDocs" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/swagger-api-docs-json")
public class SaveSwaggerData {
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

	@ApiOperation(value = "Save json datas from SwaggerApiDocs url")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody

	public SaveCommonResponse SaveSwaggerApiDocs(@RequestBody OverLapAndSaveRequest request) {

		SaveCommonResponse response = new SaveCommonResponse();
		ServiceDtoSwagger serviceDto;
		ApiCategoryDto apiCategoryDto;
		ApiDtoSwagger apiDtoSwagger;

		try {
			Gson gson = new Gson();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			SwaggerData swaggerData = new SwaggerData();
			if (request.getImportType().equals("url")) {
				swaggerData = mapper.readValue(new URL(request.getSwaggerUrl()), SwaggerData.class);
			} else if (request.getImportType().equals("file")) {
				swaggerData = mapper.convertValue(request.getSwaggerFile(), SwaggerData.class);
			}
			Info info = mapper.treeToValue(swaggerData.getInfo(), Info.class);
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

			ArrayList<CustomDefinitions> customDefinitions;
			String totalDefinition = gson.toJson(definitionsMap);
			String serviceUrl = "http://" + swaggerData.getHost() + swaggerData.getBasePath(); // Service URL

			Vector<String> jsonApiCategoryVector = new Vector<String>();
			Vector<String> dbApiCategoryVector = new Vector<String>();
			Vector<String> willDeletedApiCategoryVector = new Vector<String>();
			Vector<String> willInsertApiCategoryVector = new Vector<String>();
			Vector<String> willUpdateApiCategoryVector = new Vector<String>();

			Vector<CheckOverlapApi> jsonApiVector = new Vector<CheckOverlapApi>();
			Vector<CheckOverlapApi> dbApiVector = new Vector<CheckOverlapApi>();
			Vector<CheckOverlapApi> willDeleteApiVector = new Vector<CheckOverlapApi>();
			Vector<CheckOverlapApi> willInsertApiVector = new Vector<CheckOverlapApi>();
			Vector<CheckOverlapApi> willUpdateApiVector = new Vector<CheckOverlapApi>();

			JsonNodeFactory tempDef = JsonNodeFactory.instance; // ! JSONBUILDER
			ObjectNode tempDefs = tempDef.objectNode(); // ! JSONBUILDER
			ObjectNode fullDefs = tempDef.objectNode();
			FindRef findRef = new FindRef(); // ! JSONBUILDER
			tempDefs = findRef.findRefFromDefinition(dynamicDefinitions);
			fullDefs = findRef.ChangeRefToRealJson(tempDefs);
			JsonNode fullDefs_ = mapper.convertValue(fullDefs, JsonNode.class);

			if ((mapperSave.isProperGroupNo(serviceUrl, request.getGroupNo(), request.getServiceCategoryNo()) == null)
					&& (mapperSave.isProperServiceCategoryNo(serviceUrl, request.getGroupNo(),
							request.getServiceCategoryNo()) == null)) {
				jsonApiCategoryVector.clear();
				dbApiCategoryVector.clear();
				willDeletedApiCategoryVector.clear();
				willInsertApiCategoryVector.clear();
				willUpdateApiCategoryVector.clear();

				jsonApiVector.clear();
				dbApiVector.clear();
				willDeleteApiVector.clear();
				willInsertApiVector.clear();
				willUpdateApiVector.clear();
				if ((request.getGroupNo() != null) && (request.getServiceCategoryNo() != null)
						&& ((request.getSwaggerUrl() != null) || (request.getSwaggerFile() != null))
						&& (request.getApiCategoryName() == null) && (request.getEmployee() == null)
						&& (request.getEmployeeSub() == null) && (request.getSelectedApiList() == null)) {
					// log.info("\n___SERVICE ___START
					// _______________________________________________________\n");

					if (mapperSave.isAlreadySavedService(serviceUrl)) {
						// log.info("\n___SERVICE ___UPDATE ___START ________________________\n");

						serviceDto = new ServiceDtoSwagger();
						serviceDto.setUpdated_timestamp(LocalDateTime.now());
						serviceDto.setService_name_kr(info.getTitle());
						serviceDto.setService_url(serviceUrl);
						serviceDto.setService_code(swaggerData.getBasePath());
						if (!totalDefinition.isEmpty()) {
							serviceDto.setDefinitions(totalDefinition);
						} else {
							serviceDto.setDefinitions(null);
						}
						mapperSave.updateService(serviceDto.getUpdated_timestamp(), serviceDto.getService_name_kr(),
								serviceDto.getService_url(), serviceDto.getDefinitions(), serviceDto.getService_code());

						for (int j = 0; j < tags.length; j++) {
							jsonApiCategoryVector.add(tags[j].getName());
						}
						dbApiCategoryVector = mapperSave.totalApiCategory(serviceUrl);
						for (int i = 0; i < jsonApiCategoryVector.size(); i++) {
							if (dbApiCategoryVector.contains(jsonApiCategoryVector.get(i))) {
								willUpdateApiCategoryVector.add(jsonApiCategoryVector.get(i));
							} else
								willInsertApiCategoryVector.add(jsonApiCategoryVector.get(i));
						}
						for (int i = 0; i < dbApiCategoryVector.size(); i++) {
							if (!jsonApiCategoryVector.contains(dbApiCategoryVector.get(i))) {
								willDeletedApiCategoryVector.add(dbApiCategoryVector.get(i));
							}
						}

						if (!willDeletedApiCategoryVector.isEmpty()) {
							for (int i = 0; i < willDeletedApiCategoryVector.size(); i++) {
								mapperSave.deleteApiCategory(willDeletedApiCategoryVector.get(i), LocalDateTime.now());

								if (mapperSave.checkApiOfDeletedCategory(willDeletedApiCategoryVector.get(i))) {
									mapperSave.deleteApi(willDeletedApiCategoryVector.get(i), LocalDateTime.now(),
											request.getUpdateEmployeeId());
								}
							}
						}

						if (!willUpdateApiCategoryVector.isEmpty()) {
							for (int i = 0; i < willUpdateApiCategoryVector.size(); i++) {
								mapperOverlap.updateApiCategory(LocalDateTime.now(),
										willUpdateApiCategoryVector.get(i));
							}
						}
						if (!willInsertApiCategoryVector.isEmpty()) {
							for (int j = 0; j < willInsertApiCategoryVector.size(); j++) {

								apiCategoryDto = new ApiCategoryDto();
								apiCategoryDto.setInsert_timestamp(LocalDateTime.now());
								apiCategoryDto.setUpdated_timestamp(LocalDateTime.now());
								apiCategoryDto.setIs_deleted("F");
								apiCategoryDto.setApi_category_name_kr(willInsertApiCategoryVector.get(j));
								apiCategoryDto.setService_no(mapperApiCategory.getServiceNo(serviceUrl));
								mapperApiCategory.save(apiCategoryDto); /** INSERT DB */
							}
						}
						dbApiVector = mapperSave.totalApi(serviceUrl);
						for (int i = 0; i < getDynamicPathKeys.size(); i++) {
							String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
							String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
							String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
							ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

							for (int j = 0; j < apiJson.length; j++) {
								CheckOverlapApi checkOverlapApiJson = new CheckOverlapApi();
								checkOverlapApiJson.setApi_url(getDynamicPathKeys.get(i));
								checkOverlapApiJson.setMethod(apiMethod[j]);
								checkOverlapApiJson.setApi_category_no(
										mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));

								checkOverlapApiJson.setIs_deleted("F");

								jsonApiVector.add(checkOverlapApiJson);
							}
						}

						if (!dbApiVector.isEmpty()) {
							for (int i = 0; i < dbApiVector.size(); i++) {
								if (!jsonApiVector.contains(dbApiVector.get(i))) {
									willDeleteApiVector.add(dbApiVector.get(i));
								}
							}
						}
						if (!jsonApiVector.isEmpty()) {
							for (int i = 0; i < jsonApiVector.size(); i++) {
								if (dbApiVector.contains(jsonApiVector.get(i))) {
									willUpdateApiVector.add(jsonApiVector.get(i));
								} else {
									willInsertApiVector.add(jsonApiVector.get(i));
								}
							}
						}

						for (int i = 0; i < getDynamicPathKeys.size(); i++) {
							String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
							String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
							String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
							ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

							for (int j = 0; j < apiJson.length; j++) {
								CheckOverlapApi checkUrlApi = new CheckOverlapApi();
								checkUrlApi.setApi_url(getDynamicPathKeys.get(i));
								checkUrlApi.setMethod(apiMethod[j]);

								apiDtoSwagger = new ApiDtoSwagger();
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {

									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}

								apiDtoSwagger.setApi_url(getDynamicPathKeys.get(i));
								apiDtoSwagger.setParameter_type(apiJson[j].getConsumes().toString());
								apiDtoSwagger.setResponse_type(apiJson[j].getProduces().toString());
								apiDtoSwagger.setDelay_status("F");
								apiDtoSwagger.setEmployee_no(0L);
								apiDtoSwagger.setErr_status("F");
								apiDtoSwagger.setInsert_timestamp(LocalDateTime.now());
								apiDtoSwagger.setIs_deleted("F");
								apiDtoSwagger.setMethod(apiMethod[j]);
								apiDtoSwagger.setEmployee_sub_no(0L);
								customDefinitions = new ArrayList<CustomDefinitions>();
								// ! WORK

								if ((Arrays.toString(apiJson[j].getParameters()).length() == 0)
										|| (Arrays.toString(apiJson[j].getParameters()).isEmpty())
										|| (Arrays.toString(apiJson[j].getParameters()) == null)
										|| apiJson[j].getParameters() == null) {

									apiDtoSwagger.setParam(null);
								} else {

									apiDtoSwagger.setParam(Arrays.toString(apiJson[j].getParameters()));

									boolean c = false;
									ArrayNode changepar = (ArrayNode) new ObjectMapper()
											.readTree(Arrays.toString(apiJson[j].getParameters()));

									for (int k = 0; k < apiJson[j].getParameters().length; k++) {
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getParameters()[k];
										if (apiJson[j].getParameters()[k].toString()
												.contains("\"schema\":{\"$ref\":\"#/definitions/")) {
											c = true;
											String doubleQuotesRef = apiJson[j].getParameters()[k].get("schema")
													.get("$ref").toString();
											String refPath = doubleQuotesRef.replace("\"", "");
											int trashPrefix = 13;
											String ref = refPath.substring(trashPrefix + 1);
											CustomDefinitions customDefinition = new CustomDefinitions();
											customDefinition.setType(ref);
											customDefinition.setProperties(definitionsMap.get(ref));
											customDefinitions.add(customDefinition);

											changeSchema.set("schema", fullDefs_.get(ref));

											changepar.set(k, findRef.Change2ndRef(changeSchema, tempDefs));
										}
									}
									if (c == true) {

										apiDtoSwagger.setParam(changepar.toString());

									}
									// log.info("------------------------------------------");
								}
								if (apiJson[j].getResponses() == null) {
									apiDtoSwagger.setResponse_list(null);
								} else {

									apiDtoSwagger.setResponse_list(apiJson[j].getResponses().toString());

									if (apiJson[j].getResponses().toString()
											.contains("\"schema\":{\"$ref\":\"#/definitions/")) {

										String doubleQuotesRef = apiJson[j].getResponses().get("200").get("schema")
												.get("$ref").toString();
										String refPath = doubleQuotesRef.replace("\"", "");
										int trashPrefix = 13;
										String ref = refPath.substring(trashPrefix + 1);
										CustomDefinitions customDefinition = new CustomDefinitions();
										customDefinition.setType(ref);
										customDefinition.setProperties(definitionsMap.get(ref));
										customDefinitions.add(customDefinition);

										ObjectNode changeres = (ObjectNode) apiJson[j].getResponses();
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getResponses().get("200");
										changeSchema.set("schema", fullDefs_.get(ref));
										changeres.set("200", changeSchema);
										apiDtoSwagger
												.setResponse_list(findRef.Change2ndRef(changeres, tempDefs).toString());
									}

								}
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}
								apiDtoSwagger.setApi_category_no(
										mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
								apiDtoSwagger.setUpdated_timestamp(LocalDateTime.now());
								apiDtoSwagger
										.setUpdate_employee_no(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()));
								if (!willDeleteApiVector.isEmpty()) {
									for (int i2 = 0; i2 < willDeleteApiVector.size(); i2++) {

										mapperSave.deleteOnlyApi(LocalDateTime.now(),
												mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
												willDeleteApiVector.get(i2).getApi_url(),
												willDeleteApiVector.get(i2).getMethod(),
												willDeleteApiVector.get(i2).getApi_category_no());
									}
								}
								if (!willInsertApiVector.isEmpty()) {
									CheckOverlapApi checkOverlapApiInsert = new CheckOverlapApi();
									checkOverlapApiInsert.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiInsert.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiInsert.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiInsert.setMethod(apiDtoSwagger.getMethod());
									if (willInsertApiVector.contains(checkOverlapApiInsert)) {
										mapperApi.save(apiDtoSwagger); /** INSERT DB */
									}
								}
								if (!willUpdateApiVector.isEmpty()) {
									CheckOverlapApi checkOverlapApiUpdate = new CheckOverlapApi();
									checkOverlapApiUpdate.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiUpdate.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiUpdate.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiUpdate.setMethod(apiDtoSwagger.getMethod());
									if (willUpdateApiVector.contains(checkOverlapApiUpdate)) {
										mapperOverlap.updateApi(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
												apiDtoSwagger.getParameter_type(), 0L, 0L,
												apiDtoSwagger.getResponse_type(), apiDtoSwagger.getParam(),
												apiDtoSwagger.getResponse_list(), apiDtoSwagger.getDescription(),
												apiDtoSwagger.getApi_category_no(), LocalDateTime.now(),
												apiDtoSwagger.getApi_url(), apiDtoSwagger.getMethod()); /** UPDATE DB */
									}
								}
							}
						}

						response.setSaveSuccess(true);
						response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
								request.getGroupNo(), request.getServiceCategoryNo())));
						response.setProperServiceCategoryName(
								mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
										request.getGroupNo(), request.getServiceCategoryNo())));
						// log.info("\n___SERVICE ___UPDATE ___END __________________________\n");
					} else {
						// log.info("\n___SERVICE ___INSERT(NEW) ___START
						// __________________________\n");

						serviceDto = new ServiceDtoSwagger();
						serviceDto.setService_name_kr(info.getTitle());
						serviceDto.setService_category_no(request.getServiceCategoryNo());
						serviceDto.setGroup_no(request.getGroupNo());
						serviceDto.setInsert_timestamp(LocalDateTime.now());
						serviceDto.setIs_deleted("F");
						serviceDto.setService_state(1L);
						serviceDto.setService_url(serviceUrl);
						serviceDto.setService_code(swaggerData.getBasePath());

						if (!totalDefinition.isEmpty()) {
							serviceDto.setDefinitions(totalDefinition);
						} else {
							serviceDto.setDefinitions(null);
						}
						mapperService.save(serviceDto);
						/** INSERT DB */

						for (int i = 0; i < tags.length; i++) {

							apiCategoryDto = new ApiCategoryDto();
							apiCategoryDto.setInsert_timestamp(LocalDateTime.now());
							apiCategoryDto.setUpdated_timestamp(LocalDateTime.now());
							apiCategoryDto.setIs_deleted("F");
							apiCategoryDto.setApi_category_name_kr(tags[i].getName().toString());
							apiCategoryDto.setService_no(mapperApiCategory.getServiceNo(serviceUrl));
							mapperApiCategory.save(apiCategoryDto); /** INSERT DB */
						}

						for (int i = 0; i < getDynamicPathKeys.size(); i++) {
							String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
							String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
							String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
							ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

							for (int j = 0; j < apiJson.length; j++) {
								apiDtoSwagger = new ApiDtoSwagger();
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								} else {

								}
								apiDtoSwagger.setApi_url(getDynamicPathKeys.get(i));
								apiDtoSwagger.setParameter_type(apiJson[j].getConsumes().toString());
								apiDtoSwagger.setResponse_type(apiJson[j].getProduces().toString());
								apiDtoSwagger.setDelay_status("F");
								apiDtoSwagger.setEmployee_no(0L);
								apiDtoSwagger.setErr_status("F");
								apiDtoSwagger.setInsert_timestamp(LocalDateTime.now());
								apiDtoSwagger.setIs_deleted("F");
								apiDtoSwagger.setMethod(apiMethod[j]);
								apiDtoSwagger.setEmployee_sub_no(0L);
								customDefinitions = new ArrayList<CustomDefinitions>();

								// log.info("_________________________________________");

								// ! WORK
								if ((Arrays.toString(apiJson[j].getParameters()).length() == 0)
										|| (Arrays.toString(apiJson[j].getParameters()).isEmpty())
										|| (Arrays.toString(apiJson[j].getParameters()) == null)
										|| apiJson[j].getParameters() == null) {
									apiDtoSwagger.setParam(null);
								} else {
									apiDtoSwagger.setParam(Arrays.toString(apiJson[j].getParameters()));

									boolean c = false;
									ArrayNode changepar = (ArrayNode) new ObjectMapper()
											.readTree(Arrays.toString(apiJson[j].getParameters()));

									// log.info("" + (apiJson[j].getParameters().getClass()));
									for (int k = 0; k < apiJson[j].getParameters().length; k++) {
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getParameters()[k];
										if (apiJson[j].getParameters()[k].toString()
												.contains("\"schema\":{\"$ref\":\"#/definitions/")) {
											c = true;
											String doubleQuotesRef = apiJson[j].getParameters()[k].get("schema")
													.get("$ref").toString();
											String refPath = doubleQuotesRef.replace("\"", "");
											int trashPrefix = 13;
											String ref = refPath.substring(trashPrefix + 1);
											CustomDefinitions customDefinition = new CustomDefinitions();
											customDefinition.setType(ref);
											customDefinition.setProperties(definitionsMap.get(ref));
											customDefinitions.add(customDefinition);

											changeSchema.set("schema", fullDefs_.get(ref));
											changepar.set(k, changeSchema);
										}
									}
									if (c == true) { // ? WORK2
										apiDtoSwagger.setParam(changepar.toString());
									}
								}
								if (apiJson[j].getResponses() == null) {
									apiDtoSwagger.setResponse_list(null);
								} else {

									apiDtoSwagger.setResponse_list(apiJson[j].getResponses().toString());

									if (apiJson[j].getResponses().toString()
											.contains("\"schema\":{\"$ref\":\"#/definitions/")) {

										String doubleQuotesRef = apiJson[j].getResponses().get("200").get("schema")
												.get("$ref").toString();
										String refPath = doubleQuotesRef.replace("\"", "");
										int trashPrefix = 13;
										String ref = refPath.substring(trashPrefix + 1);
										CustomDefinitions customDefinition = new CustomDefinitions();
										customDefinition.setType(ref);
										customDefinition.setProperties(definitionsMap.get(ref));
										customDefinitions.add(customDefinition);

										ObjectNode changeres = (ObjectNode) apiJson[j].getResponses();
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getResponses().get("200");
										changeSchema.set("schema", fullDefs_.get(ref));
										changeres.set("200", changeSchema);

										// ? WORK2
										apiDtoSwagger
												.setResponse_list(findRef.Change2ndRef(changeres, tempDefs).toString());
									}

								}
								apiDtoSwagger.setApi_category_no(
										mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
								apiDtoSwagger.setUpdated_timestamp(LocalDateTime.now());
								apiDtoSwagger
										.setUpdate_employee_no(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()));
								mapperApi.save(apiDtoSwagger); /** INSERT DB */
							}
						}
						response.setSaveSuccess(true);
						response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
								request.getGroupNo(), request.getServiceCategoryNo())));
						response.setProperServiceCategoryName(
								mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
										request.getGroupNo(), request.getServiceCategoryNo())));
						// log.info("\n___SERVICE ___INSER(NEW) ___END ____________________________\n");
					}

					// log.info("\n___SERVICE
					// ___END__________________________________________________________\n");
				} else if ((request.getGroupNo() != null) && (request.getServiceCategoryNo() != null)
						&& ((request.getSwaggerUrl() != null) || (request.getSwaggerFile() != null))
						&& (request.getUpdateEmployeeId() != null) && (request.getApiCategoryName() != null)
						&& (request.getEmployee() == null) && (request.getEmployeeSub() == null)
						&& (request.getSelectedApiList() == null)) {

					// !!! 카테고리 백터 생성
					jsonApiVector.clear();
					dbApiVector.clear();
					willDeleteApiVector.clear();
					willInsertApiVector.clear();
					willUpdateApiVector.clear();

					String oneApiCategoryUpdate = "";
					String oneApiCategoryInsert = "";
					if (!mapperOverlap.isOverlapService(serviceUrl)) {
						serviceDto = new ServiceDtoSwagger();
						serviceDto.setService_name_kr(info.getTitle());
						serviceDto.setService_category_no(request.getServiceCategoryNo());
						serviceDto.setGroup_no(request.getGroupNo());
						serviceDto.setInsert_timestamp(LocalDateTime.now());
						if (!totalDefinition.isEmpty()) {
							serviceDto.setDefinitions(totalDefinition);
						} else {
							serviceDto.setDefinitions(null);
						}
						serviceDto.setIs_deleted("F");
						serviceDto.setService_state(1L);
						serviceDto.setService_url("http://" + swaggerData.getHost() + swaggerData.getBasePath());
						serviceDto.setService_code(swaggerData.getBasePath());
						mapperService.save(serviceDto); /** INSERT DB */

					}

					// !!! 카테고리 백터 생성
					for (int j = 0; j < tags.length; j++) {
						if (tags[j].getName().equals(request.getApiCategoryName())) {
							jsonApiCategoryVector.add(tags[j].getName());
						}
					}

					dbApiCategoryVector = mapperSave.totalApiCategory(serviceUrl);

					for (int i = 0; i < jsonApiCategoryVector.size(); i++) {
						if (dbApiCategoryVector.contains(jsonApiCategoryVector.get(i))) {
							willUpdateApiCategoryVector.add(jsonApiCategoryVector.get(i));
						} else
							willInsertApiCategoryVector.add(jsonApiCategoryVector.get(i));
					}
					for (int i = 0; i < dbApiCategoryVector.size(); i++) {
						if (!jsonApiCategoryVector.contains(dbApiCategoryVector.get(i))) {
							willDeletedApiCategoryVector.add(dbApiCategoryVector.get(i));
						}
					}

					if (!jsonApiCategoryVector.contains(request.getApiCategoryName())) {
						response.setSaveSuccess(false);
						response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
								request.getGroupNo(), request.getServiceCategoryNo())));
						response.setProperServiceCategoryName(
								mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
										request.getGroupNo(), request.getServiceCategoryNo())));
						return response;
					}
					if (jsonApiCategoryVector.contains(request.getApiCategoryName())
							&& dbApiCategoryVector.contains(request.getApiCategoryName())) {
						oneApiCategoryUpdate = request.getApiCategoryName();
					}
					if (jsonApiCategoryVector.contains(request.getApiCategoryName())
							&& !dbApiCategoryVector.contains(request.getApiCategoryName())) {
						oneApiCategoryInsert = request.getApiCategoryName();
						apiCategoryDto = new ApiCategoryDto();
						apiCategoryDto.setInsert_timestamp(LocalDateTime.now());
						apiCategoryDto.setUpdated_timestamp(LocalDateTime.now());
						apiCategoryDto.setIs_deleted("F");
						apiCategoryDto.setApi_category_name_kr(oneApiCategoryInsert);
						apiCategoryDto.setService_no(mapperApiCategory.getServiceNo(serviceUrl));
						mapperApiCategory.save(apiCategoryDto);
					}

					dbApiVector.clear();
					dbApiVector = mapperSave.onlyApiOfCategory(serviceUrl, request.getApiCategoryName());

					for (int i = 0; i < getDynamicPathKeys.size(); i++) {
						String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
						String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
						String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
						ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

						for (int j = 0; j < apiJson.length; j++) {
							CheckOverlapApi checkOverlapApiJson = new CheckOverlapApi();
							checkOverlapApiJson.setApi_url(getDynamicPathKeys.get(i));
							checkOverlapApiJson.setMethod(apiMethod[j]);
							checkOverlapApiJson.setApi_category_no(
									mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));

							checkOverlapApiJson.setIs_deleted("F");
							if (checkOverlapApiJson.getApi_category_no() != null) {
								jsonApiVector.add(checkOverlapApiJson);
							}
						}
					}

					if (!dbApiVector.isEmpty()) {
						for (int i = 0; i < dbApiVector.size(); i++) {
							if (!jsonApiVector.contains(dbApiVector.get(i))) {
								willDeleteApiVector.add(dbApiVector.get(i));
							}
						}
					}
					if (!jsonApiVector.isEmpty()) {
						for (int i = 0; i < jsonApiVector.size(); i++) {
							if (dbApiVector.contains(jsonApiVector.get(i))) {
								willUpdateApiVector.add(jsonApiVector.get(i));
							} else {
								willInsertApiVector.add(jsonApiVector.get(i));
							}
						}
					}
					if (!oneApiCategoryUpdate.equals("")) {
						mapperOverlap.updateApiCategory(LocalDateTime.now(), oneApiCategoryUpdate);

						for (int i = 0; i < getDynamicPathKeys.size(); i++) {
							String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
							String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
							String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
							ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

							for (int j = 0; j < apiJson.length; j++) {
								CheckOverlapApi checkUrlApi = new CheckOverlapApi();
								checkUrlApi.setApi_url(getDynamicPathKeys.get(i));
								checkUrlApi.setMethod(apiMethod[j]);

								apiDtoSwagger = new ApiDtoSwagger();
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}
								apiDtoSwagger.setApi_url(getDynamicPathKeys.get(i));
								apiDtoSwagger.setParameter_type(apiJson[j].getConsumes().toString());
								apiDtoSwagger.setResponse_type(apiJson[j].getProduces().toString());
								apiDtoSwagger.setDelay_status("F");
								apiDtoSwagger.setEmployee_no(0L);
								apiDtoSwagger.setErr_status("F");
								apiDtoSwagger.setInsert_timestamp(LocalDateTime.now());
								apiDtoSwagger.setIs_deleted("F");
								apiDtoSwagger.setMethod(apiMethod[j]);
								apiDtoSwagger.setEmployee_sub_no(0L);
								customDefinitions = new ArrayList<CustomDefinitions>();

								// ! WORK
								if ((Arrays.toString(apiJson[j].getParameters()).length() == 0)
										|| (Arrays.toString(apiJson[j].getParameters()).isEmpty())
										|| (Arrays.toString(apiJson[j].getParameters()) == null)
										|| apiJson[j].getParameters() == null) {
									apiDtoSwagger.setParam(null);
								} else {
									apiDtoSwagger.setParam(Arrays.toString(apiJson[j].getParameters()));

									boolean c = false;
									ArrayNode changepar = (ArrayNode) new ObjectMapper()
											.readTree(Arrays.toString(apiJson[j].getParameters()));

									for (int k = 0; k < apiJson[j].getParameters().length; k++) {
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getParameters()[k];
										if (apiJson[j].getParameters()[k].toString()
												.contains("\"schema\":{\"$ref\":\"#/definitions/")) {
											c = true;
											String doubleQuotesRef = apiJson[j].getParameters()[k].get("schema")
													.get("$ref").toString();
											String refPath = doubleQuotesRef.replace("\"", "");
											int trashPrefix = 13;
											String ref = refPath.substring(trashPrefix + 1);
											CustomDefinitions customDefinition = new CustomDefinitions();
											customDefinition.setType(ref);
											customDefinition.setProperties(definitionsMap.get(ref));
											customDefinitions.add(customDefinition);

											changeSchema.set("schema", fullDefs_.get(ref));
											changepar.set(k, changeSchema);
										}
									}
									if (c == true) {
										apiDtoSwagger.setParam(changepar.toString());
									}

								}
								if (apiJson[j].getResponses() == null) {
									apiDtoSwagger.setResponse_list(null);
								} else {

									apiDtoSwagger.setResponse_list(apiJson[j].getResponses().toString());

									if (apiJson[j].getResponses().toString()
											.contains("\"schema\":{\"$ref\":\"#/definitions/")) {

										String doubleQuotesRef = apiJson[j].getResponses().get("200").get("schema")
												.get("$ref").toString();
										String refPath = doubleQuotesRef.replace("\"", "");
										int trashPrefix = 13;
										String ref = refPath.substring(trashPrefix + 1);
										CustomDefinitions customDefinition = new CustomDefinitions();
										customDefinition.setType(ref);
										customDefinition.setProperties(definitionsMap.get(ref));
										customDefinitions.add(customDefinition);

										ObjectNode changeres = (ObjectNode) apiJson[j].getResponses();
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getResponses().get("200");
										changeSchema.set("schema", fullDefs_.get(ref));
										changeres.set("200", changeSchema);

										apiDtoSwagger
												.setResponse_list(findRef.Change2ndRef(changeres, tempDefs).toString());

									}

								}
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}
								apiDtoSwagger.setApi_category_no(
										mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
								apiDtoSwagger.setUpdated_timestamp(LocalDateTime.now());
								apiDtoSwagger
										.setUpdate_employee_no(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()));
								if (!willDeleteApiVector.isEmpty()) {
									for (int i2 = 0; i2 < willDeleteApiVector.size(); i2++) {

										mapperSave.deleteOnlyApi(LocalDateTime.now(),
												mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
												willDeleteApiVector.get(i2).getApi_url(),
												willDeleteApiVector.get(i2).getMethod(),
												willDeleteApiVector.get(i2).getApi_category_no());
									}
								}
								if ((!willInsertApiVector.isEmpty())
										&& (apiJson[j].getTags()[0].equals(request.getApiCategoryName()))) {
									CheckOverlapApi checkOverlapApiInsert = new CheckOverlapApi();
									checkOverlapApiInsert.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiInsert.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiInsert.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiInsert.setMethod(apiDtoSwagger.getMethod());
									if (willInsertApiVector.contains(checkOverlapApiInsert)
											&& (jsonApiCategoryVector.contains(request.getApiCategoryName()))) {
										mapperApi.save(apiDtoSwagger); /** INSERT DB */
									}
								}

								if ((!willUpdateApiVector.isEmpty())
										&& (apiJson[j].getTags()[0].equals(request.getApiCategoryName()))) {
									CheckOverlapApi checkOverlapApiUpdate = new CheckOverlapApi();
									checkOverlapApiUpdate.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiUpdate.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiUpdate.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiUpdate.setMethod(apiDtoSwagger.getMethod());
									if (willUpdateApiVector.contains(checkOverlapApiUpdate)
											&& (dbApiCategoryVector.contains(request.getApiCategoryName()))) {
										mapperOverlap.updateApi(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
												apiDtoSwagger.getParameter_type(), 0L, 0L,
												apiDtoSwagger.getResponse_type(), apiDtoSwagger.getParam(),
												apiDtoSwagger.getResponse_list(), apiDtoSwagger.getDescription(),
												apiDtoSwagger.getApi_category_no(), LocalDateTime.now(),
												apiDtoSwagger.getApi_url(), apiDtoSwagger.getMethod()); /** UPDATE DB */
									}
								}
							}
						}
						response.setSaveSuccess(true);
						response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
								request.getGroupNo(), request.getServiceCategoryNo())));
						response.setProperServiceCategoryName(
								mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
										request.getGroupNo(), request.getServiceCategoryNo())));
					}
					if (!oneApiCategoryInsert.equals("")) {

						for (int i = 0; i < getDynamicPathKeys.size(); i++) {
							String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
							String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
							String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
							ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

							for (int j = 0; j < apiJson.length; j++) {
								CheckOverlapApi checkUrlApi = new CheckOverlapApi();
								checkUrlApi.setApi_url(getDynamicPathKeys.get(i));
								checkUrlApi.setMethod(apiMethod[j]);

								apiDtoSwagger = new ApiDtoSwagger();
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}
								apiDtoSwagger.setApi_url(getDynamicPathKeys.get(i));
								apiDtoSwagger.setParameter_type(apiJson[j].getConsumes().toString());
								apiDtoSwagger.setResponse_type(apiJson[j].getProduces().toString());
								apiDtoSwagger.setDelay_status("F");
								apiDtoSwagger.setEmployee_no(0L);
								apiDtoSwagger.setErr_status("F");
								apiDtoSwagger.setInsert_timestamp(LocalDateTime.now());
								apiDtoSwagger.setIs_deleted("F");
								apiDtoSwagger.setMethod(apiMethod[j]);
								apiDtoSwagger.setEmployee_sub_no(0L);
								customDefinitions = new ArrayList<CustomDefinitions>();

								// ! WORK
								if ((Arrays.toString(apiJson[j].getParameters()).length() == 0)
										|| (Arrays.toString(apiJson[j].getParameters()).isEmpty())
										|| (Arrays.toString(apiJson[j].getParameters()) == null)
										|| apiJson[j].getParameters() == null) {
									apiDtoSwagger.setParam(null);
								} else {
									apiDtoSwagger.setParam(Arrays.toString(apiJson[j].getParameters()));

									boolean c = false;
									ArrayNode changepar = (ArrayNode) new ObjectMapper()
											.readTree(Arrays.toString(apiJson[j].getParameters()));

									for (int k = 0; k < apiJson[j].getParameters().length; k++) {
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getParameters()[k];
										if (apiJson[j].getParameters()[k].toString()
												.contains("\"schema\":{\"$ref\":\"#/definitions/")) {
											c = true;
											String doubleQuotesRef = apiJson[j].getParameters()[k].get("schema")
													.get("$ref").toString();
											String refPath = doubleQuotesRef.replace("\"", "");
											int trashPrefix = 13;
											String ref = refPath.substring(trashPrefix + 1);
											CustomDefinitions customDefinition = new CustomDefinitions();
											customDefinition.setType(ref);
											customDefinition.setProperties(definitionsMap.get(ref));
											customDefinitions.add(customDefinition);

											changeSchema.set("schema", fullDefs_.get(ref));
											changepar.set(k, changeSchema);

										}
									}
									if (c == true) {
										apiDtoSwagger.setParam(changepar.toString());
									}

								}
								if (apiJson[j].getResponses() == null) {
									apiDtoSwagger.setResponse_list(null);
								} else {

									apiDtoSwagger.setResponse_list(apiJson[j].getResponses().toString());

									if (apiJson[j].getResponses().toString()
											.contains("\"schema\":{\"$ref\":\"#/definitions/")) {

										String doubleQuotesRef = apiJson[j].getResponses().get("200").get("schema")
												.get("$ref").toString();
										String refPath = doubleQuotesRef.replace("\"", "");
										int trashPrefix = 13;
										String ref = refPath.substring(trashPrefix + 1);
										CustomDefinitions customDefinition = new CustomDefinitions();
										customDefinition.setType(ref);
										customDefinition.setProperties(definitionsMap.get(ref));
										customDefinitions.add(customDefinition);

										ObjectNode changeres = (ObjectNode) apiJson[j].getResponses();
										ObjectNode changeSchema = (ObjectNode) apiJson[j].getResponses().get("200");
										changeSchema.set("schema", fullDefs_.get(ref));
										changeres.set("200", changeSchema);

										apiDtoSwagger
												.setResponse_list(findRef.Change2ndRef(changeres, tempDefs).toString());
									}
								}
								apiDtoSwagger.setDescription(apiJson[j].getSummary());
								if (apiJson[j].getSummary() == "") {
									apiDtoSwagger.setDescription(apiJson[j].getDescription());
								}
								apiDtoSwagger.setApi_category_no(
										mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
								apiDtoSwagger.setUpdated_timestamp(LocalDateTime.now());
								apiDtoSwagger
										.setUpdate_employee_no(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()));

								if ((!willInsertApiVector.isEmpty())
										&& (apiJson[j].getTags()[0].equals(request.getApiCategoryName()))) {
									CheckOverlapApi checkOverlapApiInsert = new CheckOverlapApi();
									checkOverlapApiInsert.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiInsert.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiInsert.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiInsert.setMethod(apiDtoSwagger.getMethod());
									if (willInsertApiVector.contains(checkOverlapApiInsert)
											&& (jsonApiCategoryVector.contains(request.getApiCategoryName()))) {
										mapperApi.save(apiDtoSwagger); /** INSERT DB */
									}
								}
								if ((!willUpdateApiVector.isEmpty())
										&& (apiJson[j].getTags()[0].equals(request.getApiCategoryName()))) {
									CheckOverlapApi checkOverlapApiUpdate = new CheckOverlapApi();
									checkOverlapApiUpdate.setApi_category_no(
											mapperApi.getApiCategoryNo(apiJson[j].getTags()[0], serviceUrl));
									checkOverlapApiUpdate.setApi_url(apiDtoSwagger.getApi_url());
									checkOverlapApiUpdate.setIs_deleted(apiDtoSwagger.getIs_deleted());
									checkOverlapApiUpdate.setMethod(apiDtoSwagger.getMethod());
									if (willUpdateApiVector.contains(checkOverlapApiUpdate)) {
										mapperOverlap.updateApi(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
												apiDtoSwagger.getParameter_type(), 0L, 0L,
												apiDtoSwagger.getResponse_type(), apiDtoSwagger.getParam(),
												apiDtoSwagger.getResponse_list(), apiDtoSwagger.getDescription(),
												apiDtoSwagger.getApi_category_no(), LocalDateTime.now(),
												apiDtoSwagger.getApi_url(), apiDtoSwagger.getMethod()); /** UPDATE DB */
									}
								}
							}
						}
						response.setSaveSuccess(true);
						response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
								request.getGroupNo(), request.getServiceCategoryNo())));
						response.setProperServiceCategoryName(
								mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
										request.getGroupNo(), request.getServiceCategoryNo())));
					}
				} else if ((request.getGroupNo() != null) && (request.getServiceCategoryNo() != null)
						&& ((request.getSwaggerUrl() != null) || (request.getSwaggerFile() != null))
						&& (request.getUpdateEmployeeId() != null) && (request.getApiCategoryName() != null)
						&& (request.getEmployee() != null) && (request.getEmployeeSub() != null)
						&& (request.getSelectedApiList() != null)) {
					// log.info("---[1]API개별 등록 START----------------");
					if (!mapperOverlap.isOverlapService(serviceUrl)) {
						// log.info("---[2]SERVICE 없음----------------");
						serviceDto = new ServiceDtoSwagger();
						serviceDto.setService_name_kr(info.getTitle());
						serviceDto.setService_category_no(request.getServiceCategoryNo());
						serviceDto.setGroup_no(request.getGroupNo());
						serviceDto.setInsert_timestamp(LocalDateTime.now());
						serviceDto.setIs_deleted("F");
						if (!totalDefinition.isEmpty()) {
							serviceDto.setDefinitions(totalDefinition);
						} else {
							serviceDto.setDefinitions(null);
						}
						serviceDto.setService_state(1L);
						serviceDto.setService_url("http://" + swaggerData.getHost() + swaggerData.getBasePath());
						serviceDto.setService_code(swaggerData.getBasePath());
						mapperService.save(serviceDto); /** INSERT DB */

						// log.info("---[1]API개별 등록 START---" + dbApiCategoryVector + "-------------");
					}
					dbApiCategoryVector = mapperSave.totalApiCategory(serviceUrl);
					Vector<String> tempTags = new Vector<String>();
					for (int j = 0; j < tags.length; j++) {
						tempTags.add(tags[j].getName());
					}

					if (tempTags.contains(request.getApiCategoryName())) {
						// log.info("---[3]req.category==json.category----------------");
						if (dbApiCategoryVector.contains(request.getApiCategoryName())) {
							// log.info("---[4]카테고리 업데이트 예정 목록 추가----------------");
							willUpdateApiCategoryVector.add(request.getApiCategoryName());
						}
						if (dbApiCategoryVector.isEmpty()
								|| !dbApiCategoryVector.contains(request.getApiCategoryName())) {

							// log.info("---[4]카테고리 !생성! 예정 목록 추가----------------" + dbApiCategoryVector);
							willInsertApiCategoryVector.add(request.getApiCategoryName());

							// log.info("---[4]카테고리 !생성!----------------");
							apiCategoryDto = new ApiCategoryDto();
							apiCategoryDto.setInsert_timestamp(LocalDateTime.now());
							apiCategoryDto.setUpdated_timestamp(LocalDateTime.now());
							apiCategoryDto.setIs_deleted("F");
							apiCategoryDto.setApi_category_name_kr(request.getApiCategoryName());
							apiCategoryDto.setService_no(mapperApiCategory.getServiceNo(serviceUrl));
							mapperApiCategory.save(apiCategoryDto);
						}
					}

					for (int i = 0; i < getDynamicPathKeys.size(); i++) {

						String apiJsonMethod = mapper.writeValueAsString(dynamicPath[i].getMethods().keySet());
						String[] apiMethod = mapper.readValue(apiJsonMethod, String[].class);
						String apiJsonDetail = mapper.writeValueAsString(dynamicPath[i].getMethods().values());
						ApiJson[] apiJson = mapper.readValue(apiJsonDetail, ApiJson[].class);

						for (int j = 0; j < apiJson.length; j++) {
							CheckOverlapApi checkUrlApi = new CheckOverlapApi();
							checkUrlApi.setApi_url(getDynamicPathKeys.get(i));
							checkUrlApi.setMethod(apiMethod[j]);

							apiDtoSwagger = new ApiDtoSwagger();
							apiDtoSwagger.setDescription(apiJson[j].getSummary());
							if (apiJson[j].getSummary() == "") {
								apiDtoSwagger.setDescription(apiJson[j].getDescription());
							}
							apiDtoSwagger.setApi_url(getDynamicPathKeys.get(i));
							apiDtoSwagger.setParameter_type(apiJson[j].getConsumes().toString());
							apiDtoSwagger.setResponse_type(apiJson[j].getProduces().toString());
							apiDtoSwagger.setDelay_status("F");
							apiDtoSwagger.setEmployee_no(request.getEmployee().getEmployee_no());
							apiDtoSwagger.setErr_status("F");
							apiDtoSwagger.setInsert_timestamp(LocalDateTime.now());
							apiDtoSwagger.setIs_deleted("F");
							apiDtoSwagger.setMethod(apiMethod[j]);
							apiDtoSwagger.setEmployee_sub_no(request.getEmployeeSub().getEmployee_no());
							customDefinitions = new ArrayList<CustomDefinitions>();

							// ! WORK
							if ((Arrays.toString(apiJson[j].getParameters()).length() == 0)
									|| (Arrays.toString(apiJson[j].getParameters()).isEmpty())
									|| (Arrays.toString(apiJson[j].getParameters()) == null)
									|| apiJson[j].getParameters() == null) {
								apiDtoSwagger.setParam(null);
							} else {
								apiDtoSwagger.setParam(Arrays.toString(apiJson[j].getParameters()));

								boolean c = false;
								ArrayNode changepar = (ArrayNode) new ObjectMapper()
										.readTree(Arrays.toString(apiJson[j].getParameters()));

								for (int k = 0; k < apiJson[j].getParameters().length; k++) {
									ObjectNode changeSchema = (ObjectNode) apiJson[j].getParameters()[k];
									if (apiJson[j].getParameters()[k].toString()
											.contains("\"schema\":{\"$ref\":\"#/definitions/")) {
										c = true;
										String doubleQuotesRef = apiJson[j].getParameters()[k].get("schema").get("$ref")
												.toString();
										String refPath = doubleQuotesRef.replace("\"", "");
										int trashPrefix = 13;
										String ref = refPath.substring(trashPrefix + 1);
										CustomDefinitions customDefinition = new CustomDefinitions();
										customDefinition.setType(ref);
										customDefinition.setProperties(definitionsMap.get(ref));
										customDefinitions.add(customDefinition);

										changeSchema.set("schema", fullDefs_.get(ref));
										changepar.set(k, changeSchema);

									}
								}
								if (c == true) {
									apiDtoSwagger.setParam(changepar.toString());
								}
							}
							if (apiJson[j].getResponses() == null) {
								apiDtoSwagger.setResponse_list(null);
							} else {

								apiDtoSwagger.setResponse_list(apiJson[j].getResponses().toString());

								if (apiJson[j].getResponses().toString()
										.contains("\"schema\":{\"$ref\":\"#/definitions/")) {

									String doubleQuotesRef = apiJson[j].getResponses().get("200").get("schema")
											.get("$ref").toString();
									String refPath = doubleQuotesRef.replace("\"", "");
									int trashPrefix = 13;
									String ref = refPath.substring(trashPrefix + 1);
									CustomDefinitions customDefinition = new CustomDefinitions();
									customDefinition.setType(ref);
									customDefinition.setProperties(definitionsMap.get(ref));
									customDefinitions.add(customDefinition);

									ObjectNode changeres = (ObjectNode) apiJson[j].getResponses();
									ObjectNode changeSchema = (ObjectNode) apiJson[j].getResponses().get("200");
									changeSchema.set("schema", fullDefs_.get(ref));
									changeres.set("200", changeSchema);
									apiDtoSwagger
											.setResponse_list(findRef.Change2ndRef(changeres, tempDefs).toString());
								}
							}
							apiDtoSwagger.setDescription(apiJson[j].getSummary());
							if (apiJson[j].getSummary() == "") {
								apiDtoSwagger.setDescription(apiJson[j].getDescription());
							}
							apiDtoSwagger.setApi_category_no(
									mapperApi.getApiCategoryNo(request.getApiCategoryName(), serviceUrl));
							apiDtoSwagger.setUpdated_timestamp(LocalDateTime.now());
							apiDtoSwagger
									.setUpdate_employee_no(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()));

							for (int k = 0; k < request.getSelectedApiList().length; k++) {
								// log.info("---[5]req api----------------");
								if (request.getSelectedApiList()[k].getPath().equals(getDynamicPathKeys.get(i))
										&& request.getSelectedApiList()[k].getMethod().equals(apiMethod[j])
										&& mapperSave.checkMethodAndPath(request.getSelectedApiList()[k].getPath(),
												request.getSelectedApiList()[k].getMethod(),
												request.getApiCategoryName())) {
									// log.info("---[6]req리스트 업데이트들----------------");
									mapperOverlap.updateApi(mapperSave.getEmployeeNo(request.getUpdateEmployeeId()),
											apiDtoSwagger.getParameter_type(), request.getEmployee().getEmployee_no(),
											request.getEmployeeSub().getEmployee_no(), apiDtoSwagger.getResponse_type(),
											apiDtoSwagger.getParam(), apiDtoSwagger.getResponse_list(),
											apiDtoSwagger.getDescription(), apiDtoSwagger.getApi_category_no(),
											LocalDateTime.now(), apiDtoSwagger.getApi_url(),
											apiDtoSwagger.getMethod()); /** UPDATE DB */
									response.setSaveSuccess(true);
								}

								if (request.getSelectedApiList()[k].getPath().equals(getDynamicPathKeys.get(i))
										&& request.getSelectedApiList()[k].getMethod().equals(apiMethod[j])
										&& !mapperSave.checkMethodAndPath(request.getSelectedApiList()[k].getPath(),
												request.getSelectedApiList()[k].getMethod(),
												request.getApiCategoryName())) {
									// log.info("---[6]req리스트 생성들----------------");
									mapperApi.save(apiDtoSwagger);

									/** INSERT DB */
									response.setSaveSuccess(true);
									response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(
											serviceUrl, request.getGroupNo(), request.getServiceCategoryNo())));
									response.setProperServiceCategoryName(
											mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(
													serviceUrl, request.getGroupNo(), request.getServiceCategoryNo())));
									response.setSaveSuccess(true);
								}
							}
						}
					}
					response.setSaveSuccess(true);
				} else {
					response.setSaveSuccess(false);
					response.setProperGroupName(mapperService.getGroupName(mapperSave.isProperGroupNo(serviceUrl,
							request.getGroupNo(), request.getServiceCategoryNo())));
					response.setProperServiceCategoryName(
							mapperService.getServiceCategoryName(mapperSave.isProperServiceCategoryNo(serviceUrl,
									request.getGroupNo(), request.getServiceCategoryNo())));
				}
			} else {
				response.setSaveSuccess(false);
				response.setProperGroupName(mapperService.getGroupName(
						mapperSave.isProperGroupNo(serviceUrl, request.getGroupNo(), request.getServiceCategoryNo())));
				response.setProperServiceCategoryName(mapperService.getServiceCategoryName(mapperSave
						.isProperServiceCategoryNo(serviceUrl, request.getGroupNo(), request.getServiceCategoryNo())));
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return response;
		}
	}
}
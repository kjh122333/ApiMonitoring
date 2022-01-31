/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-15 00:15:59 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-16 00:57:12
 */

package com.duzon.dbp.apimonitoring.swagger_api_docs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.Definitions;

import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.DynamicDefinitions;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.Property_dynamic;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.SpecOfObject_dynamic;
import com.duzon.dbp.apimonitoring.swagger_api_docs.json_entity.swagger.definitions.Types_dynamic;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * FindRef
 */
public class FindRef {

    JsonNodeFactory jnf = JsonNodeFactory.instance; // ! JSONBUILDER
    ObjectNode customizedDefinitions = jnf.objectNode();

    JsonNodeFactory customDef = JsonNodeFactory.instance; // ! JSONBUILDER
    ObjectNode def = new ObjectNode(customDef); // JSONBUILDER-0

    JsonNodeFactory paramOrResponse = JsonNodeFactory.instance; // ! JSONBUILDER
    ObjectNode pr = new ObjectNode(paramOrResponse); // JSONBUILDER-0

    JsonNodeFactory custom2ndDef = JsonNodeFactory.instance; // ! JSONBUILDER
    ObjectNode def2nd = new ObjectNode(custom2ndDef); // JSONBUILDER-0
    ObjectNode on1;

    public ObjectNode findRefFromDefinition(DynamicDefinitions dynamicDefinitions) {
        // log.info("\n\n- - - FindRef - - START - - - - - - - - - - -\n\n");

        try {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

            JsonNodeFactory customDef = JsonNodeFactory.instance; // ! JSONBUILDER
            ObjectNode def; // ! JSONBUILDER
            ObjectNode defKey; // ! JSONBUILDER

            Vector<String> def1VecKey = new Vector<String>();
            Vector<Object> def1Vec = new Vector<Object>();

            SpecOfObject_dynamic temp;
            Property_dynamic dynamicProperty;
            Types_dynamic dynamicPropertyDetail;

            // ! DEFINITION JSON MAKER
            /**
             * SECTION 1. Json 형태만 만들기 1-1. 내용물에 $ref가 있으면 또 만드세요^^;
             */
            def = new ObjectNode(customDef); // JSONBUILDER-0
            def = customDef.objectNode(); // JSONBUILDER-0

            for (String key : dynamicDefinitions.getDynamicDefinitions().keySet()) {

                // NOTE VECTOR로 보관
                def1VecKey.add(key); // ! 1차
                def1Vec.add(dynamicDefinitions.getDynamicDefinitions().get(key)); // ! 1차

                for (int i = 0; i < def1Vec.size(); i++) {
                    JsonNode temp_ = mapper.convertValue(def1Vec.get(i), JsonNode.class);
                    temp = mapper.treeToValue(temp_, SpecOfObject_dynamic.class);
                    dynamicProperty = mapper.treeToValue(temp.getProperties(), Property_dynamic.class); // ! FOCUS
                    defKey = def.putObject(key); // JSONBUILDER-1

                    if (dynamicProperty != null) {
                        // log.info("$$$----------DISCIPTION CUSTOMIZING ----------");
                        for (String key2 : dynamicProperty.getDynamicProperty().keySet()) {
                            // log.info("$$$\tSTART!");
                            dynamicPropertyDetail = mapper.treeToValue(dynamicProperty.getDynamicProperty().get(key2),
                                    Types_dynamic.class); // ! FOCUS
                            StringBuilder keyBuilder = new StringBuilder();
                            StringBuilder valueBuilder = new StringBuilder();
                            String _key = "";
                            String _value = "";
                            keyBuilder.append(key2);
                            keyBuilder.append("(");

                            for (String key3 : dynamicPropertyDetail.getDynamicTypes().keySet()) {
                                if (key3.equals("enum")) {
                                    keyBuilder.append(key3);
                                    keyBuilder.append(", ");
                                }
                                if (key3.equals("type") || key3.equals("format")) {
                                    keyBuilder.append(dynamicPropertyDetail.getDynamicTypes().get(key3).toString());
                                    keyBuilder.append(", ");
                                } else {
                                    if (key3.equals("description")) {
                                        // log.info("$$$\t\t!!! description !!!");
                                        // log.info("$$$\t\t X(type) : !"+ dynamicPropertyDetail.getDynamicTypes().get(key3).toString() +"!");
                                        valueBuilder.append(dynamicPropertyDetail.getDynamicTypes().get(key3).toString());
                                        valueBuilder.append(", ");
                                    }
                                    if (dynamicPropertyDetail.getDynamicTypes().get(key3).toString().contains("definitions")) {
                                        // log.info("$$$\t\t!!! #/ref/~ !!!");
                                        // log.info("$$$\t\t X(type) : !"+ dynamicPropertyDetail.getDynamicTypes().get(key3).toString() +"!");
                                        valueBuilder.append(dynamicPropertyDetail.getDynamicTypes().get(key3).toString());
                                        valueBuilder.append(", ");
                                    }
                                }
                            }
                            if (_key.length() > 0 && _key.charAt(_key.length() - 0) != ')')
                                keyBuilder.append(")");
                            _key = keyBuilder.toString().substring(0, keyBuilder.toString().length());
                            _value = valueBuilder.toString().substring(0, valueBuilder.toString().length());
                            if (_key.length() > 0 && _key.charAt(_key.length() - 2) == ',')
                                _key = _key.substring(0, _key.length() - 2);
                            if (_value.length() > 0 && _value.charAt(_value.length() - 2) == ',')
                                _value = _value.substring(0, _value.length() - 2);

                            defKey.put(_key + ")", _value); // JSONBUILDER-2
                            // log.info("$$$\tEND!");
                        }
                        // log.info("$$$-------------------------------------------");
                    }
                }
            }
            customizedDefinitions = def.deepCopy();
            // log.info("\n\n- - - FindRef - - END - - - - - - - - - - - -\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customizedDefinitions;
    }

    public ObjectNode ChangeRefToRealJson(ObjectNode json) {

        // log.info("\n\n- - - ChangeRefToRealJson - - START - - - - -\n\n");

        try {

            ObjectNode defKey; // ! JSONBUILDER
            Vector<String> def1VecKey = new Vector<String>();
            String x = "";
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            Property_dynamic disassemble1 = mapper.treeToValue(json, Property_dynamic.class);
            def1VecKey.addAll(disassemble1.getDynamicProperty().keySet());
            def = customDef.objectNode();
            if (disassemble1 != null) {
                for (String key1 : disassemble1.getDynamicProperty().keySet()) {

                    Property_dynamic disassemble2 = mapper.treeToValue(disassemble1.getDynamicProperty().get(key1),
                            Property_dynamic.class);

                    defKey = def.putObject(key1);
                    if (disassemble2 != null) {
                        for (String key2 : disassemble2.getDynamicProperty().keySet()) {

                            String[] findRefArray = disassemble2.getDynamicProperty().get(key2).asText().split("/");
                            Vector<String> findRefVector = new Vector<String>();
                            findRefVector.addAll(Arrays.asList(findRefArray));

                            if (findRefVector.contains("definitions")) { // ! REF확인

                                x = disassemble2.getDynamicProperty().get(key2).asText();
                                x = new String(x.replaceAll("}", ""));
                                String[] xTemp = x.split("/");
                                x = new String(xTemp[xTemp.length - 1]);
                                if (disassemble1.getDynamicProperty().containsKey(x)) {

                                    defKey.set(key2, json.get(x));

                                }
                            } else {
                                defKey.put(key2, disassemble2.getDynamicProperty().get(key2).asText()); // JSONBUILDER-2

                            }
                        }
                    }
                }
            }
            // log.info("\n\n-- - - - - -\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // log.info("\n\n- - - ChangeRefToRealJson - - END - - - - - -\n\n");
        return def;
    }

    public ObjectNode Change2ndRef(ObjectNode target, ObjectNode list)
            throws JsonParseException, JsonMappingException, IOException {
        // log.info("\n\n- - - Change2ndRef - - START - - - - -\n\n");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode exJsonNode = (JsonNode) new ObjectMapper().readTree(target.toString());
        ObjectNode exObjectNode = (ObjectNode) exJsonNode;

        ObjectNode on2;
        ObjectNode on3;
        ObjectNode on4;

        if (!exObjectNode.isNull()) {
            on1 = new ObjectNode(custom2ndDef);
            on1 = custom2ndDef.objectNode();
            Map<String, Object> exMap1 = mapper.convertValue(exObjectNode,
                    new TypeReference<HashMap<String, Object>>() {
                    });
            for (String k1 : exMap1.keySet()) {

                if (exMap1.get(k1).getClass().getSimpleName().equals("LinkedHashMap")) {
                    on2 = on1.putObject(k1);
                } else {
                    on2 = on1.put(k1, exMap1.get(k1).toString());
                }
                if ((exMap1.get(k1) != null) && (exMap1.get(k1).getClass().getSimpleName().equals("LinkedHashMap"))) {
                    Map<String, Object> exMap2 = mapper.convertValue(exMap1.get(k1),
                            new TypeReference<HashMap<String, Object>>() {
                            });
                    for (String k2 : exMap2.keySet()) {

                        if (exMap2.get(k2).getClass().getSimpleName().equals("LinkedHashMap")) {
                            on3 = on2.putObject(k2);
                        } else {
                            on3 = on2.put(k2, exMap2.get(k2).toString());
                        }

                        if ((exMap2.get(k2) != null)
                                && (exMap2.get(k2).getClass().getSimpleName().equals("LinkedHashMap"))) {
                            Map<String, Object> exMap3 = mapper.convertValue(exMap2.get(k2),
                                    new TypeReference<HashMap<String, Object>>() {
                                    });
                            for (String k3 : exMap3.keySet()) {

                                if (exMap3.get(k3).getClass().getSimpleName().equals("LinkedHashMap")) {
                                    on4 = on3.putObject(k3);
                                } else {
                                    on4 = on3.put(k3, exMap3.get(k3).toString());
                                }
                                if ((exMap3.get(k3) != null)
                                        && (exMap3.get(k3).getClass().getSimpleName().equals("LinkedHashMap"))) {
                                    Map<String, Object> exMap4 = mapper.convertValue(exMap3.get(k3),
                                            new TypeReference<HashMap<String, Object>>() {
                                            });
                                    for (String k4 : exMap4.keySet()) {

                                        String compare = (String) exMap4.get(k4);
                                        String resultTemp = "";
                                        if (compare.contains("#/definitions/")) {
                                            if (compare.charAt(compare.length() - 1) == '}') {
                                                String temp = compare.replace("}", "");
                                                resultTemp = temp.substring(temp.lastIndexOf("/") + 1);
                                            } else {
                                                resultTemp = compare.substring(compare.lastIndexOf("/") + 1);
                                            }
                                            if (list.has(resultTemp)) {
                                                on4 = on4.set(k4, list.get(resultTemp));
                                            } else {
                                                on4 = on3.put(k4, exMap4.get(k4).toString());
                                            }

                                        } else {
                                            on4 = on4.put(k4, exMap4.get(k4).toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // log.info("\n\n- - - Change2ndRef - - END - - - - - -\n\n");
        return on1;
    }
}
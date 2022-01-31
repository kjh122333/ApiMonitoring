package com.duzon.dbp.apimonitoring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

/**
 * JsonParseService
 */
@Service
public class JsonParseService {

	// Employee Map<String, Object>
	public Map<String, Object> jsonEmployeeMapParse(Map<String, Object> list) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<String, Object>();
        for (String key : list.keySet()) {
            if (list.get(key) != null) {
                map.put(key, list.get(key));
            }
        }
        map.remove("columns_config");
		map.remove("password");

        Map<String, Object> columns_config = null;
        if (list.get("columns_config") != null) {
            try {
                columns_config = objectMapper.readValue(list.get("columns_config").toString(), new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
		map.put("columns_config", columns_config);

        return map;
    }
    
    // Employee List<Map<String, Object>>
	public List<Map<String, Object>> jsonEmployeeListMapParse(List<Map<String, Object>> list) {
        List<Map<String, Object>> test = new ArrayList<Map<String, Object>>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : list.get(i).keySet()) {
                if (list.get(i).get(key) != null) {
                    map.put(key, list.get(i).get(key));
                }
            }
            map.remove("columns_config");
			map.remove("password");

            Map<String, Object> columns_config = null;
            if (list.get(i).get("columns_config") != null) {
                try {
                    columns_config = objectMapper.readValue(list.get(i).get("columns_config").toString(), new TypeReference<Map<String, Object>>() {});
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
			map.put("columns_config", columns_config);
			
            test.add(map);
        }

        return test;
    }

    // Api Map<String, Object>
    public Map<String, Object> jsonMapParse(Map<String, Object> list) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(list);

        map.remove("response_list");
        map.remove("param");
        map.remove("parameter_type");

        List<Map<String, String>> param = null;
        Map<String, Object> response_list = null;
        List<String> parameter_type = null;

        if (list.get("param") != null) {
            try {
                param = objectMapper.readValue(list.get("param").toString(), new TypeReference<List<Map<String, String>>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (list.get("response_list") != null) {
            try {
                response_list = objectMapper.readValue(list.get("response_list").toString(), new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (list.get("parameter_type") != null) {
            try {
                parameter_type = objectMapper.readValue(list.get("parameter_type").toString(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        map.put("param", param);
        map.put("response_list", response_list);
        map.put("parameter_type", parameter_type);

        return map;
    }
    
    // Api List<Map<String, Object>>
	public List<Map<String, Object>> jsonListMapParse(List<Map<String, Object>> list) {
	    List<Map<String, Object>> test = new ArrayList<Map<String, Object>>();
	    ObjectMapper objectMapper = new ObjectMapper();
	
	    for (int i = 0; i < list.size(); i++) {
	        Map<String, Object> map = new HashMap<String, Object>();
	
	        for (String key : list.get(i).keySet()) {
	            if (list.get(i).get(key) != null) {
	                map.put(key, list.get(i).get(key));
	            }
	        }
	
	        map.remove("response_list");
	        map.remove("param");
	        map.remove("parameter_type");
	        map.remove("response_type");
	
	        Map<String, Object> response_list = null;
	        List<Map<String, Object>> param = null;
	        List<String> parameter_type = null;
	        List<String> response_type = null;
	
	        if (list.get(i).get("param") != null) {
	            try {
	                param = objectMapper.readValue(list.get(i).get("param").toString(), new TypeReference<List<Map<String, Object>>>() {});
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        }
	        if (list.get(i).get("response_list") != null) {
	            try {
	                response_list = objectMapper.readValue(list.get(i).get("response_list").toString(), new TypeReference<Map<String, Object>>() {});
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        }
	        if (list.get(i).get("parameter_type") != null) {
	            try {
	                parameter_type = objectMapper.readValue(list.get(i).get("parameter_type").toString(), new TypeReference<List<String>>() {});
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        }
	        if (list.get(i).get("response_type") != null) {
	            try {
	                response_type = objectMapper.readValue(list.get(i).get("response_type").toString(), new TypeReference<List<String>>() {});
	            } catch (JsonProcessingException e) {
	                e.printStackTrace();
	            }
	        }
	
	        map.put("param", param);
	        map.put("response_list", response_list);
	        map.put("parameter_type", parameter_type);
	        map.put("response_type", response_type);
	
	        test.add(map);
	    }
	    return test;
    }
    
    // Service Map<String, Object>
	public Map<String, Object> jsonMapParseService(Map<String, Object> list) {
		ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<String, Object>();
        map.putAll(list);

        map.remove("definitions");

        Map<String, Object> definitions = null;

        if (list.get("definitions") != null) {
            try {
                definitions = objectMapper.readValue(list.get("definitions").toString(), new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        map.put("definitions", definitions);

        return map;
    }

    // Service List<Map<String, Object>>
    public List<Map<String, Object>> jsonListMapParseService(List<Map<String, Object>> list) {
	    List<Map<String, Object>> test = new ArrayList<Map<String, Object>>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (String key : list.get(i).keySet()) {
                if (list.get(i).get(key) != null) {
                    map.put(key, list.get(i).get(key));
                }
            }

            map.remove("definitions");
            
            Map<String, Object> definitions = null;
            if (list.get(i).get("definitions") != null) {
                try {
                    definitions = objectMapper.readValue(list.get(i).get("definitions").toString(), new TypeReference<Map<String, Object>>() {});
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
			map.put("definitions", definitions);
			
            test.add(map);
        }

        return test;
    }
    
    // Employee Info List<Map<String, Object>>
	public List<Map<String, Object>> jsonEmployeeInfoListMapParse(List<Map<String, Object>> errlist, List<Map<String, Object>> delaylist) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	    for (int i = 0; i < errlist.size(); i++) {
	        Map<String, Object> map = new HashMap<String, Object>();
	
	        for (String key : errlist.get(i).keySet()) {
	            if (errlist.get(i).get(key) != null) {
	                map.put(key, errlist.get(i).get(key));
	            }
	        }
	
	        list.add(map);
        }
        
        for (int i = 0; i < delaylist.size(); i++) {
	        Map<String, Object> map = new HashMap<String, Object>();
	
	        for (String key : delaylist.get(i).keySet()) {
	            if (delaylist.get(i).get(key) != null) {
	                map.put(key, delaylist.get(i).get(key));
	            }
	        }
	
	        list.add(map);
        }
        Collections.sort(list, new Comparator<Map<String, Object >>() {
            @Override
            public int compare(Map<String, Object> first, Map<String, Object> second) {
                return ((String) second.get("insert_timestamp")).compareTo((String) first.get("insert_timestamp")); //descending order
            }
        });

        return list;
	}
}
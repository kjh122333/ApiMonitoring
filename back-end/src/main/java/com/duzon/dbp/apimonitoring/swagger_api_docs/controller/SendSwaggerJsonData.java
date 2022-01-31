/*
 * @Author: junghwan.kong 
 * @Date: 2019-12-09 13:33:05 
 * @Last Modified by: junghwan.kong
 * @Last Modified time: 2019-12-10 13:03:01
 */

package com.duzon.dbp.apimonitoring.swagger_api_docs.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "SwaggerApiDocs" })
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/swagger-api-docs-json")
public class SendSwaggerJsonData {

    @ApiOperation(value = "return Json Data")
    @RequestMapping(value = "/jsonfiles", method = RequestMethod.GET)
    @ResponseBody
    public String ReturnSwaggerJsonFromFile(@RequestParam String req) {
        String res = "";
        InputStream input;

        try {
            input = SendSwaggerJsonData.class.getResourceAsStream("/BOOT-INF/classes/static/" + req + ".json");
            if (input == null) {
                input = SendSwaggerJsonData.class.getClassLoader().getResourceAsStream(req);
            }

            BufferedReader r = new BufferedReader(new InputStreamReader(input));

            // reads each line
            String l;
            while ((l = r.readLine()) != null) {
                res = res + l;
            }
            input.close();
        } catch (Exception e) {

            res = "backend.json\n" + "eapprovals.json\n" + "expense.json\n" + "note.json\n" + "personal.json\n"
                    + "todo.json\n" + "wecrm.json\n";

        }
        return res;
    }
}

/*
 * ====================================================================
 * Copyright 2005-2011 Wai-Lun Kwok
 *
 * http://www.kwoksys.com/LICENSE
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package com.kwoksys.framework.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpUtils class.
 */
public class HttpUtils {

    /**
     * Gets contents from url
     * @param url
     * @return
     * @throws Exception
     */
    public static String getContent(String url) throws Exception {
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception("HTTP method error: " + method.getStatusLine());
            }

            // Read the response body.
            return new String(method.getResponseBody());

        } finally {
            // Release the connection.
            method.releaseConnection();
        }
    }

    public static void setDownloadResponseHeaders(HttpServletResponse response, String fileName) {
        // To work around file download problems on IE
        response.setHeader("Pragma", "public"); // For HTTP/1.0 backward compatibility.
        response.setHeader("Cache-Control", "public"); // For HTTP/1.1.

        // Set filename
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }
}

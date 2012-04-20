/*
 * Copyright 2010 Brian Sanders
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.codehaus.groovy.grails.plugins.ibatis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.codehaus.groovy.grails.commons.GrailsClass;

public class GatewayArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "Gateway";
    private static Log log = LogFactory.getLog(GatewayArtefactHandler.class);

    public GatewayArtefactHandler() {
        super(TYPE, GrailsClass.class, DefaultGrailsGatewayClass.class, null);
        log.debug("Created instance");
    }

    @Override
    public boolean isArtefactClass(Class aClass) {
        log.debug("Checking potential artefact class " + aClass);
        return aClass != null && aClass.getName().endsWith("Gateway");
    }

    @Override
    public boolean isArtefactGrailsClass(GrailsClass grailsClass) {
        log.debug("Checking potential artefact class " + grailsClass + " within isArtefactGrailsClass");
        return super.isArtefactGrailsClass(grailsClass);
    }
}

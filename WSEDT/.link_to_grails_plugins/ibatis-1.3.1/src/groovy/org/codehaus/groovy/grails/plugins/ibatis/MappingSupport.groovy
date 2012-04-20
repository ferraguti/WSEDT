/*
 * Copyright 2010-2011 Brian Sanders
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

package org.codehaus.groovy.grails.plugins.ibatis

import javax.sql.DataSource
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.codehaus.groovy.grails.commons.GrailsClassUtils

class MappingSupport {

  private log = LogFactory.getLog(MappingSupport)

  def getArtefactResourcePaths(GrailsApplication application) {
    def resources = []
    for (a in application.getArtefacts(GatewayArtefactHandler.TYPE)) {
      def filename = getIbatisFilename(a.fullName)
      def ibatisFile = getResource(filename)
      if (ibatisFile.exists()) {
        resources << ibatisFile
      }
    }
    resources
  }

  private Resource getResource(String name) {
    def r = new FileSystemResource("grails-app/gateways/$name")
    if (!r.exists()) {
      r = new ClassPathResource(name)
    }
    r
  }

  def getOperationIds(mapping) {
    def operationIds = [:]
    ['select', 'insert', 'update', 'delete', 'procedure', 'statement'].each {opType ->
      operationIds[opType] = mapping."$opType".list().collect { it.@id.text() }
    }
    log.debug("Found mappings: " + operationIds)
    return operationIds
  }

  private invokeMybatis(id, opType, multiplicity, args, application) {
    def opName = "${opType}${multiplicity}"
    def sessionTemplate = application.mainContext.getBean('sqlSessionTemplate')
    sessionTemplate."$opName"(id, args)
  }

  def registerMappings(GrailsClass g, GrailsApplication application) {
    log.debug("Registering mappings for class " + g)
    def operationIds = [:]
    def namespace
    String filename = getIbatisFilename(g.fullName)
    try {
      def mappingXml = loadIbatisFile(filename, g)
      namespace = mappingXml.@namespace.text()
      operationIds = getOperationIds(mappingXml)
    } catch (e) {
      log.error("Failed to load iBATIS SQL map file ${filename}", e)
    }
    def mc = g.clazz.metaClass
    operationIds.each {String opType, ids ->
      ids.each {String id ->
        def ibatisOp = "${namespace}.$id"
        if (g.clazz.methods.find { it.name == id}) {
          id = "generated${id[0].toUpperCase()}${id.substring(1)}"
        }
        log.debug "Adding method $id to metaclass $mc"
        def postFix = (opType == 'select' ? (isListOp(g, id) ? "List" : "One") : "")
        mc."$id" = {args ->
          invokeMybatis(ibatisOp, opType, postFix, args, application)
        }
      }
    }
  }

  private shortenName(s) {
    if (s.indexOf('Gateway') != -1) {
      s = s.substring(0, s.lastIndexOf('Gateway'))
    }
    s
  }

  // We will assume this is a list operation if the operation id contains the plural form of the name,
  // like "getShinyBaubles" as opposed to "getBaubleByID".  You can force this behavior by
  // including a static property 'forceAsListOps' on the Gateway class.
  protected isListOp(GrailsClass g, String id) {
    def listOps = GrailsClassUtils.getStaticPropertyValue(g.clazz, "forceAsListOps")
    if (listOps?.find {it == id}) {
      return true
    }
    def coreName = g.name.contains('.') ? shortenName(g.name).tokenize('.')[-1] : shortenName(g.name)
    id.toLowerCase().contains(coreName.toLowerCase() + 's')
  }

  private loadIbatisFile(String filename, GrailsClass g, boolean validating = true) {
    def text
    def gatewaysDir = new File("./grails-app/gateways")
    if (gatewaysDir.exists()) {
      text = new File(gatewaysDir, filename).text
    } else {
      text = g.clazz.getResource("/" + filename).text
    }
    new XmlSlurper(validating, true).parseText(text)
  }

  protected getIbatisFilename(String fullName) {
    def trimmed = shortenName(fullName)
    def packageName
    def className
    if (trimmed.indexOf('.') == -1) {
      packageName = ""
      className = trimmed
    } else {
      packageName = trimmed.subSequence(0, trimmed.lastIndexOf('.')+1)
      className = trimmed.substring(trimmed.lastIndexOf('.')+1)
    }
    def fileName = new StringBuilder()
    fileName << className[0].toLowerCase()
    className[1..-1].each { c ->
      char c2 = c
      fileName << (c2.isUpperCase() ? "-" + c2.toLowerCase() : c)
    }
    packageName.replace('.', '/') + fileName + ".xml"
  }

  private relativeFileName(File f) {
    f.absoluteFile.name - f.parentFile.absoluteFile.name
  }
}

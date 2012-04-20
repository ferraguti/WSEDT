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

package grails.test

import org.codehaus.groovy.grails.commons.DefaultGrailsClass
import org.codehaus.groovy.grails.plugins.ibatis.MappingSupport
import org.mybatis.spring.SqlSessionFactoryBean

abstract class GatewayIntegrationTest extends GrailsUnitTestCase {

  def grailsApplication
  private Object _gateway
  private mappingSupport = new MappingSupport()

  protected getGateway() {
    if (!_gateway) {
      _gateway = createGateway()
    }
    _gateway
  }

  private createGateway() {
    def gatewayClassName = getGatewayClassName(getClass())
    def gatewayClass = getClass().classLoader.loadClass(gatewayClassName)
    def gc = new DefaultGrailsClass(gatewayClass)
    mappingSupport.registerMappings(gc, grailsApplication)
    gatewayClass.newInstance()
  }

  static String getGatewayClassName(clazz) {
    println clazz
    clazz.name.substring(0, clazz.name.lastIndexOf('Test'))
  }
}

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsClass
import org.codehaus.groovy.grails.plugins.ibatis.GatewayArtefactHandler
import org.codehaus.groovy.grails.plugins.ibatis.MappingSupport

class IbatisGrailsPlugin {
  def author = "Brian Sanders"
  def authorEmail = "brian.j.sanders@gmail.com"
  def title = "MyBatis (mybatis.org) support for Grails"
  def description = '''
Implements the Table Data Gateway (http://martinfowler.com/eaaCatalog/tableDataGateway.html) pattern
using MyBatis.  This plugin doesn't currently interact with GORM (other than sharing a datasource definition);
rather, it's a complementary approach.

Features:
* Adds the target 'create-gateway [name]' for creating a gateway class and its corresponding MyBatis mapper XML file
* Each named operation in the mapping file becomes a method on the gateway class.  For example, an operation
<select id="getAccout"> becomes a method MyGateway.getAccount()
'''
  def documentation = "http://grails.org/plugin/ibatis"

  static def VERSION = '1.3.1'
  def version = VERSION
  def grailsVersion = "1.3 > *"
  def dependsOn = [dataSource: "1.3 > *"]

  def pluginExcludes = [ "docs/**/*",
                         "grails-app/views/**/*",
                         "grails-app/gateways/**/*",
                         "grails-app/conf/**/*",
                         "grails-app/utils/**/*",
                         "test/**/*",
                         "lib/build/*",
                         "lib/compile/*",
                         "lib/runtime/*",
                         "lib/test/*",
                         "web-app/**/*"]

  def artefacts = [GatewayArtefactHandler]
  def watchedResources = "file:./grails-app/gateways/**/*"

  def log = LogFactory.getLog(IbatisGrailsPlugin)
  def mappingSupport = new MappingSupport()

  def doWithSpring = {
    def ssfb = sqlSesssionFactoryBean(org.mybatis.spring.SqlSessionFactoryBean) {
      dataSource = ref('dataSource')
      mapperLocations = mappingSupport.getArtefactResourcePaths(application)
    }
    sqlSessionTemplate(org.mybatis.spring.SqlSessionTemplate, ref('sqlSesssionFactoryBean'))

    log.debug "Looking for artifacts of type " + GatewayArtefactHandler.TYPE
    for (a in application.getArtefacts(GatewayArtefactHandler.TYPE)) {
      log.debug "Found gateway artifact $a of type ${a.clazz}; will register as ${a.shortName}"
      if (a) {
        "${a.shortName}"(a.clazz) { bean ->
          bean.singleton = true
          bean.autowire = "byName"
        }
      }
    }
  }

  def doWithDynamicMethods = { ctx ->
    GrailsClass[] gateways = application.getArtefacts(GatewayArtefactHandler.TYPE)
    log.debug "Gateways length is ${gateways?.length}"
    gateways.each { mappingSupport.registerMappings(it, application) }
  }
}

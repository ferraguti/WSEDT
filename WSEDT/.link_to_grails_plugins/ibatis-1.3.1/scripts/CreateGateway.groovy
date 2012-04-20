import org.springframework.core.io.FileSystemResource

includeTargets << grailsScript("_GrailsInit")
includeTargets << grailsScript("_GrailsCreateArtifacts")

target(default: "Create a new Gateway class and corresponding iBATIS file") {
  depends(checkVersion, parseArguments)

  def type = "Gateway"
  promptForName(type: type)

  for (moduleName in argsMap["params"]) {
    moduleName = purgeRedundantArtifactSuffix(moduleName, type)
    def packageName = moduleName.lastIndexOf('.') == -1 ? grailsAppName : moduleName.substring(0, moduleName.lastIndexOf('.') + 1)
    def packageRelPath = packageName.replace('.', '/') + '/'
    def mappingName = moduleName.substring(moduleName.lastIndexOf('.') + 1)

    def fileName = new StringBuilder()
    fileName << mappingName[0].toLowerCase()
    mappingName[1..-1].each { c ->
      char c2 = c
      fileName << (c2.isUpperCase() ? "-" + c2.toLowerCase() : c)
    }
    def mapPath = "$basedir/grails-app/gateways/${packageRelPath}${fileName}.xml"
    createArtifact(name: moduleName, suffix: type, type: type, path: "grails-app/gateways")
    createIntegrationTest(name: moduleName, suffix: type, path: "test/integration/gateways", superClass: 'GatewayIntegrationTest')

    def templateRelPath = "src/templates/artifacts/Gateway.xml"
    def templatePath = new FileSystemResource("$basedir/$templateRelPath")
    if (!templatePath.exists()) {
      templatePath = resolveResources("file:${pluginsHome}/*/$templateRelPath")[0]
    }
    copyGrailsResource(mapPath, templatePath)
    ant.replace(file: mapPath) {
      replaceFilter(token: "@mapper.className@", value: moduleName.toLowerCase())
      replaceFilter(token: "@mapper.tableName@", value: mappingName.toLowerCase())
    }
    event("CreatedFile", [ mapPath ])
  }
}

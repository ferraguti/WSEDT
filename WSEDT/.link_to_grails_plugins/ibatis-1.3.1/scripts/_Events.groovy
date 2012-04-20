import org.codehaus.groovy.grails.exceptions.CompilationFailedException

boolean inApp = !new File('./IbatisGrailsPlugin.groovy').exists()

def getIbatisFilename = {String fullName ->
  def trimmed = shortenName(fullName)
  def packageName
  def className
  if (trimmed.indexOf('.') == -1) {
    packageName = ""
    className = trimmed
  } else {
    packageName = trimmed.subSequence(0, trimmed.lastIndexOf('.') + 1)
    className = trimmed.substring(trimmed.lastIndexOf('.') + 1)
  }
  def fileName = new StringBuilder()
  fileName << className[0].toLowerCase()
  className[1..-1].each {c ->
    char c2 = c
    fileName << (c2.isUpperCase() ? "-" + c2.toLowerCase() : c)
  }
  packageName.replace('.', '/') + fileName + ".xml"
}

def validateSource(File srcDir) {
  assert srcDir?.directory
  def ibatisFiles = srcDir.listFiles().findAll {it.name ==~ /.+\.xml/}
  def gateways = srcDir.listFiles().findAll {it.name ==~ /.+Gateway\.groovy/}

  def gatewayNames = gateways.collect {it.name - '.groovy'}
  gatewayNames.each {String gatewayName ->
    def ibatisFile = new File(srcDir, getIbatisFilename(gatewayName.replace(File.separator, '.')))
    if (ibatisFiles.find {it.canonicalPath == ibatisFile.canonicalPath}) {
      ibatisFiles = ibatisFiles - ibatisFile
      gatewayNames = gatewayNames - gatewayName
    }
  }
  if (ibatisFiles || gatewayNames) {
    def msg = "iBATIS plugin detected missing XML files or Gateway classes: ${ibatisFiles} ${gatewayNames}"
    throw new CompilationFailedException(msg)
  }
}

eventCompileEnd = {e ->
  if (inApp && new File('./grails-app/gateways').exists()) {
    /* validateSource('./grails-app/gateways' as File) */
    ant.copy(todir: grailsSettings.classesDir.path) {
      fileset(dir: './grails-app/gateways') {
        include(name: '**/*.xml')
      }
    }
  }
}

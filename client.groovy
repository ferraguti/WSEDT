#!/usr/bin/env groovy
@Grab(group='org.codehaus.groovy.modules', module='groovyws', version='0.5.2')
import groovyx.net.ws.WSClient


def getProxy(wsdl, classLoader) {
  new WSClient(wsdl, classLoader)
}
proxy = new WSClient("http://localhost:8080/WSEDT/services/rechercherSalles?wsdl", this.class.classLoader)
proxy.initialize()

result = proxy.rechercherSallesLight(2012, 6, 4, 14, 30)
println "Salle availlable : ${result}"
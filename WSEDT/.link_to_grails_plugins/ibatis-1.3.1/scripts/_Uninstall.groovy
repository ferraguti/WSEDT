def d = new File("${basedir}/grails-app/gateways")
if (d.exists() && d.listFiles().length == 0) {
  ant.rmdir(dir:d)
}
grails.work.dir = 'work'
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
  inherits 'global'
  repositories {
    grailsHome()
    mavenCentral()
  }
  dependencies {
    compile 'org.mybatis:mybatis:3.0.4'
    compile('org.mybatis:mybatis-spring:1.0.0') {
      transitive = false
    }
  }
  log 'warn'
}


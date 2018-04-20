/* REMOVED NOW DUE TO DYNAMIC PLUGIN LOAD */
grails.plugin.location.backend = "../OpenLabBackend"

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        excludes "bcpg-jdk15", "bcprov-jdk15"
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        grailsCentral()
        mavenCentral()
        mavenRepo "http://repo.grails.org/grails/repo/"

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        /*build 'org.bouncycastle:bcpg-jdk15on:1.50'
        build 'org.bouncycastle:bcprov-jdk15on:1.50'
        compile "org.docx4j:docx4j-ImportXHTML:3.2.2"
        compile "net.sf.jtidy:jtidy:r938"   */

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
		
        // runtime 'mysql:mysql-connector-java:5.1.21'
    }
}

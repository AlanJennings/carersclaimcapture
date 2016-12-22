node ('master') {
    def server = Artifactory.server('newlab-artifactory')
    def artifactoryGradle = Artifactory.newGradleBuild()
    artifactoryGradle.tool = 'Gradle321' // Tool name from Jenkins configuration
    artifactoryGradle.deployer repo:'libs-snapshot-local', server: server
    artifactoryGradle.resolver repo:'repo', server: server
    artifactoryGradle.deployer.deployMavenDescriptors = true
    artifactoryGradle.deployer.deployIvyDescriptors = true
    artifactoryGradle.deployer.ivyPattern = '[organisation]/[module]/ivy-[revision].xml'
    artifactoryGradle.deployer.artifactPattern = '[organisation]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]'
    artifactoryGradle.deployer.mavenCompatible = true
    artifactoryGradle.deployer.usesPlugin = true

    def buildInfo = Artifactory.newBuildInfo()
    buildInfo.env.capture = true
    buildInfo.env.filter.addInclude("")
    buildInfo.env.filter.addExclude("DONT_COLLECT*")
    
    def artifactoryMaven = Artifactory.newMavenBuild()
    artifactoryMaven.resolver releaseRepo:'repo', snapshotRepo:'repo', server: server


    stage ('Checkout') {
        checkout scm
    }
    stage ('Build') {
        try {
            artifactoryGradle.run switches: '-Dgradle.user.home=$JENKINS_HOME/.gradle', buildFile: 'build.gradle', tasks: 'clean test build sourcesJar artifactoryPublish', buildInfo: buildInfo, server: server
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'Test Report'])
            junit keepLongStdio: true, testResults: 'build/test-results/test/*.xml'
        } catch (Exception e) {
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'Test Report'])
            junit allowEmptyResults: true, keepLongStdio: true, testResults: 'build/test-results/test/*.xml'
            throw e;
        }
        
    }
    stage ('Publish build info') {
        server.publishBuildInfo buildInfo
    }
    stage ('Deploy to lab') {
        sshagent(['8b4a081b-f1d6-424d-959f-ae9279d08b3b']) {
            sh 'scp c3-*-SNAPSHOT-full.war c3javalab@37.26.89.94:c3java-latest-SNAPSHOT-full.war'
            sh 'ssh c3javalab@37.26.89.94 "sudo /home/c3javalab/deploy.sh restart"'
        }
    }


}

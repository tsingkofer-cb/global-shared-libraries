def call(String fileName = '', String controllerUrl = '', String jobPath = '', String waitForResult = 'true'){
    //Get CLI jar
    sh 'curl -O $JENKINS_URL/jnlpJars/jenkins-cli.jar'

    //Construct params
    def DEMO_TF_VARS = readFile "$fileName"

    //Trigger build
    withCredentials([usernamePassword(credentialsId: 'oc-api-token', passwordVariable: 'JENKINS_API_TOKEN', usernameVariable: 'JENKINS_USER_ID')]) {
        sh """java -jar jenkins-cli.jar -s $controllerUrl -webSocket build $jobPath -f -p TF_VARS_PARAM="$DEMO_TF_VARS" """
    }
}

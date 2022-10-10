def call(Map config){

    //Get CLI jar
    sh 'curl -O $JENKINS_URL/jnlpJars/jenkins-cli.jar'

    //Construct params
    def DEMO_TF_VARS = readFile "$config.fileName"
    def PARAMETER_STRING = ''
    for (key in config.keySet()){
        if (key.startsWith("PARAM")){
            PARAMETER_STRING = "$PARAMETER_STRING" + ' -p ' + key + '=' + config.get(key)
        }
    }

    //Trigger build
    withCredentials([usernamePassword(credentialsId: 'oc-api-token', passwordVariable: 'JENKINS_API_TOKEN', usernameVariable: 'JENKINS_USER_ID')]) {
        sh """java -jar jenkins-cli.jar -s $config.controllerUrl -webSocket build $config.jobPath -f $PARAMETER_STRING -p TF_VARS_PARAM="$DEMO_TF_VARS" """
    }
}

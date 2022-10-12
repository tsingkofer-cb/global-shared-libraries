// syntax to call from a pipeline:
// cliTriggerJobWithParams PARAM_SDLC_ENV: 'SDLC', PARAM_ENV_ALIAS: 'ts', fileName: 'demo.tfvars', controllerUrl: 'JENKINS_URL', jobPath: 'folderName/jobName'

def call(Map config){

    //Get CLI jar
    sh 'curl -O $JENKINS_URL/jnlpJars/jenkins-cli.jar'

    //Construct params based on what's in the Map
    def PARAMETER_STRING = ''
    for (key in config.keySet()){
        if (key.startsWith("PARAM")){
            PARAMETER_STRING = "$PARAMETER_STRING" + ' -p ' + key + '=' + config.get(key)
        }
    }

    //read file into a variable so it can be passed as a param
    def DEMO_TF_VARS = readFile "$config.fileName"

    //Trigger build
    withCredentials([usernamePassword(credentialsId: 'oc-api-token', passwordVariable: 'JENKINS_API_TOKEN', usernameVariable: 'JENKINS_USER_ID')]) {
        sh """java -jar jenkins-cli.jar -s $config.controllerUrl -webSocket build $config.jobPath -f $PARAMETER_STRING -p TF_VARS_PARAM="$DEMO_TF_VARS" """
    }

    //Cleanup CLI jar file
    sh 'rm jenkins-cli.jar'
}

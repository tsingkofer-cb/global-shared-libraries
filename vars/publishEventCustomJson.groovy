def call(String jobPath, String fileName = 'demo.tfvars') {
    sh "awk -v ORS='\\\\n' 1 $fileName > replaced.txt"
    archiveArtifacts artifacts: "$fileName,replaced.txt", followSymlinks: false
    DEMO_TF_VARS = readFile 'replaced.txt'
    publishEvent event: jsonEvent("""{"triggerTag":"$jobPath", "tfVars": "$DEMO_TF_VARS"}"""), verbose: true
}
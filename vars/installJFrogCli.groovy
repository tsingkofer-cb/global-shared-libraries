def call(Map config){
    // withCredentials([usernamePassword(credentialsId: '', passwordVariable: 'ARTIFACTORY_PSW', usernameVariable: 'ARTIFACTORY_USR')]){
        sh '''#!/bin/bash
        function ver { printf "%03d%03d%03d%03d" $(echo "$1" | tr '.' ' '); }

        if ! command -v jf 2>&1 >/dev/null 
        then
            echo "JFrog CLI not found. Proceeding with installation.";
            # get install script from Artifactory...
            # curl -u $ARTIFACTORY_USR:$ARTIFACTORY_PSW https://artifactory/path/to/install/script/install-cli.sh -o install-cli.sh

            # get from JFrog site directly...
            curl -fL https://install-cli.jfrog.io -o install-cli.sh

            #install a specific version of the CLI
            sh install-cli.sh 2.71.3
        else
            echo "JFrog CLI already installed."
            jf --version
            INSTALLED_VERSION=$(jf --version | cut -c11-)
            if [ $(ver ${INSTALLED_VERSION}) -lt $(ver 2.71.3) ]
            then
                echo "Newer approved version available."
                #install the latest version of the CLI
                sh install-cli.sh 2.71.3
            else
                echo "This is the latest version."
            fi
        fi
        '''
    // }
}
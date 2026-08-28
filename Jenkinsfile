#!/usr/bin/env groovy

pipeline {
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    agent any
    tools {
        jdk "jdk-21"
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build and Deploy') {
            steps {
                echo 'Building and Deploying to Maven'
					sh './gradlew checkSyntax buildAll checkDatagen :Xplat:publish :Fabric:publish :NeoForge:publish'
					sh 'scripts/check_artifacts.sh'
					sh 'scripts/check_server_boot.sh'
                }
            }
        }
    post {
        always {
            archiveArtifacts artifacts: 'Fabric/build/libs/Psi-fabric-*.jar,NeoForge/build/libs/Psi-neoforge-*.jar,Xplat/build/libs/Psi-xplat-*.jar'
        }
    }
}

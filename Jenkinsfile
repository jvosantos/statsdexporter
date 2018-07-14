String name = "statfulstatstdexporter"
String devProject = "hold-card"
String prodProject = "hold-prod"
String secretKeyLocation = "/usr/opt/gcloud/.config/kube-jenkins.json"
String sonarqube_host = "http://sonarqube.hold.co"

groovy.lang.GString releases = "${name}-${UUID.randomUUID().toString()}"
podTemplate(name: "jnlp",
        label: releases,
        namespace: "jenkins",
        containers: [
                containerTemplate(name: 'gcloud-docker', image: 'gcr.io/hold-card/ops/gcloud-docker:v.2', ttyEnabled: true,
                        envVars: [
                                envVar(key: 'SECRET_KEY_LOCATION', value: secretKeyLocation)
                        ])
        ],
        volumes: [
                hostPathVolume(mountPath: '/usr/bin/docker', hostPath: '/usr/bin/docker'),
                hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
                emptyDirVolume(mountPath: '/home', memory: false),
                secretVolume(secretName: 'kube-jenkins-gcloud-secret', mountPath: '/usr/opt/gcloud/.config')
        ])
        {

            node(releases) {
                stage('artifacts-build-and-release') {
                    checkout scm
                    container('gcloud-docker') {
                        currentBuild.displayName = env.BUILD_NUMBER
                        currentBuild.description = env.BUILD_NUMBER

                        withCredentials([usernamePassword(credentialsId: 'sonar', usernameVariable: 'sonar_user', passwordVariable: 'sonar_password')]) {
                            sh "docker build -t ${name}:${env.BUILD_NUMBER} . --build-arg sonar_host=${sonar_host} --build-arg sonar_user=${sonar_user} --build-arg sonar_password=${sonar_password}"
                        }
                    }
                }

                stage('docker-image-build-and-release') {
                    container('gcloud-docker') {
                        dockerBuild(devProject, name)
                        dockerBuild(prodProject, name)
                    }
                }
            }
        }

def dockerBuild(project, name) {
    sh "docker tag ${name}:${env.BUILD_NUMBER} gcr.io/${project}/statful/${name}:${env.BUILD_NUMBER}"
    sh "docker push gcr.io/${project}/statful/${name}:${env.BUILD_NUMBER}"
    sh "docker rmi \$(docker images | grep gcr.io/${project}/statful/${name} | awk '{print \$3}') -f"
}

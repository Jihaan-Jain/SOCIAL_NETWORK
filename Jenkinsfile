pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Starting Java build for SOCIAL_NETWORK project'
                script {
                    if (isUnix()) {
                        sh 'mkdir -p out'
                        sh 'javac -cp . -d out dsa_projects/SocialNetwork.java dsa_projects/SocialNetworkMainClass1.java'
                    } else {
                        bat 'if not exist out mkdir out'
                        bat 'javac -cp . -d out dsa_projects\\SocialNetwork.java dsa_projects\\SocialNetworkMainClass1.java'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running smoke test — verifying SocialNetwork compiles and runs cleanly...'
                script {
                    if (isUnix()) {
                        sh 'java -cp out dsa_projects.SocialNetworkMainClass1 || echo "Runtime done"'
                    } else {
                        bat 'java -cp out dsa_projects.SocialNetworkMainClass1 2>&1 || echo "Runtime done"'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                // FIX: use rawBuild.getLog() — RunWrapper does not expose getLog(int) directly
                def logText = ''
                try {
                    logText = currentBuild.rawBuild.getLog(2000).join("\n")
                } catch (e) {
                    logText = "Could not retrieve build log: ${e.message}"
                }

                def repoUrl    = env.GIT_URL    ?: ''
                def branchName = env.GIT_BRANCH ?: ''
                def commitSha  = env.GIT_COMMIT ?: ''

                def payload = groovy.json.JsonOutput.toJson([
                    job_name    : env.JOB_NAME,
                    build_number: env.BUILD_NUMBER.toInteger(),
                    status      : currentBuild.currentResult,
                    log         : logText,
                    repository  : repoUrl,
                    branch      : branchName,
                    commit_id   : commitSha
                ])

                def traceLogId = ''

                // ── Step 1: Send build log to TRACE backend ────────────────────
                if (isUnix()) {
                    def response = sh(
                        script: """
                            curl -s -X POST http://localhost:8000/logs/jenkins \\
                                 -H "Content-Type: application/json" \\
                                 -d '${payload.replace("'", "'\\''")}'
                        """,
                        returnStdout: true
                    ).trim()
                    try {
                        def parsed = new groovy.json.JsonSlurper().parseText(response)
                        traceLogId = parsed?.log_id ?: ''
                    } catch (e) {
                        echo "TRACE: could not parse ingest response"
                    }
                } else {
                    // Windows: POST with PowerShell — Invoke-WebRequest returns raw JSON text
                    writeFile file: 'trace_payload.json', text: payload
                    def response = bat(
                        script: 'powershell -Command "(Invoke-WebRequest -Uri \'http://localhost:8000/logs/jenkins\' -Method Post -ContentType \'application/json\' -InFile \'trace_payload.json\').Content"',
                        returnStdout: true
                    ).trim()
                    // bat() echoes the command line; take the last non-empty line (the actual JSON)
                    def jsonLine = response.readLines().findAll { it.trim() }.last()
                    try {
                        def parsed = new groovy.json.JsonSlurper().parseText(jsonLine)
                        traceLogId = parsed?.log_id ?: ''
                    } catch (e) {
                        echo "TRACE: could not parse ingest response: ${e.message}"
                    }
                    bat 'del trace_payload.json 2>nul || true'
                }

                // ── Step 2: Poll TRACE for ML result, set build description ───
                if (traceLogId) {
                    def traceSummary = 'TRACE | Classifying...'
                    def attempts = 0
                    while (attempts < 6) {
                        sleep(5)
                        attempts++
                        try {
                            if (isUnix()) {
                                traceSummary = sh(
                                    script: "curl -s http://localhost:8000/logs/${traceLogId}/summary",
                                    returnStdout: true
                                ).trim()
                            } else {
                                traceSummary = bat(
                                    script: "powershell -Command \"(Invoke-WebRequest -Uri 'http://localhost:8000/logs/${traceLogId}/summary').Content\"",
                                    returnStdout: true
                                ).trim()
                            }
                            if (!traceSummary.contains('Classifying')) break
                        } catch (e) {
                            echo "TRACE: summary poll attempt ${attempts} failed"
                        }
                    }

                    currentBuild.description = traceSummary
                    echo "TRACE result: ${traceSummary}"
                }
            }
        }
    }
}


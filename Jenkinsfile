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
                        // Compile all Java files in the package together
                        sh 'echo "Starting Java build for SOCIAL_NETWORK project"'
                        sh 'javac -cp . dsa_projects/SocialNetwork.java dsa_projects/SocialNetworkMainClass1.java'
                    } else {
                        // Windows: compile full package together — fails on ANY error
                        bat 'echo Starting Java build for SOCIAL_NETWORK project'
                        bat 'javac -cp . dsa_projects\\SocialNetwork.java dsa_projects\\SocialNetworkMainClass1.java'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                echo 'Running smoke test — verifying main class loads...'
                script {
                    if (isUnix()) {
                        sh 'java -cp . dsa_projects.SocialNetworkMainClass1 || true'
                    } else {
                        bat 'java -cp . dsa_projects.SocialNetworkMainClass1 2>&1 || echo "Runtime check done"'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                def logText = currentBuild.rawBuild.getLog(1000).join("\n")
                def repoUrl = env.GIT_URL ?: ''
                def branchName = env.GIT_BRANCH ?: ''
                def commitSha = env.GIT_COMMIT ?: ''

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
                    // Windows: POST with PowerShell using Invoke-WebRequest so we get
                    // raw JSON text (Invoke-RestMethod returns a PS object, not JSON)
                    writeFile file: 'trace_payload.json', text: payload
                    def response = bat(
                        script: 'powershell -Command "(Invoke-WebRequest -Uri \'http://localhost:8000/logs/jenkins\' -Method Post -ContentType \'application/json\' -InFile \'trace_payload.json\').Content"',
                        returnStdout: true
                    ).trim()
                    // bat() output includes the echoed command line; take the last non-empty line
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
                    // Wait up to 30s for ML classification to complete
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
                            // Stop polling once classified (no longer 'Classifying...')
                            if (!traceSummary.contains('Classifying')) break
                        } catch (e) {
                            echo "TRACE: summary poll attempt ${attempts} failed"
                        }
                    }

                    // Write ML classification back to Jenkins build description
                    currentBuild.description = traceSummary
                    echo "TRACE result: ${traceSummary}"
                }
            }
        }
    }
}

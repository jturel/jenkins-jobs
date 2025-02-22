def ghprb_git_checkout() {
    checkout changelog: true, poll: false, scm: [
        $class: 'GitSCM',
        branches: [[name: '${sha1}']],
        doGenerateSubmoduleConfigurations: false,
        extensions: [[$class: 'PreBuildMerge', options: [fastForwardMode: 'FF', mergeRemote: 'origin', mergeTarget: '${ghprbTargetBranch}']]],
        submoduleCfg: [],
        userRemoteConfigs: [
            [refspec: '+refs/heads/${ghprbTargetBranch}:refs/remotes/origin/${ghprbTargetBranch} +refs/pull/${ghprbPullId}/*:refs/remotes/origin/pr/${ghprbPullId}/*', url: 'https://github.com/${ghprbGhRepository}']
        ]
    ]
}

def git_hash(ref = 'HEAD') {
    return sh(script: "git rev-parse ${ref}", returnStdout: true).trim()
}

def archive_git_hash(ref = 'HEAD') {
    def hash = git_hash(ref)
    writeFile(file: 'commit', text: hash)
    archiveArtifacts(artifacts: 'commit')
    return hash
}

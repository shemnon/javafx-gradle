/*
 * Copyright (c) 2011-2012, Danno Ferrin
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of Danno Ferrin nor the
 *         names of contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.bitbucket.shemnon.javafxplugin.tasks;

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import org.gradle.process.internal.DefaultExecAction
import org.gradle.process.internal.ExecAction

/**
 * Created by IntelliJ IDEA.
 * User: dannoferrin
 * Date: 3/8/11
 * Time: 9:07 PM
 */
class GenKeyTask extends ConventionTask {

    @OutputFile
    File keyStore

    String alias
    String dname
    Integer validity // conventions don't play nice with primitives
    String keyPass
    String storePass
    String storeType

    @TaskAction
    processResources() {
        if (getKeyStore().exists()) return

        ExecAction aaptExec = new DefaultExecAction()
        aaptExec.workingDir = project.projectDir
        aaptExec.executable = "${System.properties['java.home']}/bin/keytool"

        def args = []
        args << '-genkeypair'
        ['alias', 'dname', 'validity', 'keyPass', 'keyStore', 'storePass', 'storeType'].each {
            if (this[it]) {
                args << "-${it.toLowerCase()}" << this[it] as String
            }
        }

        // [-v]
        // [-protected]
        // [-keyalg <keyalg>]
        // [-keysize <keysize>]
        // [-sigalg <sigalg>]
        // [-providername <name>]
        // [-providerclass <provider_class_name> [-providerarg <arg>]] ...
        // [-providerpath <pathlist>]


        aaptExec.args = args

        ExecResult exec = aaptExec.execute()
        exec.assertNormalExitValue()
    }

}

JavaFX Gradle Plugin
====================

This plugin will ultimately provide gradle build tasks for the JavaFX Deployment tools in the Java 7 JDK.

Hopefully this plugin will also set conventions that a Maven Mojo could follow as well.


To Test Drive
=============
This plugin works with either Java 7 or Java 8 (except the `ConferenceScheduleApp` demo requires JavaFX 8).  For Windows installers you need the pre-requisite third party items installed.

    #install the plugin to your local maven repo
    gradle install
    
    #Build the samples
    cd samples/<sample of choice>
    gradle assemble

The samples will land in `samples/<sample>/build/distributions/bundles`.

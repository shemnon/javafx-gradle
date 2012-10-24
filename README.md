JavaFX Gradle Plugin
====================

This plugin will ultimately provide gradle build tasks for the JavaFX Deployment tools in the Java 7 JDK.

Hopefully this plugin will also set conventions that a Maven Mojo could follow as well.


To Test Drive
=============
This plugin works with either Java 7 or Java 8, but the `ConferenceScheduleApp` demo requires JavaFX 8.  So these steps presume that you are running JDK8-ea-b61 or later.  Also, for the installers you need the pre-requisite third party items installed (this is mostly a windows issue)

    #install the plugin to your local maven repo
    gradle install
    
    #Build the samples
    cd samples
    gradle assemble

The samples will land in `samples/<sample>/build/distributions/bundles`.

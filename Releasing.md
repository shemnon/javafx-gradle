
# Golden Testing Rules
Build on the lowest supported JDK, *i.e. Java 1.7*
Test sample apps on all versions of java

# release

1. upgrade version number in ./gradle-javafx-plugin/build.gradle
1. In the root directory `gradle clean upload`

    Do not release the jars yet on bintray

1. verify samples
1. upgrade all plugin scripts in the plugin directories to a production plugin
1. commit
1. write release blog post
1. add new plugin scripts to bintray
1. upgrade the global plugin scripts on bintray
1. push commits to bitbucket
1. release the jars on bintray
1. post release blog post
1. upgrade the version in ./gradle-javafx-plugin/build.gradle to the next snapshot
1. downgrade all plugin scripts in the plugin directories to a snapshot plugin
1. verify cloudbees has published the new snapshot
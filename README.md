# impinj-test
this repo is for test code related to the impinj rfid reader

This project depends on non-maven packages that need to be downloaded and installed manually. 
1. The Impinj Octane SDK [download here](https://support.impinj.com/hc/en-us/articles/360010077479-Octane-SDK-Installation-Instructions)
    * Once downloaded, extract the zip file and install the jar into the local maven repository with the following command:
        ```
        mvn install:install-file -Dfile=OctaneSDKJava-3.3.0.0-jar-with-dependencies.jar -DgroupId=com.impinj -DartifactId=octane -Dversion=3.3.0.0 -Dpackaging=jar
       ```
1. (Eventually) the AWS IoT SDK for Java [download here](https://github.com/aws/aws-iot-device-sdk-java-v2#build-iot-device-sdk-from-source)

After installing the dependencies, the app can be installed with `mvn clean install`
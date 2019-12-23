# Developement

> ⚠️ This documentation will be useful if you plan to contribute to the development of the plugin

## Configuration

* Install [java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (JDK)
* Install [Atlassian Plugin SDK](https://marketplace.atlassian.com/apps/1210950/atlassian-plugin-sdk-windows?hosting=server&tab=overview)

## Run the plugin localy

Clear target folder
```batch
atlas-clean
```

Generate the plugin jar and obr
```batch
atlas-package
```

Launch a local instance of Bitbucket to test the plugin
```batch
atlas-run --jvmargs '-Xms1g -Xmx2g'
```

[INFO] bitbucket started successfully in 145s at http://localhost:7990/bitbucket

http://localhost:7990/bitbucket
with user : **admin** and password : **admin**

Install the plugin
```batch
atlas-install-plugin
```

## IDE (VSCODE)

* https://code.visualstudio.com/docs/languages/java
* https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack


### Debuging

Add the following remote debug configuration in vscode 
https://code.visualstudio.com/docs/editor/debugging
```json
{
    "type": "java",
    "name": "Debug (Attach) - Atlassian",
    "request": "attach",
    "hostName": "localhost",
    "port": 5005
}
```

To be able to connect your IDE to the java instance runnig , you have to launch Bitbucet using
```batch
atlas-debug
```
> ⚠ Debuging is consuming more ressources than standard launch (with ``atlas-run``)
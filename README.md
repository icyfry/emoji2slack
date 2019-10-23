# emoji2slack bitbucket plugin

This plugin send messages to slack when a emoji is used in comments in Bitbucket.

## Developement

### Configuration

* Install [java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (JDK) (not more)
* Install [Atlassian Plugin SDK](https://marketplace.atlassian.com/apps/1210950/atlassian-plugin-sdk-windows?hosting=server&tab=overview)


### IDE (VSCODE)

TODO

### Run the plugin localy

Clear target folder
```batch
atlas-clean
```

Build the plugin and lauch local instance of Bitbucket
```batch
atlas-run
```

http://localhost:7990/bitbucket
with user : **admin** and password : **admin**
# emoji2slack bitbucket plugin

<aside class="notice">
THIS PLUGIN IS NOT FUNCTIONAL YET
</aside>

This plugin send messages to slack when an emoji is used in comments in Bitbucket.

## Developement

### Configuration

* Install [java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (JDK)
* Install [Atlassian Plugin SDK](https://marketplace.atlassian.com/apps/1210950/atlassian-plugin-sdk-windows?hosting=server&tab=overview)


### IDE (VSCODE)

TODO

### Run the plugin localy

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

http://localhost:7990/bitbucket
with user : **admin** and password : **admin**

Install the plugin
```batch
atlas-install-plugin
```
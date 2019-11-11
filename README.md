# emoji2slack bitbucket plugin

This plugin send messages to slack when an emoji is used in comments in Bitbucket.

1. Configure the token (xoxb-....) of a bot installed on your Slack channel in the global settings of the plugin (bitbucket/plugins/servlet/emoji2slack/settings)
2. Link a emoji to a channel in a repository
3. Write a message on a PR containing the emoji, a message will be send to the channel configured 

> Version 1.0.0 currently under work

## UI

🎨 The ui is not ready yet

## API

Return the list of all emojis
```
GET
/bitbucket/rest/emoji2slack/latest/emojis
```

Return the emojis configurations related to the plugin on a specific repository
```
GET
/bitbucket/rest/emoji2slack/latest/emojis/configurations/{repositoryid}
```

Add a new emoji configuration
```
POST
/bitbucket/rest/emoji2slack/latest/emojis/configurations/add
```

```json
{
    "channelId": "string",
    "emojiShortcut": "string",
    "repositoryId": "int"
}
```

Delete a emoji configuration (with internal id)
```
DELETE
/bitbucket/rest/emoji2slack/latest/emojis/configurations/{id}
```

Retrive or modify global configuration of the plugin
```
GET or POST
/bitbucket/rest/emoji2slack/latest/configuration
```

```json
{
    "botAccessToken": "string"
}
```

## Pages

Global configuration page
```
bitbucket/plugins/servlet/emoji2slack/settings
```

Repository configuration page
```
bitbucket/plugins/servlet/emoji2slack/project/{PROJECT}/{REPOSITORY}/settings
```

## Projects used

* [slack-client](https://github.com/HubSpot/slack-client)
* [vue-aui](https://spartez.github.io/vue-aui/#/)

## Developement

> ⚠️ The documentation bellow will be useful if you plan to contribute to the development of the plugin

### Configuration

* Install [java 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (JDK)
* Install [Atlassian Plugin SDK](https://marketplace.atlassian.com/apps/1210950/atlassian-plugin-sdk-windows?hosting=server&tab=overview)

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

[INFO] bitbucket started successfully in 145s at http://localhost:7990/bitbucket

http://localhost:7990/bitbucket
with user : **admin** and password : **admin**

Install the plugin
```batch
atlas-install-plugin
```

### IDE (VSCODE)

* https://code.visualstudio.com/docs/languages/java
* https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack


#### Debuging

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

## References

* [emoji-list on unicode.org](http://unicode.org/emoji/charts/full-emoji-list.html)
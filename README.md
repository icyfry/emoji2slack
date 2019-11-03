# emoji2slack bitbucket plugin

> NOT FUNCTIONAL YET 😅

This plugin send messages to slack when an emoji is used in comments in Bitbucket.

[emoji-list on unicode.org](http://unicode.org/emoji/charts/full-emoji-list.html)


## UI

<img src="./doc/capture_1.png" />

## API

Return the list of all emojis
```
GET
/bitbucket/rest/emoji2slack/1.0/emojis
```

Return the emojis configurations related to the plugin on a specific repository
```
GET
/bitbucket/rest/emoji2slack/1.0/configurations/list?repositoryid={id}
```

Add a new emoji configuration
```
POST
/bitbucket/rest/emoji2slack/1.0/configurations/add
```

```json
{
    "channelId": "string",
    "emojiShortcut": "string",
    "repositoryId": "int"
}
```



## Projects used

* [slack-client](https://github.com/HubSpot/slack-client)

## Developement


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

https://bitbucket.org/DACOFFEY/wiki/wiki/BITBUCKET/EMOJI/Emoji?_ga=2.194310062.2040885428.1572033028-866815006.1570574180


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

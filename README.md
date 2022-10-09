> This repository is archived as the plugin is discontinued

# emoji2slack Bitbucket plugin
![GitHub Workflow Status](https://github.com/icyfry/emoji2slack/workflows/Java%20CI/badge.svg)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/icyfry/emoji2slack?include_prereleases&sort=semver)
![Bitbucket version](https://img.shields.io/badge/Bitbucket%20server%20version-6.5.1-blue?logo=Bitbucket)

<b>This plugin send messages to Slack when an emoji is used in comments in Bitbucket.</b>

1. Configure the token (xoxb-....) of a bot installed on your Slack channel in the global settings of the plugin
2. Link a emoji to a channel in a repository
3. Write a message on a PR containing the emoji, a message will be send to the channel configured 

## UI

### Global plugin configuration
You can configure the link to a bitbucket bot in the administration of Bitbucket
> Administration / Slack integration (Emojis)

![capture_2](doc/capture_2.png)

### Pull request configurations
You can link a channel to a emoji on a specific repository
> Repository settings / Slack integration (Emojis)

![capture_1](doc/capture_1.png)

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

## References

* [emoji-list on unicode.org](http://unicode.org/emoji/charts/full-emoji-list.html)

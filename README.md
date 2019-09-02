Salesforce Data Store for Fess [![Build Status](https://travis-ci.org/codelibs/fess-ds-salesforce.svg?branch=master)](https://travis-ci.org/codelibs/fess-ds-salesforce)
==========================

## Overview

Salesforce Data Store is an extension for Fess Data Store Crawling.

## Download

See [Maven Repository](http://central.maven.org/maven2/org/codelibs/fess/fess-ds-salesforce/).

## Installation (For 13.2.x and older)

1. Download fess-ds-salesforce-X.X.X.jar
2. Copy fess-ds-salesforce-X.X.X.jar to $FESS\_HOME/app/WEB-INF/lib or /usr/share/fess/app/WEB-INF/lib

## Installation (For 13.3 and later)

1. Download fess-ds-salesforce-X.X.X.jar
2. Copy fess-ds-salesforce-X.X.X.jar to $FESS\_HOME/app/WEB-INF/plugin or /usr/share/fess/app/WEB-INF/plugin


## Getting Started

### Parameters

```
base_url=https://login.salesforce.com
auth_type=token
username=admin@example.com
client_id=3MVG9I1kFE5Iul2DewTbNB1_YcSY0ptj5SCZIhdYPfIsVczSBZWGZRYSUs8tRyRgDRucoa8IK_bEnAwRzgbSZ
private_key=MIIEvQ...
number_of_threads=1
ignoreError=true
custom=FessObj,...
FessObj.title=name
FessObj.contents=name,body
```

| Key | Value |
| --- | --- |
| base_url | URL of the Salesforce server (eg: `https://test.salesforce.com`) |
| username | Username |
| auth_type | `token` or `password` (if you choose `password`, the you have to set `client_secret` and `security_token`) |
| client_id | Consumer key |
| private_key | Private key (required for `token`) |
| client_secret | Consumer secret (required for `password`) |
| security_token | Security token (required for `password`) |
| custom | Custom object name (split by `,`) |
| ${object_name}.title | Field name that correspond title of document |
| ${object_name}.content | Field names that correspond to contents of document |

### Scripts

```
title="[" + object.type + "] " + object.title
digest=object.description
content=object.content
created=object.created
timestamp=object.last_modified
url=object.url
```

| Key | Value |
| --- | --- |
| object.type |  The type of the Salesforce object (eg: `Case`, `User`, `Solution` ...) |
| object.title | The name of the Salesforce object. |
| object.description | A short description of Salesforce object. |
| object.content | The text contents of the Salesforce object |
| object.id | The id of the Salesforce object |
| object.content_length | The content length of the Salesforce object |
| object.created | The time when the Salesforce object was created. |
| object.last_modified | The last time the Salesforce object was modified by anyone. |
| object.url | The Salesforce object URL  |
| object.thumbnail | The thumbnail URL of the Salesforce object |

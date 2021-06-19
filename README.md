Salesforce Data Store for Fess
[![Java CI with Maven](https://github.com/codelibs/fess-ds-salesforce/actions/workflows/maven.yml/badge.svg)](https://github.com/codelibs/fess-ds-salesforce/actions/workflows/maven.yml)
==========================

## Overview

Salesforce Data Store is an extension for Fess Data Store Crawling.

## Download

See [Maven Repository](http://central.maven.org/maven2/org/codelibs/fess/fess-ds-salesforce/).

## Installation 

See [Plugin](https://fess.codelibs.org/13.3/admin/plugin-guide.html) of Administration guide.

## Getting Started

### Parameters

```
base_url=https://login.salesforce.com
auth_type=oauth_token
username=admin@example.com
client_id=...
private_key=...
number_of_threads=1
ignoreError=true
custom=FessObj,...
FessObj.title=name
FessObj.contents=name,body
```

| Key | Value |
| --- | --- |
| *base_url* | URL of the Salesforce server (eg: `https://test.salesforce.com`) |
| *username* | Username |
| *auth_type* | `oauth_token` or `oauth_password` (if you choose `oauth_password`, you have to set `client_secret` and `security_token`) |
| *client_id* | Consumer key |
| *private_key* | Private key (required for `oauth_token` only) |
| *client_secret* | Consumer secret (required for `oauth_password` only) |
| *security_token* | Security token (required for `oauth_password` only) |
| *custom* | Names of the Custom Objects that you created on the Salesforce (split by `,`). |
| *${object_name}.title* | Field names of the Custom Objects that correspond to titles of documents |
| *${object_name}.content* | Field names of the Custom Objects that correspond to contents of documents |

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
| *object.type* |  The type of the Salesforce object (e.g. `Case`, `User`, `Solution` ...) |
| *object.title* | The name of the Salesforce object. |
| *object.description* | A short description of the Salesforce object. |
| *object.content* | The text contents of the Salesforce object |
| *object.id* | The id of the Salesforce object |
| *object.content_length* | The content length of the Salesforce object |
| *object.created* | The time when the Salesforce object was created. |
| *object.last_modified* | The last time the Salesforce object was modified. |
| *object.url* | The URL of the Salesforce object  |
| *object.thumbnail* | The thumbnail URL of the Salesforce object |

Salesforce Data Store for Fess [![Build Status](https://travis-ci.org/codelibs/fess-ds-salesforce.svg?branch=master)](https://travis-ci.org/codelibs/fess-ds-salesforce)
==========================

## Overview

Salesforce Data Store is an extension for Fess Data Store Crawling.

## Download

See [Maven Repository](http://central.maven.org/maven2/org/codelibs/fess/fess-ds-salesforce/).

## Installation

1. Download fess-ds-salesforce-X.X.X.jar
2. Copy fess-ds-salesforce-X.X.X.jar to $FESS\_HOME/app/WEB-INF/lib or /usr/share/fess/app/WEB-INF/lib

## Getting Started

### Parameters

```
base_url=https://login.salesforce.com
auth_type=oauth
username=admin@example.com
client_id=3MVG9I1kFE5Iul2DewTbNB1_YcSY0ptj5SCZIhdYPfIsVczSBZWGZRYSUs8tRyRgDRucoa8IK_bEnAwRzgbSZ
private_key=MIIEvQ...
custom=FessObj,...
FessObj.title=name
FessObj.contents=name,body
```

| Key | Value |
| --- | --- |
| base_url | URL of Salesforce server (eg: `https://test.salesforce.com`) |
| username | username |
| auth_type | `oauth` or `password` |
| client_id | consumer key |
| private_key | private key |
| custom | custom object name (split by `,`) |
| ${object_name}.title | field name that correspond title of document |
| ${object_name}.content | field names that correspond to contents of document |
#!/bin/sh
#curl --header "content-type: text/xml" -d @$1 http://localhost:1010/ws
curl --header "content-type: text/xml" -d @$1 http://test.soap.api.glytoucan.org/ws

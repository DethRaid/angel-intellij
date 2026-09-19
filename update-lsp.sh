#!/bin/bash

ROOT_DIR=$(pwd)

cd angel-lsp
npm run compile

cd server
npm run bundle

cd $ROOT_DIR

cp angel-lsp/server/angelscript-language-server.js src/main/resources/js/angelscript-language-server.js


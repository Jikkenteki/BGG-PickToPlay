#!/bin/bash
# Clean previous css from fontawesome package
rm -rf resources/public/css
rm -rf resources/public/webfonts

# Copies fontawesome css and webfonts
mkdir -p resources/public/css
cp node_modules/@fortawesome/fontawesome-free/css/all.min.css resources/public/css/all.min.css
cp -r node_modules/@fortawesome/fontawesome-free/webfonts resources/public/
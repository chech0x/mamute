#!/usr/bin/env bash
version="1.5.0"
IMG_VERSION="0.8"

scripts/mvn-package.sh && cp "target/mamute-$version.war" docker/production/Mamute/ && docker build -t "gcr.io/kvz-ci-sion/mamute-kvz:$IMG_VERSION" -t gcr.io/kvz-ci-sion/mamute-kvz:latest ./docker/production/Mamute/

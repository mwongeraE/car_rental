#!/bin/bash
rm service.war
cd "$(dirname "$0")"
jar -cvfM service.war -C service .
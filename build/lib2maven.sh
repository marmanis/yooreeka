#!/bin/sh
set -e
MVN=mvn
CMD="install:install-file"
#CMD="-DrepositoryId=nexus-snapshots -Durl=http://devel.net.ilb.ru:28052/nexus/content/repositories/releases deploy:deploy-file"
$MVN -DartifactId=jigg -DgroupId=de.thesuntoucher.jigg -Dversion=0.1 -Dpackaging=jar -Dfile=../lib/jigg-0.1.jar -DgeneratePom=true $CMD
$MVN -DartifactId=rooster -DgroupId=com.headzoo.net.services -Dversion=0.1 -Dpackaging=jar -Dfile=../lib/rooster.jar -DgeneratePom=true $CMD

mvn deploy:deploy-file -Dmaven.test.skip=true -Dpackaging=jar -Dfile=./smile-xt-%1.jar -DgroupId=smile -DartifactId=smile-xt -Dversion=%1 -Dpackaging=jar -DrepositoryId=releases -Durl=http://192.168.0.81:8081/nexus/content/repositories/releases/
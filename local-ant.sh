export JAVA_HOME=/usr/java/jdk1.7.0_51
export ANT_HOME=builders/apache-ant

#bootstrap can be used to download the apache ivy library if not already installed
#call ant bootstrap

#if you're offline and you already have all libraries don't call the resolve line below
#call ant resolve

#below will startup the oasis framework
#ant run

echo "executing the following ant command(s): ant $@"
ant "$@"


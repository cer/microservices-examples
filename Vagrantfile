# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "ubuntu/trusty64"

  config.vm.provider "virtualbox" do |v|
    v.memory = 2048
    v.cpus = 2
    config.vm.network "forwarded_port", guest: 8080, host: 8080
    config.vm.network "forwarded_port", guest: 8081, host: 8081
    config.vm.network "forwarded_port", guest: 5672, host: 5672
    config.vm.network "forwarded_port", guest: 27017, host: 27017
    config.vm.network "forwarded_port", guest: 8761, host: 8761
  end

  config.vm.provision "shell", inline: <<-SHELL
  #!/bin/sh

  # https://github.com/pussinboots/vagrant-devel/blob/master/provision/packages/java8.sh

  if which java >/dev/null; then
     	echo "skip java 8 installation"
  else
  	echo "java 8 installation"
  	apt-get install --yes python-software-properties
  	add-apt-repository ppa:webupd8team/java
  	apt-get update -qq
  	echo debconf shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
  	echo debconf shared/accepted-oracle-license-v1-1 seen true | /usr/bin/debconf-set-selections
  	apt-get install --quiet --yes oracle-java8-installer
  	yes "" | apt-get -f install
  fi
SHELL

  config.vm.provision "docker" do |d|
  end

  config.vm.provision "shell", inline: <<-SHELL
  if which docker-compose >/dev/null; then
     	echo "skip docker-compose installation"
  else
   sudo  bash -c "curl -L https://github.com/docker/compose/releases/download/1.6.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose ; chmod +x /usr/local/bin/docker-compose"
 fi
SHELL

end

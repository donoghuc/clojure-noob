# clojure-noob

Start building examples from https://www.braveclojure.com/ by Daniel Higganbotham

## Installation

## Install jdk 8
cas@ubuntu:~/working_dir/brave_closure$ sudo apt-get install openjdk-8-jre

## go to /bin
cas@ubuntu:~$ cd /bin

## download lein installer
sudo curl -LO https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein

## permission 
cas@ubuntu:/bin$ sudo chmod a+x lein 

## install
cas@ubuntu:/bin$ lein 

## new project
cas@ubuntu:~/working_dir/brave_closure$ lein new app clojure-noob

## run
cas@ubuntu:~/working_dir/brave_closure/clojure-noob$ lein run

## repl

## install emacs24
cas@ubuntu:~/working_dir/brave_closure/clojure-noob$ sudo apt install emacs24

## get config
cas@ubuntu:~/working_dir/brave_closure$ curl -LO https://github.com/flyingmachine/emacs-for-clojure/archive/book1.zip
cas@ubuntu:~/working_dir/brave_closure$ ls
book1.zip  clojure-noob  setup.txt
cas@ubuntu:~/working_dir/brave_closure$ unzip book1.zip 

## delete stock 
cas@ubuntu:~/working_dir/brave_closure$ ls -pla ~ | grep emacs
drwx------  2 cas  cas   4096 Mar 22 14:41 .emacs.d/
cas@ubuntu:~/working_dir/brave_closure$ rm -rf ~/.emacs.d/

## replace emacs.d
cas@ubuntu:~/working_dir/brave_closure$ ls
clojure-noob  emacs-for-clojure-book1  setup.txt
cas@ubuntu:~/working_dir/brave_closure$ mv ./emacs-for-clojure-book1 ~/.emacs.d
cas@ubuntu:~/working_dir/brave_closure$ ls -pla ~ | grep emacs
drwxrwxr-x  5 cas  cas   4096 Nov 18  2016 .emacs.d/

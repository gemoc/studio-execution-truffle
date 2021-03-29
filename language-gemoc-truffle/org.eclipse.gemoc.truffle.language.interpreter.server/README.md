Idea


create a web server serving  (using a graal compatible web technology such as SpringBoot or http://sparkjava.com/ )
role: allows to 
- start truffle interpreter for the supported languages (of the current VM)
- list running interpreters (provide the ports of the runnig interpreter so a client can connect to it
- stop/kill an interpreter
- list supported languages


this allows to keep the VM alive (and avoid the language load time) (ex: 10s for python) so a new model execution will simply run in this VM
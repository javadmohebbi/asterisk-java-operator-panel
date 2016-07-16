# Asterisk Java Operator Panel
An operator panel based on asterisk-java project (http://asterisk-java.org)

# Configuring Asterisk Server
First of all, we need to configure our asterisk server. So, connect to your asterisk server using SSH and edit the manager.cong placed in /etc/asterisk/manager.conf.

Now check the following line is available:


[general]

enabled = yes

port = 5038

bindaddr = 0.0.0.0

displayconnects=no ;only effects 1.6+


After that, we need to configure our user's access in this file. In this example we are going to let our operator connect to asterisk server from the 192.168.1.1/255.255.255.0 using username (admin) and password (secret). Add or change the following line in manager.conf

[admin]

secret = secret

deny = 0.0.0.0/0.0.0.0

permit = 127.0.0.1/255.255.255.0

permit = 192.168.1.1/255.255.255.0

read = system,call,log,verbose,command.agent,user,config,command,dtmf,reporting,cdr,dialplan,originate

write = system,call,log,verbose,command,agent,user,config,command,dtmf,reporting,cdr,dialplan,originate


# Run the app
Download the jar file ajop-{file-version}.jat from the "dist" then run the app using the following command:

java -jar ajop-v1.jar



# About
I've made this project for our personal usage and it might have some problems. I would be happy if i can have your advise and point about this project.
You can contact me using this email address (m@ir-linux.ir)

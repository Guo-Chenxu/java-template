SERVER =
PORT =
BAK_COMMAND = 'cd /home/javatemplate/backend && bash bak.sh'
START_COMMAND = 'cd /home/javatemplate/backend && bash start.sh'


deploy:
	mvn package
	ssh $(SERVER) -p $(PORT) $(BAK_COMMAND)
	bash -c 'scp -P $(PORT) ${CURDIR}/target/javatemplate.jar $(SERVER):~/backend/'
	ssh $(SERVER) -p $(PORT) $(START_COMMAND)

dsrc:
	ssh $(SERVER) -p $(PORT) $(BAK_COMMAND)
	rsync -avuz -e 'ssh -p 9642' --progress --delete --exclude='.git/' --exclude='logs/' --exclude='.vscode/' --exclude='target/' --exclude='.idea/' $(CURDIR) $(SERVER):~/backend/

ld : local_deploy

bak:
	ssh $(SERVER) -p $(PORT) $(BAK_COMMAND)

local_deploy: bak
	mvn package
	mv target/javatemplate.jar ../
	cd ../ && bash start.sh

gc:
	git commit -a -s -m '$(msg)'

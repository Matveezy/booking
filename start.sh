sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo mvn install -DskipTests
sudo docker build -t booking.jar .
sudo docker network prune -f
sudo docker-compose up --force-recreate --remove-orphans
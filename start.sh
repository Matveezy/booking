sudo mvn install -DskipTests
sudo docker build -t booking.jar .
sudo docker network prune -f
sudo docker-compose up --force-recreate --remove-orphans
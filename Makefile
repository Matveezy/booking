.PHONY: clean

clean:
	rm -rf pgdata

run:
	docker-compose up

stop:
	docker-compose down

restart: stop clean run
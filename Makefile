SERVICE=app
DOCKER_COMPOSE=docker-compose

# Build the Docker image
build:
	@echo "Building Docker image..."
	$(DOCKER_COMPOSE) build

# Build the Docker image and run the container
build-run: build
	@echo "Starting container..."
	$(DOCKER_COMPOSE) up -d

# Stop the container
down:
	@echo "Stopping container..."
	$(DOCKER_COMPOSE) down

# Stop container and remove all volumes, images (full cleanup)
fclean:
	@echo "Stopping container, removing images and volumes..."
	$(DOCKER_COMPOSE) down --rmi all -v
	@echo "Cleanup complete."

# Delete all db contents
db-clean: down
	rm -rf ./h2data/*

# Clean everything and rebuild
rebuild: fclean db-clean build

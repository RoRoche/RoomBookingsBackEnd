# Default target
default: help

lint: ## Check code formatting
	@npx prettier --check .
	@mvn sortpom:verify
	@mvn license:check
	@mvn youshallnotpass:youshallnotpass

lint-fix: ## Fix formatting automatically
	@npx prettier --write .
	@mvn sortpom:sort
	@mvn license:format

deps-updates: ## Use Maven plugin to list if there are dependencies updates
	@mvn clean validate -Pfail-on-update

help: ## Show this help message
	@echo ""
	@echo "Available targets:"
	@echo ""
	@grep -E '^[a-zA-Z0-9_-]+:[^#]*##' Makefile \
		| awk 'BEGIN {FS = "##"}; {printf "  \033[1;32m%-15s\033[0m %s\n", $$1, $$2}'
	@echo ""

.PHONY: default help lint lint-fix

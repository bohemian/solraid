.PHONY: push build

build:
	./gradlew clean build --warning-mode none

lint:
	./gradlew check # runs tests and spotbugs

rebase: build
	@bin/rebase.sh

push: rebase
	@git push -f



# `guard-commons` library #

### Purpose ###

#### Source of truth for validations, eg
  * domain name
  * org_id
  * account_id

#### Re-usable service-agnostic concerns, eg
  * standardised logging
  * utility code

## To use:

### Maven
```xml
<dependency>
    <groupId>io.atlassian</groupId>
    <artifactId>guard-commons</artifactId>
    <version>1.2</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.atlassian:guard-commons:1.2'
```


### How do I contribute? ###

* At the command line: `make` (you do not need to, and probably should not, install gradle on your machine)
* To open correctly in Intellij, Open > Project from existing sources > `build.gradle`

### Contribution guidelines ###

* Aim for 100% test coverage
* Usual code review guidelines apply
* No JUnit! Spock tests only

### Who do I talk to? ###

* Repo owner: @gedmonds

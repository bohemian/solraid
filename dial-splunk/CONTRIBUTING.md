# How to contribute to this repo
Thank you for wanting to make our library better!

## Points to note:
* All changes *must* have an associated change request (ie jira)
* We use [trunk-based development](https://www.atlassian.com/continuous-delivery/continuous-integration/trunk-based-development)
* We `git rebase` before pushing
* All PRs require approval from default reviewers (see [CODEOWNERS.md](CODEOWNERS.md))

## Help
Ask for help in Slack `#help-uam` - use tag `!yoshi`  
We'd love to hear from you!

## Steps
* Create a jira ticket (eg YOURTEAM-nnnn)
* Create a branch named `YOURTEAM-nnnn-short-description`
* Code your changes, and tests for those changes
  * bump `version` in [gradle.properties](gradle.properties)
* Run `make` at the command line
* Rebase to `master` branch:
    1. `git add .`
    1. `git commit -m "A short description"`
    1. `git fetch`
    1. `git rebase origin/master`
    1. If there are conflicts:
        * Resolve conflicts
        * `git add .`
        * `git rebase --continue`
        * Repeat
* Run `make` at the command line again to be sure
* `git push`
* Create pull request, filling in all details
    * include link to the jira

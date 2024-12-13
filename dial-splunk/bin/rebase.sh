# prepares your branch for pushing
if [[ $(git status | grep -E 'Changes not staged for commit|Changes to be committed' | wc -l) -ne 0 ]]; then
    read -r -p 'Commit message (blank to abort): ' COMMIT_MSG
    if [[ -z "$COMMIT_MSG" ]]; then
        exit 1
    fi
    git add .
    git commit -m "$COMMIT_MSG"
fi

git fetch
git rebase origin/master

if [[ $? -ne 0 ]]; then
    echo "Resolve conflicts"
    exit 1
fi

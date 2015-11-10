# Submitting changes to Collide #

## If you are a committer, you have 3 options. ##

1) Commit to master, then ask for review.  Use your best judgement here; this is best-suited to small, safe, non-controversial changes.

2) Commit to a new branch, then ask for review of that.  Say you have a branch named 'bugfix'.

```
git push origin bugfix:username-bugfix
```

Now ask someone to review your public branch.  Once that's done, your next move depends on whether or not your branch is ahead of master.  If it's already ahead of master, you can simply:

```
git push origin bugfix:master
```

This will fail if you're not ahead of master.  In which case:

```
git checkout master
git merge origin/master # should be a pure fast-forward
git merge --squash bugfix
git push origin master
```

Once you're done, remove the public branch:

```
git push origin :username-bugfix
```

3) Attach a patch to an issue, ask for review.  Committing would be a similar process as #2, minus the need to futz with a public branch.

## If you are not a committer, you have 2 options ##

### Option 1: submit a patch ###

Create a new issue in the tracker to ask for a code review.  Email a link to the issue to collide-project.

### Option 2: use git ###

1) Create a clone if you don't already have one.

See: http://code.google.com/p/collide/source/clones

2) Push a change into a branch on your clone.

3) Email a link to your branch to collide-project.
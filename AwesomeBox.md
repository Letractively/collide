# Introduction #

The AwesomeBox is in a weird split state where it was being upgraded to fit a new component host type model allowing it to be more versatile than the original design.  This means that it is a little confusing to read the code and understand the package design/layout decisions since they make almost no sense.  This page should clarify what the end-goal is and the immediate state with which collide was launched.

# The Package Layout #
Long-term the AwesomeBox should be within the clientlibs hierarchy instead of part of the main client blob.  Once it's dependency situation is fixed and the main AwesomeBox component is rewritten it will be moved.

Long term I expect that the current client.search.awesomebox.**packages will be renamed and redistributed as follows:**

## clientlibs.awesomebox.components ##
This package will hold all components which the awesomebox can host.  In the current release of collide this is limited to the find/replace dialog and the finder like help menu (the original "AwesomeBox") which allows you to search for commands as you type.

## clientlibs.awesomebox.host ##
This package holds the awesomebox host framework.  It defines interfaces which components must implement and provides the component host control which is attached to the DOM.  Clients of the AwesomeBox interact with the host class to attach and show AwesomeBox components on demand.  This will effectively just be a rename of client.search.awesomebox.host.

## clientlibs.awesomebox.shared ##
This package holds any shared resources.  Some of this is a hold-over from previous build constraints but in general the CSS resources go here as well as any shared utility code.

# What's Left to do? #
The AwesomeBox is currently setup to display sections of data in a rudimentary way.  The new UX direction is a flat list where the results of the user's query are ranked in a meaningful and intelligent way.  This way we can simplify the selection of items and make it seem more magical.

The current implementation has some baggage which will need to be rewritten and ported.  We need to get rid of the AwesomeBoxContext stuff and more or less remove the section stuff -- though the commands will have to have a new organization structure of some kind (TBD).  Good news is most of the functionality of the AwesomeBox was moved into AwesomeBoxComponentHost so the new version just needs to be a dropdown menu with an intelligent ranker (which has to be written).
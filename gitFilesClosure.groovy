#!/usr/bin/env groovy

/*
 * Inspired by:
 * http://michaelfeathers.typepad.com/michael_feathers_blog/2012/12/the-active-set-of-classes.html
 *
 * Before executing this script run the following command from command line:
 * git log --name-status --no-merges --date=short | grep '\(^Date:\|^[ADM]\>.\+\.java$\)' | uniq > history.txt
 */
 
added = [:]
changed = [:]

new File('history.txt').eachLine { line ->
    tokens = line.split()
    if (tokens[0] == 'Date:') {
        cdate = tokens[1].substring(0,7) // yyyy-MM
    } else {
        klass = tokens[1]
        if (changed[klass] == null) changed[klass] = cdate
        if (tokens[0] == 'A') added[klass] = cdate
    }
}

def collectDates(fileDateMap) {
    fileDateMap.values().inject([:].withDefault {0}) { acc, v ->
        acc[v]++
        acc
    }.sort().inject([[:],0]) { acc, v ->
        acc[1] += v.value
        acc[0][v.key] = acc[1]
        acc
    }[0]
}

println "Changed: ${collectDates(changed)}"
println "Added: ${collectDates(added)}"

